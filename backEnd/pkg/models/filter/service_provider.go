package filter

import "github.com/gofiber/fiber/v2"

type ServiceProviderFilter struct {
	Page       int
	Size       int
	Sort       string
	Order      string
	SearchText string
}

func (f *ServiceProviderFilter) Fill(ctx *fiber.Ctx) {

}
