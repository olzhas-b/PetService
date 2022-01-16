package handler

import (
	"github.com/gofiber/fiber/v2"
	"github.com/gofiber/fiber/v2/middleware/recover"
	"github.com/olzhas-b/PetService/authService/pkg/services"
)

type Handler struct {
	services *services.Services
}

func NewHandler(services *services.Services) *Handler {
	return &Handler{services: services}
}

func MiddlewareHelloWorld() fiber.Handler {
	return func(c *fiber.Ctx) error {
		println("Hello World")
		return c.Next()
	}
}

func (h *Handler) InitializeRoutes(srv *fiber.App) error {
	srv.Use(
		recover.New(),
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

	v1 := srv.Group("/api/v1")

	user := v1.Group("/user")
	{

		user.Post("/sign-in", h.CtlSignIn)
		user.Post("/sign-up", h.CtlCreateUser) // create
		user.Post("/sign-out", h.CtlSignOut)
		user.Post("/update-token", h.CtlUpdateToken)
		user.Post("/edit", nil)               // TODO unnecessary
		user.Get("", h.CtlServiceGetAllUsers) // TODO unnecessary
		//user.Get("/:id", h.CtlGetUser)        // TODO unnecessary
	}

}
