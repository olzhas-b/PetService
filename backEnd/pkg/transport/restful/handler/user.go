package handler

import (
	"github.com/gofiber/fiber/v2"
	"github.com/olzhas-b/PetService/backEnd/modules/logger"
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"github.com/olzhas-b/PetService/backEnd/pkg/models/filter"
	"github.com/olzhas-b/PetService/backEnd/tools"
	"io/ioutil"
	"net/http"
	"os"
)

func (h *Handler) CtlGetUser(ctx *fiber.Ctx) error {
	logger.WithContext(ctx.Context()).Info("information")
	id := ctx.Params("id")
	user, err := h.services.IUserService.ServiceGetUserByID(tools.StrToInt64(id))
	if err != nil {
		return err
	}
	return ctx.JSON(user)
}

func (h *Handler) CtlCreateUser(ctx *fiber.Ctx) error {
	var user models.User
	if err := ctx.BodyParser(&user); err != nil {
		return err
	}
	result, err := h.services.IUserService.ServiceCreateUser(user)
	if err != nil {
		return err
	}
	return ctx.JSON(result)
}

func (h *Handler) CtlGetAllUsers(ctx *fiber.Ctx) error {
	f := new(filter.User)
	f.Fill(ctx)
	result, err := h.services.IUserService.ServiceGetAllUsers(f)
	if err != nil {
		return err
	}
	return ctx.JSON(result)
}

func (h *Handler) CtlHandler(w http.ResponseWriter, r http.Request) {
	print("hello image")
	file, _ := os.Open("/home/olzhas/Desktop/banner_logo.png")
	fileB, _ := ioutil.ReadAll(file)
	w.WriteHeader(200)
	w.Header().Set("Content-Type", "application/octet-stream")
	w.Write(fileB)
	return
}
