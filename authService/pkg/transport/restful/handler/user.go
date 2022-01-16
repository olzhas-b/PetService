package handler

import (
	"github.com/gofiber/fiber/v2"
	"github.com/olzhas-b/PetService/authService/consts"
	"github.com/olzhas-b/PetService/authService/modules/logger"
	"github.com/olzhas-b/PetService/authService/pkg/models"
	"github.com/olzhas-b/PetService/authService/pkg/models/filter"
	"github.com/olzhas-b/PetService/authService/pkg/transport/restful/common"
	"github.com/olzhas-b/PetService/authService/tools"
)

func (h *Handler) CtlGetUser(ctx *fiber.Ctx) error {
	logger.WithContext(ctx.Context()).Info("information")
	id := ctx.Params("id")
	user, err := h.services.IUserService.ServiceGetUserByID(tools.StrToInt64(id))
	if err != nil {
		return err
	}
	return ctx.JSON(user)
}

func (h *Handler) CtlCreateUser(ctx *fiber.Ctx) error {
	var user models.User
	if err := ctx.BodyParser(&user); err != nil {
		return err
	}
	result, err := h.services.IUserService.ServiceCreateUser(user)
	if err != nil {
		return err
	}
	return ctx.JSON(result)
}

func (h *Handler) CtlSignIn(ctx *fiber.Ctx) error {
	println("sing-in request")
	userCred := extractCredential(ctx)
	accessToken, refreshToken, err := h.services.ServiceSignIn(userCred)
	if err != nil {
		return common.GenShortResponse(ctx, consts.DBSelectErr, "", "")
	}
	resp := map[string]string{
		"accessToken":  accessToken,
		"refreshToken": refreshToken,
	}
	return common.GenShortResponse(ctx, consts.Success, resp, "")
}

func (h *Handler) CtlSignOut(ctx *fiber.Ctx) error {
	token := tools.GetToken(ctx)
	err := h.services.IUserService.ServiceLogOut(token)
	return err
}

func (h *Handler) CtlUpdateToken(ctx *fiber.Ctx) error {
	return nil
}

func (h *Handler) CtlServiceGetAllUsers(ctx *fiber.Ctx) error {
	f := new(filter.User)
	f.Fill(ctx)
	result, err := h.services.IUserService.ServiceGetAllUsers(f)
	if err != nil {
		return err
	}
	return ctx.JSON(result)
}

func extractCredential(ctx *fiber.Ctx) (userCred models.UserCredential) {
	cred := struct {
		Username string
		Password string
	}{}
	ctx.BodyParser(&cred)

	userCred.Username, userCred.Password = ctx.FormValue("username"), ctx.FormValue("password")
	return
}
