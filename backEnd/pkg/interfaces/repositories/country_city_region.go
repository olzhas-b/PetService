package repositories

import "github.com/olzhas-b/PetService/backEnd/pkg/models"

type ICountryRepository interface {
	GetAllCountries() (result models.CountriesList, err error)
}
