package handler

import (
	"encoding/json"
	"fmt"
	"github.com/gofiber/fiber/v2"
	"github.com/olzhas-b/PetService/backEnd/consts"
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	filter2 "github.com/olzhas-b/PetService/backEnd/pkg/models/filter"
	"github.com/olzhas-b/PetService/backEnd/pkg/transport/restful/common"
	"github.com/olzhas-b/PetService/backEnd/tools"
	"mime/multipart"
)

func (h *Handler) CtlGetAllServiceProvider(ctx *fiber.Ctx) error {
	var filter filter2.ServiceProviderFilter
	filter.Fill(ctx)
	result, total, err := h.services.IServiceProviderService.ServiceGetAllServices(ctx.Context(), filter)
	if err != nil {
		return err
	}
	response := map[string]interface{}{
		"total": total,
		"rows":  result.ConvertListToDTO(),
	}
	return common.GenShortResponse(ctx, consts.Success, response, "")
}

func (h *Handler) CtlCreateService(ctx *fiber.Ctx) error {
	requestType := ctx.Params("id")
	var files []*multipart.FileHeader
	var service models.Service
	form, err := ctx.MultipartForm()
	body := form.Value["body"]
	if len(body) == 0 {
		return fmt.Errorf("not enough data to create")
	}
	if err := json.Unmarshal([]byte(body[0]), &service); err != nil {
		return common.GenShortResponse(ctx, consts.DBDeleteErr, "не смогли запарсить", "")
	}
	if form.File != nil {
		files = form.File["images"]
	}
	result, err := h.services.IServiceProviderService.ServiceCreateService(ctx.Context(), service, files, requestType)
	if err != nil {
		return fmt.Errorf("Handler.CtlCreateService: %w", err)
	}
	return common.GenShortResponse(ctx, consts.Success, result.ID, "")
}

func (h *Handler) CtlDeleteService(ctx *fiber.Ctx) error {
	id := tools.StrToInt64(ctx.Params("id"))
	err := h.services.IServiceProviderService.ServiceDeleteService(ctx.Context(), id)
	if err != nil {
		return common.GenShortResponse(ctx, consts.DBDeleteErr, id, "")
	}
	return common.GenShortResponse(ctx, consts.Success, "successfully deleted", "")
}
