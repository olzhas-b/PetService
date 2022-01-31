package models

type AdditionalProperties struct {
	ID        int64  `json:"-"`
	ServiceID int64  `json:"serviceID"`
	Label     string `json:"label"`
	Text      string `json:"text"`
}

func (addProp *AdditionalProperties) TableName() string {
	return "additional_properties"
}
