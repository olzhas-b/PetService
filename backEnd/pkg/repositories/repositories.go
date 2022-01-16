package repositories

import (
	"github.com/olzhas-b/PetService/backEnd/pkg/interfaces/repositories"
	"github.com/olzhas-b/PetService/backEnd/pkg/repositories/postgres"
	"gorm.io/gorm"
)

type Repositories struct {
	repositories.IUserRepository
	repositories.IServiceProviderRepository
	repositories.IImageRepository
}

func NewRepositories(DB *gorm.DB) *Repositories {
	return &Repositories{
		IUserRepository:            postgres.NewUserRepository(DB),
		IServiceProviderRepository: postgres.NewServiceServiceProviderRepository(DB),
	}
}
