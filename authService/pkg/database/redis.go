package database

import (
	"github.com/go-redis/redis"
	"log"
)

func InitRedis() (*redis.Client, error) {
	client := redis.NewClient(&redis.Options{
		Addr:     "redis-db:6379",
		Password: "",
		DB:       2,
	})

	pong, err := client.Ping().Result()
	if err != nil {
		return client, err
	}

	log.Println(pong)

	return client, err
}
