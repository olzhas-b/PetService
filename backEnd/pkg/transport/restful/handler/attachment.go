package handler

import (
	"fmt"
	"github.com/gofiber/fiber/v2"
	"github.com/olzhas-b/PetService/backEnd/consts"
	"github.com/olzhas-b/PetService/backEnd/pkg/transport/restful/common"
	"github.com/olzhas-b/PetService/backEnd/tools"
)

func (h *Handler) CtlGetAttachment(ctx *fiber.Ctx) error {
	id := ctx.Params("id")
	result, err := h.services.IAttachmentService.ServiceGetAttachmentByID(ctx.Context(), tools.StrToInt64(id))
	if err != nil {
		return common.GenShortResponse(ctx, consts.GetFileErr, err.Error(), "")
	}
	ctx.Set("Content-Type", fmt.Sprintf("%s;charset=%s", result.ContentType, "utf-8"))
	ctx.Set("Content-Length", tools.IntToStr(len(result.Content)))
	return ctx.Send(result.Content)
}

func (h *Handler) CtlCreateAttachments(ctx *fiber.Ctx) error {
	form, err := ctx.MultipartForm()
	petID := ctx.Params("id")
	if err != nil {
		return common.GenShortResponse(ctx, consts.GetFileErr, err.Error(), err.Error())
	}
	files, ok := form.File["attachments"]
	if !ok || len(files) == 0 {
		return common.GenShortResponse(ctx, consts.GetFileErr, "empty file error", err.Error())
	}

	if err := h.services.IAttachmentService.ServiceCreateAttachment(ctx.Context(), files, tools.StrToInt64(petID)); err != nil {
		return common.GenShortResponse(ctx, consts.FileUploadErr, err.Error(), "")
	}
	return common.GenShortResponse(ctx, consts.Success, "successfully uploaded", "")
}

func (h *Handler) CtlDeleteAttachment(ctx *fiber.Ctx) error {
	id := ctx.Params("id")
	if err := h.services.IAttachmentService.ServiceDeleteAttachmentByID(ctx.Context(), tools.StrToInt64(id)); err != nil {
		return common.GenShortResponse(ctx, consts.DBDeleteErr, err.Error(), "")
	}
	return common.GenShortResponse(ctx, consts.Success, "successfully deleted", "")
}
