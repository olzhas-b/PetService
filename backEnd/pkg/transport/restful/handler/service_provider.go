package handler

import (
	"fmt"
	"github.com/gofiber/fiber/v2"
	"github.com/olzhas-b/PetService/backEnd/consts"
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	filter2 "github.com/olzhas-b/PetService/backEnd/pkg/models/filter"
	"github.com/olzhas-b/PetService/backEnd/pkg/transport/restful/common"
)

func (h *Handler) CtlGetAllServiceProvider(ctx *fiber.Ctx) error {
	var filter filter2.ServiceProviderFilter
	filter.Fill(ctx)
	result, err := h.services.IServiceProviderService.ServiceGetAllServices(ctx.Context(), filter)
	if err != nil {
		return err
	}
	return common.GenShortResponse(ctx, consts.Success, result, "")
}

func (h *Handler) CtlCreateService(ctx *fiber.Ctx) error {
	id := 9
	var service models.Service
	if err := ctx.BodyParser(&service); err != nil {
		response := models.ResponseError{
			Description:          "wtf",
			MessageFromDeveloper: fmt.Errorf("Coulnd't parse body with error: %v", err).Error(),
		}
		return common.GenShortResponse(ctx, consts.DBDeleteErr, response, "")
	}
	service.UserID = int64(id)
	result, err := h.services.IServiceProviderService.ServiceCreateService(ctx.Context(), service)
	if err != nil {
		return fmt.Errorf("Handler.CtlCreateService: %w", err)
	}
	return common.GenShortResponse(ctx, consts.Success, result, "")
}
