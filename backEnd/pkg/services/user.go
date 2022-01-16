package services

import (
	"fmt"
	validator "github.com/go-playground/validator/v10"
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"github.com/olzhas-b/PetService/backEnd/pkg/models/filter"
	"github.com/olzhas-b/PetService/backEnd/pkg/repositories"
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

func (s *UserService) UpdateUser(user models.User) (result models.User, err error) {
	return s.repo.IUserRepository.UpdateUser(user, []string{"todo"})
}

func (s *UserService) ServiceGetAllUsers(f *filter.User) (result []models.User, err error) {
	result, err = s.repo.GetAllUsers(f)
	if err != nil {
		return result, fmt.Errorf("ServiceGetAllUsers : %w", err)
	}
	return
}
