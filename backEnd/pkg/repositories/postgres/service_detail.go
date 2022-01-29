package postgres

import (
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"gorm.io/gorm"
)

type ServiceDetailRepository struct {
	DB *gorm.DB
}

func NewServiceDetailRepository(DB *gorm.DB) *ServiceDetailRepository {
	return &ServiceDetailRepository{DB: DB}
}

func (repo *ServiceDetailRepository) GetServiceDetail(id int64) (sDetail models.ServiceDetail, err error) {
	err = repo.DB.Table("service").
		Select([]string{
			"id",
			"longitude",
			"latitude",
			"last_activity",
			"description",
			"acceptable_pets",
			"acceptable_size",
			"status",
		}).
		Preload("AdditionalProperties").
		Where("id = (?)", id).
		Find(&sDetail).
		Error

	return
}
