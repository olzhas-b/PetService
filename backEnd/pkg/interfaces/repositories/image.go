package repositories

import (
	"context"
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
)

type IImageRepository interface {
	GetImageByID(id int64) (image models.Image, err error)
	SaveImage(image *[]models.Image) (err error)
	DeleteImageByID(ctx context.Context, id int64) (err error)
	UpdateOrSaveImage(ctx context.Context, image models.Image) (result models.Image, err error)
}
