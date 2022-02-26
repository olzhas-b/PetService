package services

import (
	"context"
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"github.com/olzhas-b/PetService/backEnd/pkg/models/filter"
	"mime/multipart"
)

type IServiceProviderService interface {
	ServiceGetAllServices(ctx context.Context, filter filter.ServiceProviderFilter) (listService models.ListService, total int64, err error)
	ServiceCreateService(ctx context.Context, service models.Service, images []*multipart.FileHeader, requestType string) (result models.Service, err error)
	ServiceGetFavoriteServices(ctx context.Context) (listService models.ListService, err error)
}
