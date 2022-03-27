package models

type Favorite struct {
	ServiceID int64
	UserID    int64
}

func (fav *Favorite) TableName() string {
	return "favorite_user_service"
}
