package postgres

import (
	"context"
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"github.com/olzhas-b/PetService/backEnd/pkg/models/filter"
	"gorm.io/gorm"
)

type UserRepository struct {
	DB *gorm.DB
}

func NewUserRepository(DB *gorm.DB) *UserRepository {
	return &UserRepository{DB: DB}
}

func (repo *UserRepository) GetUserByID(ID int64) (user models.User, err error) {
	err = repo.DB.Model(models.User{}).
		Omit("password").
		Where("id = ?", ID).
		Preload("Image").
		First(&user).
		Error
	return
}

func (repo *UserRepository) GetAllUsers(filter *filter.User) (users []models.User, err error) {
	err = repo.DB.Model(models.User{}).
		Find(&users).
		Error
	return
}

func (repo *UserRepository) UpdateUser(ctx context.Context, user models.User, selectedColumns []string) (models.User, error) {
	err := repo.DB.Model(models.User{}).
		Omit(selectedColumns...).
		Where("id = ?", user.ID).
		Save(&user).
		Error

	return user, err
}

func (repo *UserRepository) CreateUser(user models.User, selectedColumns []string) (models.User, error) {
	err := repo.DB.Model(models.User{}).
		Select(selectedColumns).
		Create(&user).
		Error

	return user, err
}

func (repo *UserRepository) GetUserByParams(id int64, login, phone string) (result models.User, err error) {
	err = repo.DB.Model(models.User{}).
		Where("id = (?) OR login = (?) OR phone = (?)", id, login, phone).
		First(&result).
		Error
	return
}

func (repo *UserRepository) GetImageIdByUserID(userID int64) (ID int64) {
	repo.DB.Table("user").
		Select("image_id").
		Where("id = ?", userID).
		Take(&ID)
	return ID
}
