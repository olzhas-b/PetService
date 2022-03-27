package models

import "time"

type ServiceDetail struct {
	ID             int64     `gorm:"id" json:"id"`
	AcceptableSize int64     `gorm:"acceptable_size" json:"acceptableSize"`
	Longitude      float64   `gorm:"longitude" json:"longitude"`
	Latitude       float64   `gorm:"latitude" json:"latitude"`
	AcceptablePets string    `gorm:"acceptable_pets" json:"acceptablePets"`
	Description    string    `gorm:"description" json:"description"`
	LastActivity   time.Time `gorm:"last_activity" json:"lastActivity"`
	Status         int8      `gorm:"status" json:"status"`

	AdditionalProperties []AdditionalProperties `gorm:"foreignKey:ServiceID;references:ID" json:"additionalProperties"`
}

func (sd *ServiceDetail) TableName() string {
	return "service"
}
