package services

import "github.com/olzhas-b/PetService/backEnd/pkg/models"

type IServiceDetailService interface {
	ServiceGetServiceDetail(id int64) (sDetail models.ServiceDetail, err error)
}
