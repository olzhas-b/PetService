package services

import (
	"fmt"
	jwt "github.com/dgrijalva/jwt-go"
	"github.com/go-redis/redis"
	"github.com/olzhas-b/PetService/backEnd/consts"
	config2 "github.com/olzhas-b/PetService/backEnd/pkg/config"
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"github.com/olzhas-b/PetService/backEnd/pkg/repositories"
	"time"
)

type AuthorizationService struct {
	redis *redis.Client
	Services
	repo repositories.Repositories
}

func NewAuthorizationService(redis *redis.Client) *AuthorizationService {
	return &AuthorizationService{redis: redis}
}

func (s *AuthorizationService) CheckToken(token string, isAccess bool) (err error) {
	claims, err := s.ParseToken(token, isAccess)
	if err != nil {
		return fmt.Errorf("AuthorizationService.CheckToken Couldn't parse token err: %v", err)
	}
	id := claims.ID

	var key string
	if isAccess {
		key = fmt.Sprintf("%s_%d", consts.AccessToken, id)
	} else {
		key = fmt.Sprintf("%s_%d", consts.RefreshToken, id)
	}

	if err := s.redis.Get(key).Err(); err != nil {
		return fmt.Errorf("AuthorizationService.CheckToken Couldn't find token in redis: %v", err)
	}
	return nil
}

func (s *AuthorizationService) ParseToken(token string, isAccess bool) (tokenClaim models.TokenClaim, err error) {
	config := config2.Get().Token
	JwtToken, err := jwt.Parse(token, func(token *jwt.Token) (interface{}, error) {
		if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
			return nil, fmt.Errorf("failed to extract token's metadata, unexpected signing method: %v", token.Header["alg"])
		}
		if isAccess {
			return []byte(config.AccessSecret), nil
		}
		return []byte(config.RefreshSecret), nil
	})
	if err != nil {
		return
	}

	claims, ok := JwtToken.Claims.(jwt.MapClaims)

	if ok && JwtToken.Valid {
		userID, ok := claims["id"].(float64)
		if !ok {
			return tokenClaim, fmt.Errorf("field id not found")
		}
		tokenClaim.ID = int64(userID)

		iat, ok := claims["iat"].(float64)
		if !ok {
			return tokenClaim, fmt.Errorf("field iat not found")
		}
		tokenClaim.Iat = int64(iat)

		userType, ok := claims["userType"].(float64)
		if !ok {
			return tokenClaim, fmt.Errorf("field userType not found")
		}
		tokenClaim.UserType = int64(userType)

		exp, ok := claims["exp"].(float64)
		if !ok {
			return tokenClaim, fmt.Errorf("field exp not found")
		}
		expTime := time.Unix(int64(exp), 0)
		if time.Now().After(expTime) {
			return tokenClaim, fmt.Errorf("token expired")
		}
		tokenClaim.Exp = int64(exp)
		return
	}
	return tokenClaim, fmt.Errorf("invalid token")
}
