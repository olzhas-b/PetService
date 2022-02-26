package services

import (
	"context"
	"fmt"
	validator "github.com/go-playground/validator/v10"
	"github.com/olzhas-b/PetService/backEnd/consts"
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"github.com/olzhas-b/PetService/backEnd/pkg/models/filter"
	"github.com/olzhas-b/PetService/backEnd/pkg/repositories"
	"github.com/olzhas-b/PetService/backEnd/tools"
	"github.com/olzhas-b/PetService/backEnd/tools/utils"
	"mime/multipart"
	"time"
)

type UserService struct {
	repo *repositories.Repositories
}

func NewUserService(repo *repositories.Repositories) *UserService {
	return &UserService{
		repo: repo,
	}
}

func (s *UserService) ServiceGetUserByID(userID int64) (user models.User, err error) {
	return s.repo.GetUserByID(userID)
}

func (s *UserService) ServiceGetUserByParams(id int64, login, phone string) (user models.User, err error) {
	return s.repo.GetUserByParams(id, login, phone)
}

func (s *UserService) ServiceCreateUser(user models.User) (result models.User, err error) {
	validate := validator.New()
	if err := validate.Struct(user); err != nil {
		return models.User{}, fmt.Errorf("Validate.Struct: %w", err)
	}
	user.FillDefaultValue()
	result, err = s.repo.CreateUser(user, []string{
		"login",
		"username",
		"phone",
		"password",
		"first_name",
		"last_name",
		"full_name",
		"city",
		"country",
		"location",
		"description",
		"created",
		"updated",
	})
	return
}

func (srv *UserService) ServiceGetAllUsers(f *filter.User) (result []models.User, err error) {
	result, err = srv.repo.GetAllUsers(f)
	if err != nil {
		return result, fmt.Errorf("ServiceGetAllUsers : %w", err)
	}
	return
}

func (srv *UserService) ServiceUpdateUser(ctx context.Context, user models.User, file *multipart.FileHeader) (result models.User, err error) {
	user.ID = utils.GetCurrentUserID(ctx)
	user.Updated = time.Now()
	//imageID := s.repo.IUserRepository.GetImageIdByUserID(user.ID)
	//if imageID != 0 {
	//	user.ImageID = &imageID
	//}
	if err = srv.repo.IUserRepository.DeleteUserImageByUserID(ctx, user.ID); err != nil {
		return
	}
	if file != nil {
		user.Image = &models.Image{
			Name:        file.Filename,
			ContentType: file.Header.Get("Content-Type"),
			Content:     tools.ReadProperly(file),
		}
	} else {
		image := utils.GetLocalImage(consts.ProfileAvatarPath)
		user.Image = &image
	}

	omitColumns := user.GetOmitColumns()
	return srv.repo.IUserRepository.UpdateUser(ctx, user, omitColumns)
}
