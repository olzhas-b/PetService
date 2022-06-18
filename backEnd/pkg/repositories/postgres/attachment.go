package postgres

import (
	"context"
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"gorm.io/gorm"
	"log"
)

type AttachmentRepository struct {
	DB *gorm.DB
}

func (repo AttachmentRepository) GetAttachmentByID(ctx context.Context, id int64) (att models.Attachment, err error) {
	defer func() {
		if err != nil {
			log.Print("repository.Attachment.GetAttachmentByID got error %v", err)
		}
	}()

	err = repo.DB.Model(&models.Attachment{}).
		Where("id = ?", id).
		Take(&att).
		Error
	return
}

func (repo AttachmentRepository) CreateAttachment(ctx context.Context, att []models.Attachment, userID, petID int64) (err error) {
	defer func() {
		if err != nil {
			log.Printf("AttachmentRepository.CreateAttachment got error %v", err)
		}
	}()

	err = repo.DB.Transaction(func(tx *gorm.DB) error {
		if err = tx.Model(&models.Attachment{}).Create(&att).Error; err != nil {
			return err
		}
		for _, attachment := range att {
			if err := tx.Table("pet_attachment").
				Create(map[string]interface{}{"pet_id": petID, "attachment_id": attachment.ID}).
				Error; err != nil {
				return err
			}
		}
		return nil
	})
	return
}

func (repo *AttachmentRepository) DeleteAttachmentByID(ctx context.Context, userID, attID int64) (err error) {
	err = repo.DB.Exec(
		`DELETE FROM attachment    
				WHERE attachment.id IN 
					(SELECT p_att.attachment_id FROM pet_attachment p_att
						INNER JOIN pet ON pet.id=p_att.pet_id
							WHERE pet.user_id=? and p_att.attachment_id=?);`, userID, attID).Error
	return
	// Delete from attachment where attachment.id in (select in
}

func NewAttachmentRepository(DB *gorm.DB) *AttachmentRepository {
	return &AttachmentRepository{DB: DB}
}
