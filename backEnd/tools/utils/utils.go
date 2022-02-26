package utils

import (
	"context"
	"github.com/gofiber/fiber/v2"
	"github.com/olzhas-b/PetService/backEnd/consts"
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"github.com/olzhas-b/PetService/backEnd/tools"
	"io/ioutil"
	"os"
	"strings"
)

func GetToken(ctx *fiber.Ctx) string {
	header := ctx.Get("Authorization")
	if header == "" {
		return ""
	}
	parsedHeader := strings.Split(header, " ")
	if len(parsedHeader) != 2 || parsedHeader[0] != "Bearer" {
		return ""
	}
	return parsedHeader[1]
}

func GetCurrentUser(ctx *fiber.Ctx) int64 {
	userId, ok := ctx.Locals(consts.UserID).(string)
	if !ok {
		return 0
	}
	return tools.StrToInt64(userId)
}

func GetCurrentUserID(ctx context.Context) int64 {
	userID, ok := ctx.Value("userID").(string)
	if !ok {
		return 0
	}
	return tools.StrToInt64(userID)
}

func GetUserTypeByCtx(ctx *fiber.Ctx) models.UserType {
	userTypeStr, ok := ctx.Locals(consts.UserType).(string)
	if !ok {
		return 0
	}
	return models.UserType(tools.StrToInt64(userTypeStr))
}

func GetCurrentUserTypeByCtx(ctx context.Context) models.UserType {
	userTypeStr, ok := ctx.Value(consts.UserType).(string)
	if !ok {
		return 0
	}
	return models.UserType(tools.StrToInt64(userTypeStr))
}

func GetLocalImage(path string) (image models.Image) {
	file, err := os.Open(path)
	if err != nil {
		return
	}
	defer file.Close()
	bs, _ := ioutil.ReadAll(file)
	return models.Image{
		Content: bs,
	}
}
