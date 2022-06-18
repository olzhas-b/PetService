package jobs

import (
	"context"
	"github.com/olzhas-b/PetService/backEnd/pkg/services"
	"google.golang.org/appengine/log"
)

type Job struct {
	service *services.Services
}

type IJobInterface interface {
	StartJob(code string)
}

func NewJob(service *services.Services) *Job {
	return &Job{service: service}
}

func (j *Job) StartJob(ctx context.Context, code string) {
	switch code {
	case NotifyUserExpiredCertificate:
		if err := j.service.INotificationService.ServiceNotifyPetOwnersExpiredCertificate(ctx); err != nil {
			log.Warningf(ctx, "StartJob.NotifyUserExpiredCertificate got error:", err)
		}
	case "test":
		println("test")
	default:
		log.Warningf(ctx, "job doesn't exist with name:", code)
	}
}
