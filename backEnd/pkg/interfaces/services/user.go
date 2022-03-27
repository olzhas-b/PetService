package services

import (
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"github.com/olzhas-b/PetService/backEnd/pkg/models/filter"
)

type IUserService interface {
	ServiceGetUserByID(ID int64) (user models.User, err error)
	ServiceCreateUser(user models.User) (models.User, error)
	ServiceGetAllUsers(f *filter.User) ([]models.User, error)
}
