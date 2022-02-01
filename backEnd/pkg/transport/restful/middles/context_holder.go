package middles

import (
	"github.com/gofiber/fiber/v2"
	"github.com/olzhas-b/PetService/backEnd/consts"
	"github.com/olzhas-b/PetService/backEnd/pkg/services"
	"github.com/olzhas-b/PetService/backEnd/tools"
	"strconv"
)

func SetContextHolder(service *services.Services) fiber.Handler {
	return func(c *fiber.Ctx) error {
		token := tools.GetToken(c)
		if len(token) < 10 {
			return c.Next()
		}

		claims, err := service.IAuthorizationService.ParseToken(token, true)

		if err == nil {
			c.Locals(consts.UserType, strconv.FormatInt(claims.UserType, 10))
			c.Locals(consts.UserID, strconv.FormatInt(claims.ID, 10))
		}
		return c.Next()
	}
}
