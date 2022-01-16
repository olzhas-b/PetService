package services

import "github.com/olzhas-b/PetService/backEnd/pkg/models"

type IImageService interface {
	ServiceGetImage(id int64) (models.Image, error)
}
