package services

import (
	"context"
	"errors"
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"github.com/olzhas-b/PetService/backEnd/pkg/repositories"
	"time"
)

type RatingService struct {
	repo *repositories.Repositories
}

func NewRatingService(repo *repositories.Repositories) *RatingService {
	return &RatingService{repo: repo}
}

func (srv *RatingService) ServiceEstimate(ctx context.Context, rating models.Rating) (err error) {
	if rating.Score < 1 && rating.Score > 5 {
		return errors.New("incorrect score")
	}
	rating.Created = time.Now()
	return srv.repo.Estimate(ctx, rating)
}
