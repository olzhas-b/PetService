package repositories

import (
	"context"
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"time"
)

type IPetRepository interface {
	GetPetsByUserID(ctx context.Context, id int64) (pets models.PetList, err error)
	GetPetByID(ctx context.Context, id int64) (pet models.Pet, err error)
	CreateOrUpdatePet(ctx context.Context, pet models.Pet) (result models.Pet, err error)
	UpdatePet(ctx context.Context, pet models.Pet, userID int64) (result models.Pet, err error)
	DeletePetByID(ctx context.Context, userID int64, petID int64) (err error)
	GetAllPets(ctx context.Context) (pets models.PetList, err error)
	GetPetImageID(ctx context.Context, ID int64) (imageID int64)
	UpdateImageID(ctx context.Context, ID int64, imageID int64) error
	GetPetsWhichExpiredCertificate(ctx context.Context, expiredTime time.Time) (result []models.Pet, err error)
}
