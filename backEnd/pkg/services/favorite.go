package services

import (
	"context"
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"github.com/olzhas-b/PetService/backEnd/pkg/repositories"
	"github.com/olzhas-b/PetService/backEnd/tools/utils"
)

type FavoriteService struct {
	repo *repositories.Repositories
}

func NewFavoriteService(repo *repositories.Repositories) *FavoriteService {
	return &FavoriteService{repo: repo}
}
func (srv *FavoriteService) ServiceGetFavoriteList(ctx context.Context) (favSlice []int64, err error) {
	userID := utils.GetCurrentUserID(ctx)
	return srv.repo.IFavoriteRepository.GetFavoriteList(ctx, userID)
}

func (srv *FavoriteService) ServiceAddToFavorite(ctx context.Context, favorite models.Favorite) (err error) {
	favorite.UserID = utils.GetCurrentUserID(ctx)
	return srv.repo.IFavoriteRepository.AddToFavorite(ctx, favorite)
}

func (srv *FavoriteService) ServiceRemoveFromFavorite(ctx context.Context, favorite models.Favorite) (err error) {
	favorite.UserID = utils.GetCurrentUserID(ctx)
	return srv.repo.IFavoriteRepository.RemoveFromFavorite(ctx, favorite)
}
