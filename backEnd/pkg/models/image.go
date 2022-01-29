package models

import "mime/multipart"

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

type ImageToSave struct {
	Name        string                  `gorm:"name" json:"name"`
	ContentType string                  `gorm:"content_type" json:"contentType"`
	Files       []*multipart.FileHeader `gorm:"-" json:"-"`
}
