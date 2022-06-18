package services

import (
	"context"
	"fmt"
	"github.com/google/martian/log"
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"github.com/olzhas-b/PetService/backEnd/pkg/repositories"
	push_notifications "github.com/pusher/push-notifications-go"
	"time"
)

type NotificationService struct {
	repo              *repositories.Repositories
	notificatonClient push_notifications.PushNotifications
}

func NewNotificationService(repo *repositories.Repositories, notificationClient push_notifications.PushNotifications) *NotificationService {
	return &NotificationService{
		repo:              repo,
		notificatonClient: notificationClient,
	}
}
func (srv *NotificationService) ServiceNotifyPetOwnersExpiredCertificate(ctx context.Context) error {
	now := time.Now()
	pets, err := srv.repo.IPetRepository.GetPetsWhichExpiredCertificate(ctx, now)
	if err != nil {
		return fmt.Errorf("NotificationService.ServiceNotifyPetOwnersExpiredCertificate got error: %w", err)
	}
	for _, pet := range pets {
		pubID, err := srv.notificatonClient.PublishToInterests([]string{pet.GetPublishInterestID()}, NewExpiredRequest(pet))
		if err != nil {
			log.Errorf("ServiceNotifyPetOwnersExpiredCertificate.PublishToInterests got error: %v", err)
		} else {
			println("successfully send notification with pubID:", pubID)
		}
	}
	return nil
}

func NewExpiredRequest(pet models.Pet) map[string]interface{} {
	return map[string]interface{}{
		"fcm": map[string]interface{}{
			"notification": map[string]interface{}{
				"title":        "Истекает срок вакцинации!",
				"body":         fmt.Sprintf("У вашего питомца %s истекает срок вакцинации!", pet.Name),
				"click_action": "OPEN_ACTIVITY_1",
			},
			"data": map[string]interface{}{
				"title":    "Истекает срок вакцинации!",
				"body":     fmt.Sprintf("У вашего питомца %s истекает срок вакцинации!", pet.Name),
				"deepLink": fmt.Sprintf("https://api-service-pod1.herokuapp.com/api/v1/user/pet/%d", pet.ID),
			},
		},
	}
}
