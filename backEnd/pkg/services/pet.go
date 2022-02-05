package services

import (
	"context"
	"errors"
	"fmt"
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"github.com/olzhas-b/PetService/backEnd/pkg/repositories"
	"github.com/olzhas-b/PetService/backEnd/tools"
	"mime/multipart"
)

type PetService struct {
	repo *repositories.Repositories
}

func NewPetService(repo *repositories.Repositories) *PetService {
	return &PetService{repo: repo}
}

func (srv *PetService) ServiceGetPetsByUserID(ctx context.Context, id int64) (pets models.PetList, err error) {
	return srv.repo.IPetRepository.GetPetsByUserID(ctx, id)
}

func (srv *PetService) ServiceCreatePet(ctx context.Context, pet models.Pet, file *multipart.FileHeader, userID int64) (result models.Pet, err error) {
	pet.UserID = userID

	if file != nil {
		pet.Image = &models.Image{
			Name:        file.Filename,
			ContentType: file.Header.Get("Content-Type"),
			Content:     tools.ReadProperly(file),
		}
	}

	return srv.repo.IPetRepository.CreatePet(ctx, pet)
}

func (srv *PetService) ServiceUpdatePet(ctx context.Context, pet models.Pet, file *multipart.FileHeader, userID int64) (result models.Pet, err error) {

	if pet.ImageID == 0 && file != nil {
		pet.Image = &models.Image{
			Name:        file.Filename,
			ContentType: file.Header.Get("Content-Type"),
			Content:     tools.ReadProperly(file),
		}
	}

	return srv.repo.UpdatePet(ctx, pet, userID)
}

func (srv *PetService) ServiceDeletePet(ctx context.Context, userID int64, petID int64) (err error) {
	if userID == 0 || petID == 0 {
		return errors.New("pet doesn't exit")
	}
	pet, err := srv.repo.IPetRepository.GetPetByID(ctx, petID)
	if err != nil {
		return fmt.Errorf("PetService.ServiceDeletePet.GetPetByID got error: %w", err)
	}

	if err = srv.repo.IPetRepository.DeletePetByID(ctx, userID, petID); err != nil {
		return fmt.Errorf("PetService.ServiceDeletePet.DeletePetByID got error: %w", err)
	}

	if err = srv.repo.IImageRepository.DeleteImageByID(ctx, pet.ImageID); err != nil {
		return fmt.Errorf("PetService.ServiceDeletePet.DeleteImageByID got error: %w", err)
	}
	return
}
