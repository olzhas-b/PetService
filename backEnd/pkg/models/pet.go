package models

type PetList []*Pet

type Pet struct {
	ID      int64  `gorm:"id" json:"id"`
	UserID  int64  `gorm:"user_id" json:"userID"`
	ImageID int64  `gorm:"image_Id" json:"imageID"`
	Name    string `gorm:"name" json:"name"`
	Type    string `gorm:"type" json:"type"`
	Breed   string `gorm:"breed" json:"breed"`
	Weight  int64  `gorm:"weight" json:"weight"`
	Status  int8   `gorm:"status" json:"status"`

	Image *Image
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
	result["status"] = p.Status
	result["imageID"] = p.ImageID

	if p.Image != nil {
		result["image"] = p.Image.ConvertToURL()
	}

	return
}
