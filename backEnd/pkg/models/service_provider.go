package models

import "time"

type ListService []*Service

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
	IsFavorite     bool       `json:"isFavorite" gorm:"is_favorite"`

	User                 User                   `json:"user" gorm:"user"`
	Images               []Image                `json:"images" gorm:"many2many:service_image;"`
	AdditionalProperties []AdditionalProperties `json:"additionalProperties"`
}

func (s *Service) TableName() string {
	return "service"
}

func (s Service) ConvertToDTO() (result map[string]interface{}) {
	result = make(map[string]interface{}, 0)
	result["id"] = s.ID
	result["serviceType"] = s.ServiceType
	result["status"] = s.Status
	result["price"] = s.Price
	result["currencyCode"] = s.CurrencyCode
	result["pricePerTime"] = s.PricePerTime
	result["lastActivity"] = s.LastActivity
	result["isFavorite"] = s.IsFavorite
	//result["description"] = s.Description
	//result["acceptablePets"] = s.AcceptablePets
	//result["latitude"] = s.Latitude
	//result["longitude"] = s.Longitude
	//result["acceptableSize"] = s.AcceptableSize

	// convert images to data transfer object
	images := make([]string, 0)
	for _, image := range s.Images {
		images = append(images, image.ConvertToURL())
	}
	result["images"] = images

	// convert user to data transfer object
	result["user"] = s.User.ConvertToDto()

	return
}

func (list ListService) ConvertListToDTO() (result []map[string]interface{}) {
	result = make([]map[string]interface{}, 0)
	for _, service := range list {
		result = append(result, service.ConvertToDTO())
	}
	return
}

type ServiceToSave struct {
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

	User User `json:"user" gorm:"user"`
}
