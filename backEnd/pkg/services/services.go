package services

import (
	"github.com/go-redis/redis"
	interfaces "github.com/olzhas-b/PetService/backEnd/pkg/interfaces/services"
	repo "github.com/olzhas-b/PetService/backEnd/pkg/repositories"
	push_notifications "github.com/pusher/push-notifications-go"
)

//var Service Services

type Services struct {
	interfaces.IUserService
	interfaces.IImageService
	interfaces.IServiceProviderService
	interfaces.IAuthorizationService
	interfaces.IServiceDetailService
	interfaces.IPetService
	interfaces.IRatingService
	interfaces.ICountryService
	interfaces.IFavoriteService
	interfaces.IAttachmentService
	interfaces.INotificationService
}

func NewServices(repo *repo.Repositories, redis *redis.Client, notifications push_notifications.PushNotifications) *Services {
	return &Services{
		IUserService:            NewUserService(repo),
		IImageService:           NewImageService(repo),
		IServiceProviderService: NewServiceProvider(repo),
		IServiceDetailService:   NewServiceDetailService(repo),
		IAuthorizationService:   NewAuthorizationService(redis),
		IPetService:             NewPetService(repo),
		IRatingService:          NewRatingService(repo),
		ICountryService:         NewCountryService(repo),
		IFavoriteService:        NewFavoriteService(repo),
		IAttachmentService:      NewAttachmentService(repo),
		INotificationService:    NewNotificationService(repo, notifications),
	}
}
