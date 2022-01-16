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
		db, err := database.InitPostgres()
		if err != nil {
			log.Fatal("failed connection with postgres")
		}
		cfg := config.Get()
		logger.InitLogger(cfg)

		repo := repositories.NewRepositories(db)
		services := services.NewServices(repo)

		h := handler.NewHandler(services)

		server := new(transport.Server)
		log.Println("Server trying to run")
		if err := server.RunHTTPServer(nil, h); err != nil {
			log.Fatalf("failed to run server, %v\n", err)
		}
	},
}
