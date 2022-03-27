package postgres

import (
	"context"
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"gorm.io/gorm"
)

type FavoriteRepository struct {
	DB *gorm.DB
}

func NewFavoriteRepository(DB *gorm.DB) *FavoriteRepository {
	return &FavoriteRepository{DB: DB}
}

func (repo *FavoriteRepository) GetFavoriteList(ctx context.Context, userID int64) (favSlice []int64, err error) {
	err = repo.DB.Table("favorite_user_service").
		Where("user_id = ?", userID).
		Find(&favSlice).
		Error
	return
}

func (repo *FavoriteRepository) AddToFavorite(ctx context.Context, favorite models.Favorite) (err error) {
	return repo.DB.Table("favorite_user_service").Create(&favorite).Error
}

func (repo *FavoriteRepository) RemoveFromFavorite(ctx context.Context, favorite models.Favorite) (err error) {
	return repo.DB.Table("favorite_user_service").
		Where("user_id = ?", favorite.UserID).
		Where("service_id = ?", favorite.ServiceID).
		Delete(&favorite).
		Error
}
