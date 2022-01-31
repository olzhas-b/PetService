package postgres

import (
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"gorm.io/gorm"
)

type ImageRepository struct {
	DB *gorm.DB
}

func NewImageRepository(DB *gorm.DB) *ImageRepository {
	return &ImageRepository{DB: DB}
}

func (repo *ImageRepository) SaveImage(images *[]models.Image) (err error) {
	return repo.DB.Model(models.Image{}).
		Create(&images).
		Error
}

func (repo *ImageRepository) GetImageByID(id int64) (image models.Image, err error) {
	//TODO implement me
	err = repo.DB.Model(&models.Image{}).
		Where("id = (?)", id).
		Find(&image).
		Error
	return
}
