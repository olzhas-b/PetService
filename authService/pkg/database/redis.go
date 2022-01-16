package database

import (
	"context"
	"github.com/go-redis/redis"
	"log"
)

func InitRedis() (*redis.Client, error) {
	client := redis.NewClient(&redis.Options{
		Addr:     "localhost:6379",
		Password: "",
		DB:       2,
	})

	pong, err := client.Ping(context.TODO()).Result()
	if err != nil {
		return client, err
	}

	log.Println(pong)

	return client, err
}
