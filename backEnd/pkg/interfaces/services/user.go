package services

import (
	"context"
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"github.com/olzhas-b/PetService/backEnd/pkg/models/filter"
	"mime/multipart"
)

type IUserService interface {
	ServiceGetUserByID(ID int64) (user models.User, err error)
	ServiceCreateUser(user models.User) (models.User, error)
	ServiceGetAllUsers(f *filter.User) ([]models.User, error)
	ServiceUpdateUser(ctx context.Context, user models.User, file *multipart.FileHeader) (models.User, error)
}
