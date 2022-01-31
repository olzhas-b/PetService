package handler

import (
	"fmt"
	"github.com/gofiber/fiber/v2"
	"github.com/olzhas-b/PetService/backEnd/consts"
	"github.com/olzhas-b/PetService/backEnd/pkg/transport/restful/common"
	"github.com/olzhas-b/PetService/backEnd/tools"
)

func (h *Handler) CtlGetServiceDetail(ctx *fiber.Ctx) error {
	id := tools.StrToInt64(ctx.Params("id", "0"))
	result, err := h.services.IServiceDetailService.ServiceGetServiceDetail(id)
	if err != nil {
		return fmt.Errorf("CtlGetServiceDetail failed with err: %v", err)
	}
	return common.GenShortResponse(ctx, consts.Success, result, "")
}
