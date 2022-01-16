package services

import (
	interfaces "github.com/olzhas-b/PetService/backEnd/pkg/interfaces/services"
	repo "github.com/olzhas-b/PetService/backEnd/pkg/repositories"
)

type Services struct {
	interfaces.IUserService
	interfaces.IImageService
	interfaces.IServiceProviderService
}

func NewServices(repo *repo.Repositories) *Services {
	return &Services{
		IUserService:            NewUserService(repo),
		IImageService:           NewImageService(repo),
		IServiceProviderService: NewServiceProvider(repo),
	}
}
