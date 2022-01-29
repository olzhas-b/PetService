package services

import "github.com/olzhas-b/PetService/backEnd/pkg/models"

type IImageService interface {
	ServiceGetImageByFileName(name string) (image models.Image, err error)
	ServiceSaveImage(image models.ImageToSave) (err error)
}
