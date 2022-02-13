package postgres

import (
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"gorm.io/gorm"
)

type CountryRepository struct {
	DB *gorm.DB
}

func NewCountryRepository(DB *gorm.DB) *CountryRepository {
	return &CountryRepository{DB: DB}
}

func (repo *CountryRepository) GetAllCountries() (result models.CountriesList, err error) {
	err = repo.DB.Table("country").
		Select([]string{
			"name as country",
			"country_id",
		}).
		Preload("Cities").
		Find(&result).
		Error
	return
}
