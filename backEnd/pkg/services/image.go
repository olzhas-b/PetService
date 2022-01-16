package services

import (
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"github.com/olzhas-b/PetService/backEnd/pkg/repositories"
)

type ImageService struct {
	repo *repositories.Repositories
}

func (i ImageService) ServiceGetImage(id int64) (image models.Image, err error) {
	//TODO implement me
	return
}

func NewImageService(repo *repositories.Repositories) *ImageService {
	return &ImageService{repo: repo}
}
