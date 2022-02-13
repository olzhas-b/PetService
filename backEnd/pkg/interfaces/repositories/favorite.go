package repositories

import (
	"context"
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
)

type IFavoriteRepository interface {
	GetFavoriteList(ctx context.Context, userID int64) (favSlice []int64, err error)
	AddToFavorite(ctx context.Context, favorite models.Favorite) (err error)
	RemoveFromFavorite(ctx context.Context, favorite models.Favorite) (err error)
}
