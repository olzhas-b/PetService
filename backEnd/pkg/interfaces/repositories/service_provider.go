package repositories

import (
	"context"
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"github.com/olzhas-b/PetService/backEnd/pkg/models/filter"
)

type IServiceProviderRepository interface {
	GetAllServices(ctx context.Context, userID int64, filter filter.ServiceProviderFilter) (listService models.ListService, err error)
	CreateService(ctx context.Context, service models.Service) (result models.Service, err error)
	GetFavoriteServices(ctx context.Context, userID int64) (listService models.ListService, err error)
}
