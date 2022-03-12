package postgres

import (
	"context"
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

func (repo *ImageRepository) DeleteImageByID(ctx context.Context, id int64) (err error) {
	defer func() {
		if err != nil {

		}
	}()

	err = repo.DB.
		Where("id = ?", id).
		Delete(&models.Image{}).
		Error
	return
}

func (repo *ImageRepository) UpdateOrSaveImage(ctx context.Context, image models.Image) (result models.Image, err error) {
	err = repo.DB.Model(models.Image{}).
		Where("id = ?", image.ID).
		Save(&image).
		Error

	return image, err
}
