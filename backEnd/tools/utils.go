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
	return StrToInt64(ctx.Get(consts.UserID, "0"))
}

func GetUserTypeByCtx(ctx *fiber.Ctx) (userType models.UserType) {
	return models.UserType(StrToInt64(ctx.Get(consts.UserType, "0")))
}
