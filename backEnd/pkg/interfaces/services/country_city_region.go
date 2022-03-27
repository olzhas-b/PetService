package services

import "github.com/olzhas-b/PetService/backEnd/pkg/models"

type ICountryService interface {
	ServiceGetAllCountries() (result models.CountriesList, err error)
}
