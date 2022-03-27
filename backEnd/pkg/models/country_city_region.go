package models

type CountriesList []*Country

func (c *CountriesList) TableName() string {
	return "country"
}

type Country struct {
	Country   string `gorm:"name" json:"name"`
	CountryID int64  `gorm:"country_id" json:"-"`

	Cities []*City `json:"cities" gorm:"foreignKey:CountryID;references:CountryID"`
}

func (c *Country) ConvertToDTO() (result map[string]interface{}) {
	result = make(map[string]interface{})

	result["country"] = c.Country
	cities := make([]string, 0, len(c.Cities))
	for _, city := range c.Cities {
		cities = append(cities, city.Name)
	}
	result["cities"] = cities
	return
}

type City struct {
	CityID    int64  `gorm:"city_id" json:"-"`
	CountryID int64  `gorm:"country_id" json:"-"`
	RegionID  int64  `gorm:"region_id" json:"-"`
	Name      string `gorm:"name" json:"name"`
}

func (c *City) TableName() string {
	return "city"
}

func (c *CountriesList) ConvertToDTO() (result []map[string]interface{}) {
	result = make([]map[string]interface{}, 0, len(*c))
	for _, country := range *c {
		result = append(result, country.ConvertToDTO())
	}
	return
}

type Region struct {
	RegionID  int64  `gorm:"region_id" json:"regionID"`
	Name      string `gorm:"name" json:"name"`
	CountryID int64  `gorm:"country_id" json:"countryID"`
}
