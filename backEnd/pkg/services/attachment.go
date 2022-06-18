package services

import (
	"context"
	"fmt"
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	repo "github.com/olzhas-b/PetService/backEnd/pkg/repositories"
	"github.com/olzhas-b/PetService/backEnd/tools"
	"github.com/olzhas-b/PetService/backEnd/tools/utils"

	"mime/multipart"
)

type AttachmentService struct {
	repo *repo.Repositories
}

func (a *AttachmentService) ServiceGetAttachmentByID(ctx context.Context, id int64) (att models.Attachment, err error) {
	return a.repo.GetAttachmentByID(ctx, id)
}

func (a AttachmentService) ServiceCreateAttachment(ctx context.Context, attachments []*multipart.FileHeader, petID int64) (err error) {
	userID := utils.GetCurrentUserID(ctx)
	attachmentsToSave := make([]models.Attachment, 0)
	for _, file := range attachments {
		if file != nil {
			attachmentsToSave = append(attachmentsToSave, models.Attachment{
				ContentType: file.Header.Get("Content-Type"),
				Name:        file.Filename,
				Content:     tools.ReadProperly(file),
			})
		}
	}
	return a.repo.CreateAttachment(ctx, attachmentsToSave, userID, petID)
}

func (a AttachmentService) ServiceDeleteAttachmentByID(ctx context.Context, id int64) (err error) {
	userID := utils.GetCurrentUserID(ctx)
	if err := a.repo.IAttachmentRepository.DeleteAttachmentByID(ctx, userID, id); err != nil {
		return fmt.Errorf("AttachmentService.ServiceDeleteAttachmentByID got error %w", err)
	}
	return nil
}

func NewAttachmentService(repo *repo.Repositories) *AttachmentService {
	return &AttachmentService{
		repo: repo,
	}
}
