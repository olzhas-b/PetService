package postgres

import (
	"context"
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"github.com/olzhas-b/PetService/backEnd/pkg/models/filter"
	"gorm.io/gorm"
)

type ServiceProviderRepository struct {
	DB *gorm.DB
}

func NewServiceServiceProviderRepository(DB *gorm.DB) *ServiceProviderRepository {
	return &ServiceProviderRepository{DB: DB}
}

func (s *ServiceProviderRepository) GetAllServices(ctx context.Context, filter filter.ServiceProviderFilter) (serviceProvider []models.Service, err error) {
	err = s.DB.Table("service").
		Preload("User").
		Preload("Images").
		Find(&serviceProvider).
		Error

	return
}

func (s *ServiceProviderRepository) preloadServiceImages(ctx context.Context, serviceProvider []models.Service) (err error) {
	//var images []models.Image
	//
	//s.DB.Table("image").
	//	Joins("INNER JOIN service_image on service_image.image_id = image.id").
	//	Joins("INNER JOIN service on service.id = service_image.service_id").
	//	Where("service.id = ?", serviceProvider[i].ID).
	//	Find().
	//	Error
	return
}

func (s *ServiceProviderRepository) CreateService(ctx context.Context, service models.Service) (models.Service, error) {
	err := s.DB.Table("service").
		Omit("is_deleted", "last_activity", "status").
		Create(&service).
		Error
	return service, err
}
