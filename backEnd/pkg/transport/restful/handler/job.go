package handler

import (
	"github.com/gofiber/fiber/v2"
)

func (h *Handler) CtlStartJob(ctx *fiber.Ctx) error {
	code := ctx.Params("code")
	h.job.StartJob(ctx.Context(), code)
	return nil
}
