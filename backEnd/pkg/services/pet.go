package services

import (
	"context"
	"errors"
	"fmt"
	"github.com/olzhas-b/PetService/backEnd/consts"
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"github.com/olzhas-b/PetService/backEnd/pkg/repositories"
	"github.com/olzhas-b/PetService/backEnd/tools"
	"github.com/olzhas-b/PetService/backEnd/tools/utils"
	"mime/multipart"
	"time"
)

type PetService struct {
	repo *repositories.Repositories
}

func NewPetService(repo *repositories.Repositories) *PetService {
	return &PetService{repo: repo}
}

func (srv *PetService) CtlGetPetByID(ctx context.Context, id int64) (pets models.Pet, err error) {
	return srv.repo.GetPetByID(ctx, id)
}

func (srv *PetService) ServiceGetPetsByUserID(ctx context.Context, id int64) (pets models.PetList, err error) {
	return srv.repo.IPetRepository.GetPetsByUserID(ctx, id)
}

func (srv *PetService) ServiceGetPetByID(ctx context.Context, id int64) (pets models.Pet, err error) {
	return srv.repo.IPetRepository.GetPetByID(ctx, id)
}

func (srv *PetService) ServiceCreateOrUpdatePet(ctx context.Context, pet models.Pet, file *multipart.FileHeader, requestType string) (result models.Pet, err error) {
	var image models.Image
	if requestType == consts.New {
		pet.ID = 0
	} else {
		pet.ID = tools.StrToInt64(requestType)
	}
	pet.UserID = utils.GetCurrentUserID(ctx)

	// save or create pet
	result, err = srv.repo.IPetRepository.CreateOrUpdatePet(ctx, pet)
	if err != nil {
		return
	}

	// get image from customer or default image  from local storage
	if file != nil {
		image = models.Image{
			Name:        file.Filename,
			ContentType: file.Header.Get("Content-Type"),
			Content:     tools.ReadProperly(file),
		}
	} else {
		image = utils.GetLocalImage(consts.ServiceAvatarPath)
	}
	image.ID = srv.repo.IPetRepository.GetPetImageID(ctx, result.ID)

	// save image to database
	image, err = srv.repo.IImageRepository.UpdateOrSaveImage(ctx, image)
	if err != nil {
		return
	}
	result.Image = &image

	// update pets image_id
	err = srv.repo.IPetRepository.UpdateImageID(ctx, result.ID, image.ID)
	return
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

func (srv *PetService) ServiceGetAllPets(ctx context.Context) (pets models.PetList, err error) {
	return srv.repo.IPetRepository.GetAllPets(ctx)
}

func (srv *PetService) ServiceGetPetsWhichExpiredCertificate(ctx context.Context) (pets []models.Pet, err error) {
	now := time.Now()
	if pets, err = srv.repo.IPetRepository.GetPetsWhichExpiredCertificate(ctx, now); err != nil {
		return nil, fmt.Errorf("PetService.ServiceGetPetsWhichExpiredCertificate got error: %w", err)
	}
	return
}
