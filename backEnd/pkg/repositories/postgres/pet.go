package postgres

import (
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"gorm.io/gorm"
)

type PetRepository struct {
	DB *gorm.DB
}

func (p PetRepository) GetPetsByUserID(id int64) (pets []models.Pet, err error) {
	//TODO implement me
	panic("implement me")
}

func (p PetRepository) CreatePet(pet models.Pet) (result models.Pet, err error) {
	//TODO implement me
	panic("implement me")
}

func (p PetRepository) UpdatePet(pet models.Pet) (result models.Pet, err error) {
	//TODO implement me
	panic("implement me")
}

func NewPetRepository(DB *gorm.DB) *PetRepository {
	return &PetRepository{DB: DB}
}
