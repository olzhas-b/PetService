package handler

import (
	"github.com/gofiber/fiber/v2"
	"github.com/gofiber/fiber/v2/middleware/recover"
	"github.com/olzhas-b/PetService/backEnd/pkg/services"
	"github.com/olzhas-b/PetService/backEnd/pkg/transport/restful/middles"
)

type Handler struct {
	services *services.Services
}

func NewHandler(services *services.Services) *Handler {
	return &Handler{services: services}
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
		//user.Post("/sign-up", h.CtlCreateUser) // create
		//user.Post("/sign-out", nil)
		//user.Get("", h.CtlGetAllUsers)
		//user.Post("/edit", nil) // TODO update
		//user.Get("/:id", h.CtlGetUser)
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
		favorite.Get("", h.CtlGetFavoriteServiceProvider)
		favorite.Post("/:serviceID/add", h.CtlAddToFavorite)
		favorite.Delete("/:serviceID/remove", h.CtlRemoveFromFavorite)
	}

	image := v1.Group("/image")
	{
		image.Get("/:fileName", h.CtlGetImage)
		image.Post("/new", h.CtlCreateImage)
		image.Delete("/:fileName", middles.AuthorizationMiddleWare(h.services), h.CtlDeleteImage)
	}

	pet := user.Group("/pet")
	{
		user.Get("/:id/pet", h.CtlGetPet)

		pet.Get("/all", h.CtlGetAllPets)
		pet.Post("/:id", middles.AuthorizationMiddleWare(h.services), h.CtlCreateOrUpdatePet)
		pet.Delete("/:id/delete", middles.AuthorizationMiddleWare(h.services), h.CtlDeletePet)
		//pet.Put("/:id/edit", middles.AuthorizationMiddleWare(h.services), h.CtlUpdatePet)
	}

	countries := v1.Group("/countries")
	{
		countries.Get("", h.CtlGetCountries)
	}
}
