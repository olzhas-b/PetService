package handler

import (
	"github.com/gofiber/fiber/v2"
	"github.com/olzhas-b/PetService/backEnd/consts"
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"github.com/olzhas-b/PetService/backEnd/pkg/transport/restful/common"
	"github.com/olzhas-b/PetService/backEnd/tools"
	"github.com/olzhas-b/PetService/backEnd/tools/utils"
)

func (h *Handler) CtlEstimateUser(ctx *fiber.Ctx) error {
	estimatorID := utils.GetCurrentUser(ctx)
	userID := tools.StrToInt64(ctx.Params("userID"))
	score := tools.StrToInt64(ctx.Query("score"))
	rating := models.Rating{
		UserID:      userID,
		EstimatorID: estimatorID,
		Score:       int8(score),
	}
	if err := h.services.IRatingService.ServiceEstimate(ctx.Context(), rating); err != nil {
		return common.GenShortResponse(ctx, consts.NotFoundErr, err.Error(), err.Error())
	}
	return common.GenShortResponse(ctx, consts.Success, "successfully estimated", "")
}
