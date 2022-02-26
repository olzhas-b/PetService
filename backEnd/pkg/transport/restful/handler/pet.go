package handler

import (
	"github.com/gofiber/fiber/v2"
	"github.com/olzhas-b/PetService/backEnd/consts"
	"github.com/olzhas-b/PetService/backEnd/pkg/transport/restful/common"
	"github.com/olzhas-b/PetService/backEnd/tools"
	"github.com/olzhas-b/PetService/backEnd/tools/custom-parser"
	"github.com/olzhas-b/PetService/backEnd/tools/utils"
)

func (h *Handler) CtlGetAllPets(ctx *fiber.Ctx) error {
	result, err := h.services.IPetService.ServiceGetAllPets(ctx.Context())
	if err != nil {
		return common.GenShortResponse(ctx, consts.NotFoundErr, err.Error(), err.Error())
	}
	return common.GenShortResponse(ctx, consts.Success, result.ConvertToDTO(), "")
}

func (h *Handler) CtlGetPet(ctx *fiber.Ctx) error {
	id := ctx.Params("id")
	result, err := h.services.IPetService.ServiceGetPetsByUserID(ctx.Context(), tools.StrToInt64(id))
	if err != nil {
		return common.GenShortResponse(ctx, consts.NotFoundErr, err.Error(), err.Error())
	}

	return common.GenShortResponse(ctx, consts.Success, result.ConvertToDTO(), "")
}

func (h *Handler) CtlUpdatePet(ctx *fiber.Ctx) error {
	id := ctx.Params("id")
	userID := utils.GetCurrentUser(ctx)

	pet, image, err := custom_parser.PetParser(ctx)
	if err != nil {
		return common.GenShortResponse(ctx, consts.BindingJsonErr, err.Error(), err.Error())
	}
	pet.ID = tools.StrToInt64(id)

	result, err := h.services.IPetService.ServiceUpdatePet(ctx.Context(), pet, image, userID)
	if err != nil {
		return common.GenShortResponse(ctx, consts.DBUpdateErr, err.Error(), err.Error())
	}

	return common.GenShortResponse(ctx, consts.Success, result.ConvertToDTO(), "")
}

func (h *Handler) CtlCreateOrUpdatePet(ctx *fiber.Ctx) error {
	requestType := ctx.Params("id")

	pet, image, err := custom_parser.PetParser(ctx)
	if err != nil {
		return common.GenShortResponse(ctx, consts.BindingJsonErr, err.Error(), err.Error())
	}

	result, err := h.services.IPetService.ServiceCreateOrUpdatePet(ctx.Context(), pet, image, requestType)
	if err != nil {
		return common.GenShortResponse(ctx, consts.DBInsertErr, err.Error(), err.Error())
	}

	return common.GenShortResponse(ctx, consts.Success, result.ConvertToDTO(), "")
}

func (h *Handler) CtlDeletePet(ctx *fiber.Ctx) error {
	id := tools.StrToInt64(ctx.Params("id"))
	userID := utils.GetCurrentUser(ctx)
	err := h.services.IPetService.ServiceDeletePet(ctx.Context(), userID, id)
	if err != nil {
		return common.GenShortResponse(ctx, consts.DBDeleteErr, err.Error(), err.Error())
	}
	return common.GenShortResponse(ctx, consts.Success, "successfully deleted", "")
}
