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

	user := v1.Group("/user", middles.AuthorizationMiddleWare(h.services))
	{

		user.Post("/sign-up", h.CtlCreateUser) // create
		user.Post("/sign-out", nil)
		user.Get("", h.CtlGetAllUsers)
		user.Post("/edit", nil) // TODO update
		//user.Get("/:id", h.CtlGetUser)
	}

	service := v1.Group("/service", middles.AuthorizationMiddleWare(h.services))
	{
		service.Get("", h.CtlGetAllServiceProvider)
		service.Post("", h.CtlCreateService)
	}

	image := v1.Group("/image")
	{
		image.Get("/:id", h.CtlGetImage)
		image.Post("/new", h.CtlCreateImage)
		image.Post("/edit", h.CtlUpdateImage)
	}
}
