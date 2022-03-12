package postgres

import (
	"context"
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"gorm.io/gorm"
)

type PetRepository struct {
	DB *gorm.DB
}

func NewPetRepository(DB *gorm.DB) *PetRepository {
	return &PetRepository{DB: DB}
}

func (repo *PetRepository) GetAllPets(ctx context.Context) (pets models.PetList, err error) {
	err = repo.DB.Model(models.Pet{}).
		Preload("Image", func(DB *gorm.DB) *gorm.DB {
			return DB.Omit("content")
		}).
		Find(&pets).
		Error
	return
}

func (repo *PetRepository) GetPetsByUserID(ctx context.Context, id int64) (pets models.PetList, err error) {
	defer func() {
		if err != nil {

		}
	}()

	err = repo.DB.Model(&models.Pet{}).
		Where("user_id = ?", id).
		Preload("Image", func(DB *gorm.DB) *gorm.DB {
			return DB.Omit("content")
		}).
		Find(&pets).
		Error

	return
}

func (repo *PetRepository) CreateOrUpdatePet(ctx context.Context, pet models.Pet) (result models.Pet, err error) {
	defer func() {
		if err != nil {

		}
	}()

	err = repo.DB.
		Omit("status", "image_id").
		Save(&pet).
		Error

	return pet, err
}

func (repo *PetRepository) UpdatePet(ctx context.Context, pet models.Pet, userID int64) (result models.Pet, err error) {
	defer func() {
		if err != nil {

		}
	}()

	err = repo.DB.Model(&models.Pet{}).
		Omit("id", "user_id", "image_id").
		Where("user_id = ?", userID).
		Where("id = ?", pet.ID).
		Save(&pet).
		Error

	return pet, err
}

func (repo *PetRepository) DeletePetByID(ctx context.Context, userID, petID int64) (err error) {
	defer func() {
		if err != nil {

		}
	}()

	err = repo.DB.
		Where("user_id = ?", userID).
		Where("id = ?", petID).
		Delete(&models.Pet{}).
		Error

	return
}

func (repo *PetRepository) GetPetByID(ctx context.Context, id int64) (pet models.Pet, err error) {
	defer func() {
		if err != nil {

		}
	}()

	err = repo.DB.Model(models.Pet{}).
		Where("id = ?", id).
		Take(&pet).
		Error

	return
}

func (repo *PetRepository) GetPetImageID(ctx context.Context, ID int64) (imageID int64) {
	repo.DB.Debug().Model(models.Pet{}).
		Where("id = ?", ID).
		Pluck("image_id", &imageID)
	return imageID
}

func (repo *PetRepository) UpdateImageID(ctx context.Context, ID int64, imageID int64) error {
	return repo.DB.Model(&models.Pet{}).
		Where("id = ?", ID).
		Update("image_id", imageID).
		Error
}
