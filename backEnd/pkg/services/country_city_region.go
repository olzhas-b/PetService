package services

import (
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"github.com/olzhas-b/PetService/backEnd/pkg/repositories"
)

type CountryService struct {
	repo *repositories.Repositories
}

func NewCountryService(repo *repositories.Repositories) *CountryService {
	return &CountryService{repo: repo}
}

func (srv *CountryService) ServiceGetAllCountries() (result models.CountriesList, err error) {
	return srv.repo.ICountryRepository.GetAllCountries()
}
