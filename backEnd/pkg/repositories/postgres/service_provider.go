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

//Login         string    `gorm:"login" json:"login"`
//Username      string    `gorm:"username" json:"username"`
//Phone         string    `gorm:"phone" json:"phone"`
//Password      string    `gorm:"password" json:"password"`
//FirstName     string    `gorm:"first_name" json:"firstName"`
//LastName      string    `gorm:"last_name" json:"lastName"`
//FullName      string    `gorm:"full_name" json:"fullName"`
//City          string    `gorm:"city" json:"city"`
//Country       string    `gorm:"country" json:"country"`
//Location      string    `gorm:"location" json:"location"`
//Description   string    `gorm:"description" json:"description"`
//CountRating   int64     `gorm:"count_rating" json:"countRating"`
//AverageRating float64   `gorm:"average_rating" json:"averageRating"`
//Created       time.Time `gorm:"created" json:"created"`
//Updated       time.Time `gorm:"updated" json:"updated"`
//Status        int64     `gorm:"status" json:"status"`
//IsDeleted     bool      `gorm:"is_deleted" json:"isDeleted"`
func (s *ServiceProviderRepository) GetAllServices(ctx context.Context, filter filter.ServiceProviderFilter) (listService models.ListService, err error) {
	err = s.DB.Model(&models.Service{}).
		Preload("User", func(db *gorm.DB) *gorm.DB {
			return db.Select([]string{
				"id",
				"full_name",
				"username",
				"phone",
				"city",
				"country",
				"location",
				"description",
				"average_rating",
				"created",
				"updated",
				"status",
			})
		}).
		Preload("Images", func(db *gorm.DB) *gorm.DB {
			return db.Select([]string{
				"id",
				"name",
				"content_type",
				"status",
			})
		}).
		Find(&listService).
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
