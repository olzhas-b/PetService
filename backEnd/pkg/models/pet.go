package models

type Pet struct {
	ID     int64  `gorm:"id" json:"id"`
	Name   string `gorm:"name" json:"name"`
	Type   string `gorm:"type" json:"type"`
	Breed  string `gorm:"breed" json:"breed"`
	Weight int64  `gorm:"weight" json:"weight"`
	Status int8   `gorm:"status" json:"status"`
}

func (p *Pet) TableName() string {
	return "pet"
}
