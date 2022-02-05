package postgres

import (
	"context"
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"gorm.io/gorm"
)

type RatingRepository struct {
	DB *gorm.DB
}

func NewRatingRepository(DB *gorm.DB) *RatingRepository {
	return &RatingRepository{DB: DB}
}

func (repo *RatingRepository) Estimate(ctx context.Context, rating models.Rating) (err error) {
	defer func() {
		if err != nil {

		}
	}()

	err = repo.DB.
		Create(&rating).
		Error
	return
}
