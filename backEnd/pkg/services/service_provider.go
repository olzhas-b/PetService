package services

import (
	"context"
	"fmt"
	"github.com/olzhas-b/PetService/backEnd/consts"
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"github.com/olzhas-b/PetService/backEnd/pkg/models/filter"
	repo "github.com/olzhas-b/PetService/backEnd/pkg/repositories"
	"github.com/olzhas-b/PetService/backEnd/tools"
	"github.com/olzhas-b/PetService/backEnd/tools/utils"
	"mime/multipart"
)

type ServiceProvider struct {
	repo *repo.Repositories
}

func NewServiceProvider(repo *repo.Repositories) *ServiceProvider {
	return &ServiceProvider{repo: repo}
}

func (srv *ServiceProvider) ServiceGetAllServices(ctx context.Context, filter filter.ServiceProviderFilter) (listService models.ListService, total int64, err error) {
	var favoriteList []int64
	userID := utils.GetCurrentUserID(ctx)
	listService, total, err = srv.repo.IServiceProviderRepository.GetAllServices(ctx, userID, filter)
	if err != nil {
		return
	}

	if userID != 0 {
		favoriteList, err = srv.repo.IFavoriteRepository.GetFavoriteList(ctx, userID)
		if err != nil {
			return
		}
	}

	checkFavorite := tools.SliceToMapInt64(favoriteList)
	for i := range listService {
		if checkFavorite[listService[i].ID] {
			listService[i].IsFavorite = true
		}
	}
	return
}

func (srv *ServiceProvider) ServiceCreateService(ctx context.Context, service models.Service, files []*multipart.FileHeader, requestType string) (result models.Service, err error) {
	if requestType == consts.New {
		service.ID = 0
	} else {
		service.ID = tools.StrToInt64(requestType)
		if err = srv.repo.IServiceProviderRepository.DeleteImagesByServiceID(ctx, service.ID); err != nil {
			return
		}
	}

	service.LastActivity = tools.GetCurrentTimePtr()
	service.UserID = utils.GetCurrentUserID(ctx)
	for _, file := range files {
		service.Images = append(service.Images, models.Image{
			Name:        file.Filename,
			ContentType: file.Header.Get("Content-Type"),
			Content:     tools.ReadProperly(file),
		},
		)
	}
	if len(service.Images) == 0 {
		service.Images = append(service.Images, utils.GetLocalImage(consts.ServiceAvatarPath))
	}

	result, err = srv.repo.IServiceProviderRepository.CreateService(ctx, service)
	if err != nil {
		return models.Service{}, fmt.Errorf("ServiceProvider.ServiceCreateService got error: %w", err)
	}
	return
}

func (srv *ServiceProvider) ServiceGetFavoriteServices(ctx context.Context) (models.ListService, error) {
	userID := utils.GetCurrentUserID(ctx)
	return srv.repo.GetFavoriteServices(ctx, userID)
}
