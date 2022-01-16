package handler

import (
	"github.com/gofiber/fiber/v2"
	"github.com/olzhas-b/PetService/backEnd/tools"
	"strings"
)

func (h *Handler) CtlGetImage(ctx *fiber.Ctx) error {
	name := ctx.Params("id")
	list := strings.Split(name, ".")
	id := tools.StrToInt64(list[0])
	image, err := h.services.IImageService.ServiceGetImage(id)
	if err != nil {
		return err
	}
	return ctx.JSON(image)
}

func (h *Handler) CtlCreateImage(ctx *fiber.Ctx) error {
	return nil
}

func (h *Handler) CtlUpdateImage(ctx *fiber.Ctx) error {
	return nil
}

func (h *Handler) CtlDeleteImage(ctx *fiber.Ctx) error {
	return nil
}

//func GetImage(w http.ResponseWriter, r *http.Request) {
//	file, _ := os.Open("/home/olzhas/Desktop/banner_logo.png")
//	fileB, _ := ioutil.ReadAll(file)
//	w.WriteHeader(200)
//	w.Header().Set("Content-Disposition", "attachment")
//	w.Header().Set("Content-Type", "application/png")
//	w.Write(fileB)
//	return
//}
