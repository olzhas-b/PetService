package services

import (
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"mime/multipart"
)

type IImageService interface {
	ServiceGetImageByFileName(name string) (image models.Image, err error)
	ServiceSaveImage(files []*multipart.FileHeader) (err error)
}
