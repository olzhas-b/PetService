package models

import "time"

type User struct {
	ID            int64     `json:"id"`
	Login         string    `gorm:"login" json:"login"`
	Username      string    `gorm:"username" json:"username"`
	Phone         string    `gorm:"phone" json:"phone"`
	Password      string    `gorm:"password" json:"password"`
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
	return
}
