package handler

import (
	"encoding/json"
	"fmt"
	"github.com/gofiber/fiber/v2"
	"github.com/olzhas-b/PetService/backEnd/consts"
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"github.com/olzhas-b/PetService/backEnd/pkg/models/filter"
	"github.com/olzhas-b/PetService/backEnd/pkg/transport/restful/common"
	"github.com/olzhas-b/PetService/backEnd/tools"
	"github.com/olzhas-b/PetService/backEnd/tools/utils"
	"mime/multipart"
)

func (h *Handler) CtlGetUser(ctx *fiber.Ctx) error {
	id := ctx.Params("id")
	user, err := h.services.IUserService.ServiceGetUserByID(tools.StrToInt64(id))
	if err != nil {
		return err
	}
	return common.GenShortResponse(ctx, consts.Success, user.ConvertToDto(), "")
}

func (h *Handler) CtlGetCurrentUser(ctx *fiber.Ctx) error {
	userID := utils.GetCurrentUser(ctx)
	user, err := h.services.IUserService.ServiceGetUserByID(userID)
	if err != nil {
		return err
	}
	return common.GenShortResponse(ctx, consts.Success, user.ConvertToDto(), "")
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
	return common.GenShortResponse(ctx, consts.Success, result, "")
}

func (h *Handler) CtlGetAllUsers(ctx *fiber.Ctx) error {
	f := new(filter.User)
	f.Fill(ctx)
	result, err := h.services.IUserService.ServiceGetAllUsers(f)
	if err != nil {
		return err
	}
	return common.GenShortResponse(ctx, consts.Success, result, "")
}

func (h *Handler) CtlUpdateUser(ctx *fiber.Ctx) error {
	var file *multipart.FileHeader
	var user models.User
	form, _ := ctx.MultipartForm()
	body := form.Value["body"]
	if len(body) == 0 {
		return fmt.Errorf("not enough data to create")
	}
	if err := json.Unmarshal([]byte(body[0]), &user); err != nil {
		return common.GenShortResponse(ctx, consts.BindingJsonErr, "не смогли запарсить", "")
	}
	if form.File != nil && len(form.File["image"]) != 0 {
		file = form.File["image"][0]
	}

	updateUser, err := h.services.IUserService.ServiceUpdateUser(ctx.Context(), user, file)
	if err != nil {
		return common.GenShortResponse(ctx, consts.DBUpdateErr, err.Error(), "")
	}

	return common.GenShortResponse(ctx, consts.Success, updateUser.ConvertToDto(), "")
}
