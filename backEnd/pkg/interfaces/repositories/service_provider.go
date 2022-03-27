package repositories

import (
	"context"
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"github.com/olzhas-b/PetService/backEnd/pkg/models/filter"
)

type IServiceProviderRepository interface {
	GetAllServices(ctx context.Context, filter filter.ServiceProviderFilter) (sProviders []models.Service, err error)
	CreateService(ctx context.Context, service models.Service) (result models.Service, err error)
}
