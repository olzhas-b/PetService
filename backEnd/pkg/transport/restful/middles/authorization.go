package middles

import (
	"github.com/gofiber/fiber/v2"
	"github.com/olzhas-b/PetService/backEnd/pkg/services"
	"github.com/olzhas-b/PetService/backEnd/tools"
	"net/http"
)

func AuthorizationMiddleWare(s *services.Services) fiber.Handler {
	return func(c *fiber.Ctx) error {
		token := tools.GetToken(c)
		if err := s.IAuthorizationService.CheckToken(token, true); err != nil {
			c.Status(http.StatusUnauthorized)
			return nil
		}
		return c.Next()
	}
}
