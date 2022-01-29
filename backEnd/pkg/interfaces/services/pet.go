package services

import "github.com/olzhas-b/PetService/backEnd/pkg/models"

type IPetService interface {
	ServiceGetPetsByUserID(id int64) (pets []models.Pet, err error)
	ServiceCreatePet(pet models.Pet) (result models.Pet, err error)
	ServiceUpdatePet(pet models.Pet) (result models.Pet, err error)
}
