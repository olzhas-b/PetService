package services

import "time"

type IRedisAuthorizationService interface {
	Store(key string, token string, t time.Duration) (err error)
	Delete(key string) (err error)
	Update(key string, value interface{}) (err error)
}
