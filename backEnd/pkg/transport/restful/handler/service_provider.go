package handler

import (
	"encoding/json"
	"fmt"
	"github.com/gofiber/fiber/v2"
	"github.com/olzhas-b/PetService/backEnd/consts"
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	filter2 "github.com/olzhas-b/PetService/backEnd/pkg/models/filter"
	"github.com/olzhas-b/PetService/backEnd/pkg/transport/restful/common"
	"github.com/olzhas-b/PetService/backEnd/tools/utils"
	"mime/multipart"
)

func (h *Handler) CtlGetAllServiceProvider(ctx *fiber.Ctx) error {
	var filter filter2.ServiceProviderFilter
	filter.Fill(ctx)
	result, err := h.services.IServiceProviderService.ServiceGetAllServices(ctx.Context(), filter)
	if err != nil {
		return err
	}
	return common.GenShortResponse(ctx, consts.Success, result.ConvertListToDTO(), "")
}

func (h *Handler) CtlCreateService(ctx *fiber.Ctx) error {
	id := utils.GetCurrentUser(ctx)
	userType := utils.GetUserTypeByCtx(ctx)
	if id == 0 || userType == 1 {
		return fmt.Errorf("userID equal to 0")
	}
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
	service.UserID = id
	result, err := h.services.IServiceProviderService.ServiceCreateService(ctx.Context(), service, files)
	if err != nil {
		return fmt.Errorf("Handler.CtlCreateService: %w", err)
	}
	return common.GenShortResponse(ctx, consts.Success, result.ID, "")
}
