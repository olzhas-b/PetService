package repositories

import (
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"github.com/olzhas-b/PetService/backEnd/pkg/models/filter"
)

type IUserRepository interface {
	GetUserByID(userID int64) (user models.User, err error)
	GetAllUsers(f *filter.User) ([]models.User, error)
	UpdateUser(user models.User, selectedColumns []string) (models.User, error)
	CreateUser(user models.User, selectedColumns []string) (models.User, error)
	GetUserByParams(id int64, login, phone string) (models.User, error)
}
