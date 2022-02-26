package models

import "time"

type UserType int64

type User struct {
	ID            int64     `json:"id"`
	ImageID       *int64    `gorm:"image_id" json:"imageID"`
	Login         string    `gorm:"login,omitempty" json:"login"`
	Username      string    `gorm:"username,omitempty" json:"username"`
	Phone         string    `gorm:"phone" json:"phone"`
	Password      string    `gorm:"password,omitempty" json:"password"`
	FirstName     string    `gorm:"first_name" json:"firstName"`
	LastName      string    `gorm:"last_name" json:"lastName"`
	FullName      string    `gorm:"full_name" json:"fullName"`
	City          string    `gorm:"city" json:"city"`
	Country       string    `gorm:"country" json:"country"`
	Location      string    `gorm:"location" json:"location"`
	Description   string    `gorm:"description" json:"description"`
	CountRating   int64     `gorm:"count_rating" json:"countRating"`
	AverageRating float64   `gorm:"average_rating" json:"averageRating"`
	Created       time.Time `gorm:"created" json:"created"`
	Updated       time.Time `gorm:"updated" json:"updated"`
	Status        int64     `gorm:"status" json:"status"`
	IsDeleted     bool      `gorm:"is_deleted" json:"isDeleted"`

	Image *Image
}

func (u *User) TableName() string {
	return "user"
}

func (u *User) FillDefaultValue() {
	u.ID = 0
	timeNow := time.Now()
	u.Created = timeNow
	u.Updated = timeNow
}

func (u *User) ConvertToDto() (result map[string]interface{}) {
	result = make(map[string]interface{})
	result["id"] = u.ID
	result["username"] = u.Username
	result["phone"] = u.Phone
	result["firstName"] = u.FirstName
	result["lastName"] = u.LastName
	result["fullName"] = u.FullName
	result["city"] = u.City
	result["country"] = u.Country
	result["location"] = u.Location
	result["averageRating"] = u.AverageRating
	result["countRating"] = u.CountRating
	result["status"] = u.Status
	result["updated"] = u.Updated
	result["created"] = u.Created

	if u.Image != nil {
		result["image"] = u.Image.ConvertToURL()
	}
	return
}

func (u *User) GetOmitColumns() []string {
	omit := []string{"average_rating", "count_rating", "status", "created", "password", "username", "login", "phone"}
	if u.Image == nil {
		omit = append(omit, "image_id")
	}
	return omit
}
