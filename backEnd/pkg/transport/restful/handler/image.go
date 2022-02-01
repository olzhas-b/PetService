package handler

import (
	"fmt"
	"github.com/gofiber/fiber/v2"
	"github.com/olzhas-b/PetService/backEnd/consts"
	"github.com/olzhas-b/PetService/backEnd/pkg/transport/restful/common"
	"github.com/olzhas-b/PetService/backEnd/tools"
)

func (h *Handler) CtlGetImage(ctx *fiber.Ctx) error {
	url := ctx.Params("fileName")
	image, err := h.services.IImageService.ServiceGetImageByFileName(url)
	if err != nil {
		return err
	}
	ctx.Set("Content-Type", fmt.Sprintf("%s;charset=%s", image.ContentType, "utf-8"))
	ctx.Set("Content-Length", tools.IntToStr(len(image.Content)))
	return ctx.Send(image.Content)
}

func (h *Handler) CtlCreateImage(ctx *fiber.Ctx) error {
	form, err := ctx.MultipartForm()
	if err != nil {
		return common.GenShortResponse(ctx, consts.FileUploadErr, "", err.Error())
	}
	files := form.File["files"]
	err = h.services.IImageService.ServiceSaveImage(files)
	if err != nil {
		return common.GenShortResponse(ctx, consts.FileUploadErr, err.Error(), err.Error())
	}
	return nil
}

func (h *Handler) CtlUpdateImage(ctx *fiber.Ctx) error {
	return nil
}

func (h *Handler) CtlDeleteImage(ctx *fiber.Ctx) error {
	id := tools.GetUserIdByCtx(ctx)
	if id == 0 {
		return fmt.Errorf("userID equal to zero")
	}
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
