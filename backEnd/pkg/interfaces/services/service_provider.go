package services

import (
	"context"
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"github.com/olzhas-b/PetService/backEnd/pkg/models/filter"
)

type IServiceProviderService interface {
	ServiceGetAllServices(ctx context.Context, filter filter.ServiceProviderFilter) (sProviders []models.Service, err error)
	ServiceCreateService(ctx context.Context, service models.Service) (result models.Service, err error)
}
