package handler

import (
	"github.com/gofiber/fiber/v2"
	"github.com/olzhas-b/PetService/backEnd/consts"
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"github.com/olzhas-b/PetService/backEnd/pkg/transport/restful/common"
	"github.com/olzhas-b/PetService/backEnd/tools"
)

func (h *Handler) CtlEstimateUser(ctx *fiber.Ctx) error {
	userID := tools.StrToInt64(ctx.Params("userID"))
	score := tools.StrToInt64(ctx.Query("score"))

	rating := models.Rating{
		UserID: userID,
		Score:  int8(score),
	}
	if err := h.services.IRatingService.ServiceEstimate(ctx.Context(), rating); err != nil {
		return common.GenShortResponse(ctx, consts.DBInsertErr, err.Error(), err.Error())
	}
	return common.GenShortResponse(ctx, consts.Success, "successfully estimated", "")
}
