package cmd

import (
	"github.com/olzhas-b/PetService/backEnd/config"
	"github.com/olzhas-b/PetService/backEnd/modules/logger"
	"github.com/olzhas-b/PetService/backEnd/pkg/database"
	"github.com/olzhas-b/PetService/backEnd/pkg/repositories"
	"github.com/olzhas-b/PetService/backEnd/pkg/services"
	"github.com/olzhas-b/PetService/backEnd/pkg/transport"
	"github.com/olzhas-b/PetService/backEnd/pkg/transport/restful/handler"
	"github.com/spf13/cobra"
	"log"
)

var startCmd = &cobra.Command{
	Use:   "start",
	Short: "start server",
	Run: func(cmd *cobra.Command, args []string) {
		//time.Sleep(time.Second * 30)
		db, err := database.InitPostgres()
		if err != nil {
			log.Fatalf("failed connection with postgres err: %v", err)
		}
		redis, err := database.InitRedis()
		if err != nil {
			log.Fatalf("failed connection with Redis err: %v", err)
		}
		cfg := config.Get()
		logger.InitLogger(cfg)

		repo := repositories.NewRepositories(db)
		services := services.NewServices(repo, redis)

		h := handler.NewHandler(services)

		server := new(transport.Server)
		log.Println("Server trying to run")
		if err := server.RunHTTPServer(nil, h); err != nil {
			log.Fatalf("failed to run server, %v\n", err)
		}
	},
}
