package database

import (
	"github.com/go-redis/redis"
	"github.com/olzhas-b/PetService/backEnd/pkg/config"
	"log"
)

func InitRedis() (*redis.Client, error) {
	conf := config.Get().Redis
	client := redis.NewClient(&redis.Options{
		Addr: conf.Addr,
		DB:   2,
	})

	pong, err := client.Ping().Result()
	if err != nil {
		return client, err
	}

	log.Println(pong)

	return client, err
}
