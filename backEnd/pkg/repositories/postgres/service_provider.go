package postgres

import (
	"context"
	"errors"
	"github.com/olzhas-b/PetService/backEnd/consts"
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"github.com/olzhas-b/PetService/backEnd/pkg/models/filter"
	"gorm.io/gorm"
)

type ServiceProviderRepository struct {
	DB *gorm.DB
}

func NewServiceServiceProviderRepository(DB *gorm.DB) *ServiceProviderRepository {
	return &ServiceProviderRepository{DB: DB}
}

func (repo *ServiceProviderRepository) GetAllServices(ctx context.Context, userID int64, filter filter.ServiceProviderFilter) (listService models.ListService, total int64, err error) {

	db := repo.DB.Debug().Table("service").
		Select([]string{
			"service.id",
			"service.user_id",
			"service.price",
			"service.price_per_time",
			"service.last_activity",
			"service.service_type",
			"service.currency_code",
			"service.status",
		}).
		Joins("INNER JOIN \"user\" ON service.user_id = \"user\".id").
		Where("service.is_deleted = ?", consts.False)

	if filter.ServiceType != 0 {
		db = db.Where("service.service_type = ?", filter.ServiceType)
	}

	if !filter.DateFrom.IsZero() {
		db = db.Where("service.last_activity >= TO_DATE(?, 'YYYY.MM.DD')", filter.DateFrom.Format("2006.01.02"))
	}

	if !filter.DateTo.IsZero() {
		db = db.Where("service.last_activity <= TO_DATE(?, 'YYYY.MM.DD')", filter.DateTo.Format("2006.01.02"))
	}

	//if !filter.DateFrom.IsZero() && !filter.DateTo.IsZero() {
	//	//db = db.Where("service.last_activity between TO_DATE(?, 'YYYY.MM.DD') AND TO_DATE(?, 'YYYY.MM.DD')", filter.DateFrom.Format("2006.01.02"), filter.DateTo.Format("2006.01.02"))
	//}
	//
	//if filter.MinPrice != 0 {
	//	db = db.Where("price >= ?", filter.MinPrice)
	//}
	//
	//if filter.MaxPrice != 0 {
	//	db = db.Where("price <= ?", filter.MaxPrice)
	//}

	if filter.PetType != "" {
		db = db.Where("UPPER(acceptable_pets) like UPPER('%" + filter.PetType + "%')")
	}

	if filter.PetSize != 0 {
		db = db.Where("service.acceptable_size <= ?", filter.PetSize)
	}

	if filter.UserID != 0 {
		db = db.Where("service.user_id = ?", filter.UserID)
	}

	if filter.Country != "" {
		db = db.Where("UPPER(\"user\".country) = UPPER(?)", filter.Country)
	}

	if filter.City != "" {
		db = db.Where("UPPER(\"user\".city) = UPPER(?)", filter.City)
	}

	db.Count(&total)

	db = db.
		Preload("User", func(db *gorm.DB) *gorm.DB {
			return db.Omit("login", "password")
		}).
		Preload("User.Image", func(db *gorm.DB) *gorm.DB {
			return db.Omit("content")
		}).
		Preload("Images", func(db *gorm.DB) *gorm.DB {
			return db.Omit("content")
		})

	db = db.Offset(filter.Page * filter.Size)

	if filter.Sort != "" && filter.Order != "" {
		db = db.Order(filter.Sort + " " + filter.Order)
	}

	if filter.Size > 0 {
		db = db.Limit(filter.Size)
	}
	err = db.Find(&listService).Error
	return
}

func (repo *ServiceProviderRepository) CreateService(ctx context.Context, service models.Service) (models.Service, error) {
	err := repo.DB.Debug().Table("service").
		Omit("is_deleted", "last_activity", "status", "is_favorite").
		Save(&service).
		Error
	return service, err
}

func (repo *ServiceProviderRepository) GetFavoriteServices(ctx context.Context, userID int64) (listService models.ListService, err error) {
	defer func() {
		if errors.Is(err, gorm.ErrRecordNotFound) {
			err = nil
		}
	}()
	err = repo.DB.Debug().Table("service").
		Select([]string{
			"service.*",
			"(true) as is_favorite",
		}).
		Joins("INNER JOIN favorite_user_service on service.id = favorite_user_service.service_id").
		Where("favorite_user_service.user_id = ?", userID).
		Preload("User", func(db *gorm.DB) *gorm.DB {
			return db.Omit("login", "password")
		}).
		Preload("User.Image", func(db *gorm.DB) *gorm.DB {
			return db.Omit("content")
		}).
		Preload("Images", func(db *gorm.DB) *gorm.DB {
			return db.Omit("content")
		}).Find(&listService).
		Error
	return
}

func (repo *ServiceProviderRepository) DeleteImagesByServiceID(ctx context.Context, serviceID int64) (err error) {
	return repo.DB.Exec(`DELETE FROM image WHERE id in (select image_id from service_image where service_id = ?)`, serviceID).Error
}

func (repo *ServiceProviderRepository) DeleteService(ctx context.Context, id, userID int64) (err error) {
	err = repo.DB.Model(models.Service{}).
		Where("id = ?", id).
		Where("user_id = ?", userID).
		Update("is_deleted", true).
		Error
	return
}
