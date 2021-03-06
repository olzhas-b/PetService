package cmd

import (
	"github.com/olzhas-b/PetService/backEnd/modules/logger"
	"github.com/olzhas-b/PetService/backEnd/pkg/config"
	"github.com/olzhas-b/PetService/backEnd/pkg/database"
	"github.com/olzhas-b/PetService/backEnd/pkg/jobs"
	"github.com/olzhas-b/PetService/backEnd/pkg/repositories"
	"github.com/olzhas-b/PetService/backEnd/pkg/services"
	"github.com/olzhas-b/PetService/backEnd/pkg/transport"
	"github.com/olzhas-b/PetService/backEnd/pkg/transport/restful/handler"
	push_notifications "github.com/pusher/push-notifications-go"
	"github.com/spf13/cobra"
	"log"
	"time"
)

var startCmd = &cobra.Command{
	Use:   "start",
	Short: "start server",
	Run: func(cmd *cobra.Command, args []string) {
		config.InitConfig()
		if config.Get().TimeOut {
			time.Sleep(time.Second * 15)
		}

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
		notifClient, err := push_notifications.New(cfg.Notification.InstanceID, cfg.Notification.SecretKey)
		repo := repositories.NewRepositories(db)
		services := services.NewServices(repo, redis, notifClient)
		job := jobs.NewJob(services)

		h := handler.NewHandler(services, job)
		server := new(transport.Server)
		log.Println("Server trying to run")
		if err := server.RunHTTPServer(cfg, h); err != nil {
			log.Fatalf("failed to run server, got error %v\n", err)
		}
	},
}
