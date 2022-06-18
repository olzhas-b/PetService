package models

import (
	"github.com/olzhas-b/PetService/backEnd/tools"
	"time"
)

type PetList []*Pet

type Pet struct {
	Name       string    `gorm:"name" json:"name"`
	Type       string    `gorm:"type" json:"type"`
	Breed      string    `gorm:"breed" json:"breed"`
	ID         int64     `gorm:"id" json:"id"`
	UserID     int64     `gorm:"user_id" json:"userID"`
	ImageID    int64     `gorm:"image_Id" json:"imageID"`
	Weight     int64     `gorm:"weight" json:"weight"`
	Count      int64     `gorm:"count" json:"count"`
	Status     int8      `gorm:"status" json:"status"`
	IsGroup    bool      `gorm:"is_group" json:"isGroup"`
	ExpireDate time.Time `gorm:"expire_date" json:"expireDate"`

	Attachments []Attachment `json:"attachments" gorm:"many2many:pet_attachment;"`
	Image       *Image
}

type PetView struct {
	ID         int64  `gorm:"id" json:"id"`
	UserID     int64  `gorm:"user_id" json:"userID"`
	ImageID    int64  `gorm:"image_Id" json:"imageID"`
	Name       string `gorm:"name" json:"name"`
	Type       string `gorm:"type" json:"type"`
	Breed      string `gorm:"breed" json:"breed"`
	Weight     int64  `gorm:"weight" json:"weight"`
	Count      int64  `gorm:"count" json:"count"`
	Status     int8   `gorm:"status" json:"status"`
	IsGroup    bool   `gorm:"is_group" json:"isGroup"`
	ExpireDate string `gorm:"expire_date" json:"expireDate"`
}

func (p *PetView) ConvertToPet() Pet {
	return Pet{
		ID:         p.ID,
		ImageID:    p.ImageID,
		UserID:     p.UserID,
		Name:       p.Name,
		Type:       p.Type,
		Breed:      p.Breed,
		Weight:     p.Weight,
		Count:      p.Count,
		IsGroup:    p.IsGroup,
		Status:     p.Status,
		ExpireDate: tools.StrToTime(p.ExpireDate),
	}

}

func (p *Pet) TableName() string {
	return "pet"
}

func (p *PetList) ConvertToDTO() (result []map[string]interface{}) {
	if p == nil {
		return
	}
	for _, pet := range *p {
		result = append(result, pet.ConvertToDTO())
	}
	return
}

func (p *Pet) ConvertToDTO() (result map[string]interface{}) {
	result = make(map[string]interface{})
	result["id"] = p.ID
	result["userID"] = p.UserID
	result["name"] = p.Name
	result["type"] = p.Type
	result["breed"] = p.Breed
	result["weight"] = p.Weight
	result["count"] = p.Count
	result["isGroup"] = p.IsGroup
	result["status"] = p.Status
	result["imageID"] = p.ImageID
	result["expireDate"] = p.ExpireDate.Format("02.01.2006")

	if p.Image != nil {
		result["image"] = p.Image.ConvertToURL()
	}

	if p.Attachments != nil {
		attachmentsDTO := make([]map[string]interface{}, 0)
		for _, attachment := range p.Attachments {
			attachmentsDTO = append(attachmentsDTO, attachment.ConvertToDto())
		}
		result["attachments"] = attachmentsDTO
	}
	return
}

func (p *Pet) GetPublishInterestID() string {
	return "petId=" + tools.Int64ToStr(p.ID)
}
