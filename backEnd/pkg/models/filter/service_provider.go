package filter

import (
	"github.com/gofiber/fiber/v2"
	"github.com/olzhas-b/PetService/backEnd/tools"
	"time"
)

type ServiceProviderFilter struct {
	Page        int
	Size        int
	UserID      int64
	PetSize     int64
	MinPrice    int64
	MaxPrice    int64
	Sort        string
	Order       string
	SearchText  string
	City        string
	Country     string
	PetType     string
	ServiceType int8
	DateFrom    time.Time
	DateTo      time.Time
}

func (f *ServiceProviderFilter) Fill(ctx *fiber.Ctx) {
	ctx.QueryParser(f)
	f.DateFrom = tools.StartOfDay(tools.StrToTime(ctx.Query("dateFrom")))
	f.DateTo = tools.StrToTime(ctx.Query("dateTo"))
}
