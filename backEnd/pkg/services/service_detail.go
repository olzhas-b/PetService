package services

import (
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"github.com/olzhas-b/PetService/backEnd/pkg/repositories"
)

type ServiceDetailService struct {
	repo *repositories.Repositories
}

func NewServiceDetailService(repo *repositories.Repositories) *ServiceDetailService {
	return &ServiceDetailService{repo: repo}
}

func (srv *ServiceDetailService) ServiceGetServiceDetail(id int64) (sDetail models.ServiceDetail, err error) {
	return srv.repo.IServiceDetailRepository.GetServiceDetail(id)
}
