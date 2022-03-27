package handler

import (
	"github.com/gofiber/fiber/v2"
	"github.com/olzhas-b/PetService/backEnd/consts"
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"github.com/olzhas-b/PetService/backEnd/pkg/transport/restful/common"
	"github.com/olzhas-b/PetService/backEnd/tools"
)

func (h *Handler) CtlGetFavoriteServiceProvider(ctx *fiber.Ctx) error {
	result, err := h.services.IServiceProviderService.ServiceGetFavoriteServices(ctx.Context())
	if err != nil {
		return err
	}
	return common.GenShortResponse(ctx, consts.Success, result.ConvertListToDTO(), "")
}

func (h *Handler) CtlAddToFavorite(ctx *fiber.Ctx) error {
	favorite := models.Favorite{
		ServiceID: tools.StrToInt64(ctx.Params("serviceID")),
	}
	err := h.services.IFavoriteService.ServiceAddToFavorite(ctx.Context(), favorite)
	if err != nil {
		return common.GenShortResponse(ctx, consts.DBInsertErr, "", "")
	}
	return common.GenShortResponse(ctx, consts.Success, "", "")
}

func (h *Handler) CtlRemoveFromFavorite(ctx *fiber.Ctx) error {
	favorite := models.Favorite{
		ServiceID: tools.StrToInt64(ctx.Params("serviceID")),
	}
	err := h.services.IFavoriteService.ServiceRemoveFromFavorite(ctx.Context(), favorite)
	if err != nil {
		return common.GenShortResponse(ctx, consts.DBInsertErr, "", "")
	}
	return common.GenShortResponse(ctx, consts.Success, "", "")
}
