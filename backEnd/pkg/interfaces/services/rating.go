package services

import (
	"context"
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
)

type IRatingService interface {
	ServiceEstimate(ctx context.Context, rating models.Rating) (err error)
}
