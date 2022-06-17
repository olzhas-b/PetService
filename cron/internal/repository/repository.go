package repository

import (
	"cron/internal/consts"
	"cron/internal/models"
	"gorm.io/gorm"
)

type IRepository interface {
	GetAllJobs() ([]models.Job, error)
	InsertJobHistory() error
}

type Repository struct {
	db *gorm.DB
}

func NewRepository(db *gorm.DB) *Repository {
	return &Repository{db: db}
}

func (repo *Repository) GetAllJobs() (result []models.Job, err error) {
	err = repo.db.Model(&models.Job{}).
		Where("is_active = ?", consts.True).
		Where("is_deleted = ?", consts.False).
		Find(&result).
		Error
	return result, nil
}

func (repo *Repository) InsertJobHistory() error {
	return nil
}
