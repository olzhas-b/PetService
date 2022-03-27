package repositories

import (
	"context"
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
)

type IRatingRepository interface {
	Estimate(ctx context.Context, rating models.Rating) (err error)
}
