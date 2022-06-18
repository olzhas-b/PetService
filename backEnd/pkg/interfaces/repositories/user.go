package repositories

import (
	"context"
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"github.com/olzhas-b/PetService/backEnd/pkg/models/filter"
)

type IUserRepository interface {
	GetUserByID(userID int64) (user models.User, err error)
	GetAllUsers(f *filter.User) ([]models.User, error)
	GetUserByParams(id int64, login, phone string) (models.User, error)
	GetImageIdByUserID(userID int64) (ID int64)
	CreateUser(user models.User, selectedColumns []string) (models.User, error)
	UpdateUser(ctx context.Context, user models.User, selectedColumns []string) (models.User, error)
	VerifyUser(ctx context.Context, id int64) error
	DeleteUserImageByUserID(ctx context.Context, userID int64) (err error)
}
