package services

import "context"

type INotificationService interface {
	ServiceNotifyPetOwnersExpiredCertificate(context context.Context) error
}
