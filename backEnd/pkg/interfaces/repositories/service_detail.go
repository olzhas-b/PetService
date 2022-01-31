package repositories

import "github.com/olzhas-b/PetService/backEnd/pkg/models"

type IServiceDetailRepository interface {
	GetServiceDetail(id int64) (sDetail models.ServiceDetail, err error)
}
