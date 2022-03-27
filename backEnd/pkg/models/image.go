package models

import (
	"fmt"
	"github.com/olzhas-b/PetService/backEnd/pkg/config"

	"mime/multipart"
)

type Image struct {
	ID          int64  `gorm:"id" json:"id"`
	Status      int8   `gorm:"status" json:"status"`
	Name        string `gorm:"name" json:"name"`
	ContentType string `gorm:"content_type" json:"contentType"`
	Content     []byte `gorm:"content" json:"content"`
}

func (i *Image) TableName() string {
	return "image"
}

func (i *Image) ConvertToURL() string {
	url := config.GlobalConfig.HTTP.DNS()
	url += "/api/v1/image"
	return fmt.Sprintf("%s/%d.%s", url, i.ID, i.contentTypeToFileType(i.ContentType))
}

func (i *Image) contentTypeToFileType(contentType string) string {
	switch contentType {
	case "":
		return "jbeg"
	default:
		return "png"
	}
}

type ImageToSave struct {
	Name        string                  `gorm:"name" json:"name"`
	ContentType string                  `gorm:"content_type" json:"contentType"`
	Files       []*multipart.FileHeader `gorm:"-" json:"-"`
}
