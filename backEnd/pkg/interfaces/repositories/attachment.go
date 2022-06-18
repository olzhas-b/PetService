package repositories

import (
	"context"
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
)

type IAttachmentRepository interface {
	GetAttachmentByID(ctx context.Context, id int64) (att models.Attachment, err error)
	CreateAttachment(ctx context.Context, att []models.Attachment, userID, petID int64) (err error)
	DeleteAttachmentByID(ctx context.Context, userID, attID int64) (err error)
}
