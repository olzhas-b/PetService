package services

import (
	"context"
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"mime/multipart"
)

type IPetService interface {
	ServiceGetPetsByUserID(ctx context.Context, id int64) (pets models.PetList, err error)
	ServiceCreateOrUpdatePet(ctx context.Context, pet models.Pet, files *multipart.FileHeader, requestType string) (result models.Pet, err error)
	ServiceUpdatePet(ctx context.Context, pet models.Pet, files *multipart.FileHeader, userID int64) (result models.Pet, err error)
	ServiceDeletePet(ctx context.Context, userID int64, petID int64) (err error)
	ServiceGetAllPets(ctx context.Context) (pets models.PetList, err error)
}
