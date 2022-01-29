package services

import (
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"github.com/olzhas-b/PetService/backEnd/pkg/repositories"
)

type PetService struct {
	repo *repositories.Repositories
}

func (p PetService) ServiceGetPetsByUserID(id int64) (pets []models.Pet, err error) {
	//TODO implement me
	panic("implement me")
}

func (p PetService) ServiceCreatePet(pet models.Pet) (result models.Pet, err error) {
	//TODO implement me
	panic("implement me")
}

func (p PetService) ServiceUpdatePet(pet models.Pet) (result models.Pet, err error) {
	//TODO implement me
	panic("implement me")
}

func NewPetService(repo *repositories.Repositories) *PetService {
	return &PetService{repo: repo}
}
