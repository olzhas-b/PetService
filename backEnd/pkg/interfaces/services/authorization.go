package services

import "github.com/olzhas-b/PetService/backEnd/pkg/models"

type IAuthorizationService interface {
	CheckToken(token string, isAccess bool) (err error)
	ParseToken(token string, isAccess bool) (tokenClaims models.TokenClaim, err error)
}
