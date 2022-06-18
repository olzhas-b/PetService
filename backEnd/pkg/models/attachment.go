package models

import (
	"fmt"
	"github.com/olzhas-b/PetService/backEnd/consts"
	"github.com/olzhas-b/PetService/backEnd/pkg/config"
	"mime/multipart"
	"strings"
)

type Attachment struct {
	ID          int64  `gorm:"id" json:"id"`
	Status      int8   `gorm:"status" json:"status"`
	Name        string `gorm:"name" json:"name"`
	ContentType string `gorm:"content_type" json:"contentType"`
	Content     []byte `gorm:"content" json:"content"`
	IsDeleted   bool   `gorm:"is_deleted" json:"isDeleted"`
}

func (att *Attachment) TableName() string {
	return "attachment"
}

func (att *Attachment) ConvertToURL() string {
	url := config.GlobalConfig.HTTP.DNS()
	url += consts.PetAttachmentUrl
	return fmt.Sprintf("%s/%d", url, att.ID)
}

func (att *Attachment) contentTypeToFileType(contentType string) string {
	contents := strings.Split(contentType, "/")
	if len(contents) == 2 {
		return contents[1]
	}
	return "PDF"
}

func (att *Attachment) ConvertToDto() map[string]interface{} {
	result := make(map[string]interface{})

	result["id"] = att.ID
	result["contentType"] = att.ContentType

	s := strings.Split(att.ContentType, "/")
	result["name"] = att.Name + "." + s[len(s)-1]
	result["url"] = att.ConvertToURL()
	return result
}

type AttachmentToSave struct {
	Name        string                  `gorm:"name" json:"name"`
	ContentType string                  `gorm:"content_type" json:"contentType"`
	Files       []*multipart.FileHeader `gorm:"-" json:"-"`
}
