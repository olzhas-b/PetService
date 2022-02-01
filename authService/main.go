package main

import (
	"github.com/olzhas-b/PetService/authService/config"
	"github.com/olzhas-b/PetService/authService/pkg/database"
	"github.com/olzhas-b/PetService/authService/pkg/models"
	"github.com/olzhas-b/PetService/authService/pkg/repositories"
	"github.com/olzhas-b/PetService/authService/pkg/services"
	"github.com/olzhas-b/PetService/authService/pkg/transport"
	handler2 "github.com/olzhas-b/PetService/authService/pkg/transport/restful/handler"
	"log"
	"time"
)

func main() {
	time.Sleep(time.Minute)

	config.InitConfig()
	db, err := database.InitPostgres()
	if err != nil {
		log.Fatalf("postgres connection was failed: %v", err)
	}

	rConn, err := database.InitRedis()
	if err != nil {
		log.Fatalf("redis connection was failed: %v", err)
	}

	repo := repositories.NewRepositories(db)

	tokenConfig := models.TokenConfig{
		AccessSecret:  "asdflsadaqjwe123DEavlkjl12312312",
		RefreshSecret: "fadsf0ivoi@vlka0sd123,vk234/adsf;1!1231$$$#123",
		AccessTtl:     time.Hour * 1000000,
		RefreshTtl:    time.Hour * 1000000,
	}

	service := services.NewServices(repo, rConn, tokenConfig)
	handler := handler2.NewHandler(service)

	server := new(transport.Server)

	log.Println("Server trying to run")
	if err := server.RunHTTPServer(nil, handler); err != nil {
		log.Fatalf("server shut down with error %v\n", err)
	}
}

//db, err := database.InitPostgres()
//if err != nil {
//log.Fatal("failed connection with postgres")
//}
//cfg := config.Get()
//logger.InitLogger(cfg)
//
//repo := repositories.NewRepositories(db)
//services := services.NewServices(repo)
//
//h := handler.NewHandler(services)
//
//server := new(transport.Server)
//log.Println("Server trying to run")
//if err := server.RunHTTPServer(nil, h); err != nil {
//log.Fatalf("failed to run server, %v\n", err)
//}
