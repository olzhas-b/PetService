package services

import (
	"context"
	"fmt"
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"github.com/olzhas-b/PetService/backEnd/pkg/models/filter"
	repo "github.com/olzhas-b/PetService/backEnd/pkg/repositories"
	"github.com/olzhas-b/PetService/backEnd/tools"
	"mime/multipart"
	"time"
)

type ServiceProvider struct {
	repo *repo.Repositories
}

func NewServiceProvider(repo *repo.Repositories) *ServiceProvider {
	return &ServiceProvider{repo: repo}
}

func (s *ServiceProvider) ServiceGetAllServices(ctx context.Context, filter filter.ServiceProviderFilter) (listService models.ListService, err error) {
	return s.repo.IServiceProviderRepository.GetAllServices(ctx, filter)
}

func (s *ServiceProvider) ServiceCreateService(ctx context.Context, service models.Service, files []*multipart.FileHeader) (result models.Service, err error) {
	timeNow := time.Now()
	service.LastActivity = &timeNow
	for _, file := range files {
		newImage := models.Image{
			Name:        file.Filename,
			ContentType: file.Header.Get("Content-Type"),
			Content:     tools.ReadProperly(file),
		}
		service.Images = append(service.Images, newImage)
	}
	result, err = s.repo.IServiceProviderRepository.CreateService(ctx, service)
	if err != nil {
		return models.Service{}, fmt.Errorf("ServiceProvider.ServiceCreateService got error: %w", err)
	}
	return
}
