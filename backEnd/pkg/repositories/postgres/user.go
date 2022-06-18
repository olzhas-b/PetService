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
	err := repo.DB.Debug().Table("\"user\"").
		Omit(selectedColumns...).
		Save(&user).
		Error

	return user, err
}

//err := repo.DB.Debug().Table("service").
//Omit("is_deleted", "last_activity", "status", "is_favorite").
//Save(&service).
//Error
//return service, err

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

func (repo *UserRepository) DeleteUserImageByUserID(ctx context.Context, userID int64) (err error) {
	return nil
	query := "DO\n$$DECLARE\n    user_image_id BIGINT;\nBEGIN\n    SELECT image_id INTO user_image_id FROM \"user\" WHERE id = (?);\n\n    UPDATE \"user\" SET image_id = NULL WHERE id = (?);\n\n    DELETE FROM image WHERE id = user_image_id;\nEND$$;"

	return repo.DB.Exec(query, userID, userID).Error

	//return repo.DB.Exec(`
	//		DECLARE
	//		    user_image_id BIGINT;
	//		BEGIN
	//		    SELECT image_id INTO user_image_id FROM "user" WHERE id = (?);
	//		    UPDATE "user" SET image_id = NULL WHERE id = (?);
	//		    DELETE FROM image WHERE id = user_image_id;
	//		END;`, userID, userID).Error
}

func (repo *UserRepository) VerifyUser(ctx context.Context, id int64) error {
	return repo.DB.Model(&models.User{}).
		Where("id = ?", id).
		Update("is_verified", true).
		Error
}
