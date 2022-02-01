package tools

import (
	"github.com/gofiber/fiber/v2"
	"github.com/olzhas-b/PetService/backEnd/consts"
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"strings"
)

func GetToken(ctx *fiber.Ctx) (token string) {
	header := ctx.Get("Authorization")
	if header == "" {
		return
	}
	parsedHeader := strings.Split(header, " ")
	if len(parsedHeader) != 2 || parsedHeader[0] != "Bearer" {
		return
	}
	token = parsedHeader[1]
	return
}

func GetUserIdByCtx(ctx *fiber.Ctx) (ID int64) {
	userId, ok := ctx.Locals(consts.UserID).(string)
	if !ok {
		return 0
	}
	return StrToInt64(userId)
}

func GetUserTypeByCtx(ctx *fiber.Ctx) (userType models.UserType) {
	userTypeStr, ok := ctx.Locals(consts.UserType).(string)
	if !ok {
		return 0
	}
	return models.UserType(StrToInt64(userTypeStr))
}
