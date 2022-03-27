package models

import "time"

type Service struct {
	ID             int64      `json:"id"`
	UserID         int64      `json:"userID"`
	ServiceType    int64      `json:"serviceType" gorm:"service_type"`
	Status         int64      `json:"status" gorm:"status"`
	AcceptableSize int64      `json:"acceptableSize" gorm:"acceptable_size"`
	Price          float64    `json:"price"`
	Longitude      float64    `json:"longitude" gorm:"longitude"`
	Latitude       float64    `json:"latitude" gorm:"latitude"`
	CurrencyCode   string     `json:"currencyCode" gorm:"currency_code"`
	PricePerTime   string     `json:"pricePerTime" gorm:"price_per_time"`
	Description    string     `json:"description" gorm:"description"`
	AcceptablePets string     `json:"acceptablePets" gorm:"acceptable_pets"`
	LastActivity   *time.Time `json:"lastActivity" gorm:"last_activity"`
	IsDeleted      *bool      `json:"isDeleted" gorm:"is_deleted"`
	User           User       `json:"user"`
	Images         []Image    `json:"images" gorm:"many2many:service_image;"`
}

func (s *Service) TableName() string {
	return "service"
}
