package handler

import (
	"github.com/gofiber/fiber/v2"
	"github.com/olzhas-b/PetService/backEnd/consts"
	"github.com/olzhas-b/PetService/backEnd/pkg/transport/restful/common"
)

func (h *Handler) CtlGetCountries(ctx *fiber.Ctx) error {
	result, err := h.services.ICountryService.ServiceGetAllCountries()
	if err != nil {
		return common.GenShortResponse(ctx, consts.NotFoundErr, "", "")
	}
	return common.GenShortResponse(ctx, consts.Success, result.ConvertToDTO(), "")
}
