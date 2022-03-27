package models

import "time"

type Rating struct {
	ID          int64     `gorm:"id" json:"-"`
	UserID      int64     `gorm:"user_id" json:"userID"`
	EstimatorID int64     `gorm:"estimator_id" json:"estimatorID"`
	Score       int8      `gorm:"score" json:"score"`
	Created     time.Time `gorm:"created" json:"created"`
}

func (r *Rating) TableName() string {
	return "rating"
}
