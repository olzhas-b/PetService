package repositories

import "github.com/olzhas-b/PetService/backEnd/pkg/models"

type IPetRepository interface {
	GetPetsByUserID(id int64) (pets []models.Pet, err error)
	CreatePet(pet models.Pet) (result models.Pet, err error)
	UpdatePet(pet models.Pet) (result models.Pet, err error)
}
