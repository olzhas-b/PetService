package models

type AdditionalProperties struct {
	ID        int64  `gorm:"id" json:"-"`
	ServiceID int64  `gorm:"service_id" json:"-"`
	Label     string `gorm:"label" json:"label"`
	Text      string `gorm:"text" json:"text"`
}

func (addProp *AdditionalProperties) TableName() string {
	return "additional_properties"
}
