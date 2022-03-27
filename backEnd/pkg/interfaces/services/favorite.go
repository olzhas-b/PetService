package services

import (
	"context"
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
)

type IFavoriteService interface {
	ServiceGetFavoriteList(ctx context.Context) (favSlice []int64, err error)
	ServiceAddToFavorite(ctx context.Context, favorite models.Favorite) (err error)
	ServiceRemoveFromFavorite(ctx context.Context, favorite models.Favorite) (err error)
}
