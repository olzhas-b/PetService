package repositories

import "github.com/olzhas-b/PetService/backEnd/pkg/models"

type INotificationRepository interface {
	SaveNotification(models.Notification) error
}
