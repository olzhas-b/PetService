package handler

import (
	"github.com/gofiber/fiber/v2"
	"github.com/gofiber/fiber/v2/middleware/recover"
	"github.com/olzhas-b/PetService/backEnd/pkg/jobs"
	"github.com/olzhas-b/PetService/backEnd/pkg/services"
	"github.com/olzhas-b/PetService/backEnd/pkg/transport/restful/middles"
)

type Handler struct {
	services *services.Services
	job      *jobs.Job
}

func NewHandler(services *services.Services, job *jobs.Job) *Handler {
	return &Handler{
		services: services,
		job:      job,
	}
}

func (h *Handler) InitializeRoutes(srv *fiber.App) error {
	srv.Use(
		recover.New(),
		//MiddlewareHelloWorld(),
		//basicauth.New(
		//	basicauth.Config{
		//		Next:            nil,
		//		Users:           nil,
		//		Realm:           "Restricted",
		//		Authorizer:      nil,
		//		Unauthorized:    nil,
		//		ContextUsername: "",
		//		ContextPassword: "",
		//	},
		//),

	)
	h.AddRoutes(srv)
	return nil
}

func (h *Handler) AddRoutes(srv *fiber.App) {
	v1 := srv.Group("/api/v1", middles.SetContextHolder(h.services))
	user := v1.Group("/user")
	{
		user.Post("/estimate/:userID", middles.AuthorizationMiddleWare(h.services), h.CtlEstimateUser)
		user.Post("/verify", middles.AuthorizationMiddleWare(h.services), h.CtlVerifyUser)
	}

	profile := v1.Group("/profile")
	{
		profile.Get("/:id", h.CtlGetUser)
		profile.Get("", middles.AuthorizationMiddleWare(h.services), h.CtlGetCurrentUser)
		profile.Put("/edit", middles.AuthorizationMiddleWare(h.services), h.CtlUpdateUser)
	}

	service := v1.Group("/service")
	{
		service.Get("", h.CtlGetAllServiceProvider)
		service.Get("/:id/detail", h.CtlGetServiceDetail)
		service.Post("/:id", middles.AuthorizationMiddleWare(h.services), h.CtlCreateService)
		service.Delete("/:id", middles.AuthorizationMiddleWare(h.services), h.CtlDeleteService)
	}

	favorite := service.Group("/favorite")
	{
		favorite.Get("", middles.AuthorizationMiddleWare(h.services), h.CtlGetFavoriteServiceProvider)
		favorite.Post("/:serviceID/add", middles.AuthorizationMiddleWare(h.services), h.CtlAddToFavorite)
		favorite.Delete("/:serviceID/remove", middles.AuthorizationMiddleWare(h.services), h.CtlRemoveFromFavorite)
	}

	image := v1.Group("/image")
	{
		image.Get("/:fileName", h.CtlGetImage)
		image.Post("/new", h.CtlCreateImage)
		image.Delete("/:fileName", middles.AuthorizationMiddleWare(h.services), h.CtlDeleteImage)
	}

	pet := user.Group("/pet")
	{
		user.Get("/:id/pet", h.CtlGetUserPets)

		pet.Get("/all", h.CtlGetAllPets)
		pet.Get("/:id", h.CtlGetPetByID)

		pet.Post("/:id/attachment", middles.AuthorizationMiddleWare(h.services), h.CtlCreateAttachments)
		pet.Post("/:id", middles.AuthorizationMiddleWare(h.services), h.CtlCreateOrUpdatePet)
		pet.Delete("/:id/delete", middles.AuthorizationMiddleWare(h.services), h.CtlDeletePet)
	}

	attachment := pet.Group("attachment")
	{

		attachment.Get("/:id", h.CtlGetAttachment)
		attachment.Delete("/:id", middles.AuthorizationMiddleWare(h.services), h.CtlDeleteAttachment)
	}

	countries := v1.Group("/countries")
	{
		countries.Get("", h.CtlGetCountries)
	}

	job := v1.Group("/job")
	{
		job.Post("/:code/start", h.CtlStartJob)
	}
}
