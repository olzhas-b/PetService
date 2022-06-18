package services

import (
	"context"
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"mime/multipart"
)

type IAttachmentService interface {
	ServiceGetAttachmentByID(ctx context.Context, id int64) (att models.Attachment, err error)
	ServiceCreateAttachment(ctx context.Context, attachments []*multipart.FileHeader, petID int64) (err error)
	ServiceDeleteAttachmentByID(ctx context.Context, id int64) (err error)
}
