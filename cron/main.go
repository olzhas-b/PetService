package main

import (
	"context"
	"cron/internal/config"
	"cron/internal/db"
	"cron/internal/repository"
	"cron/internal/services"
	"log"
	"time"
)

type MyJob struct {
	Name string
}

type Schedule struct {
}

func (s Schedule) Next(time2 time.Time) time.Time {
	return time2.Add(time.Second * 10)
}

func NewJob(name string) *MyJob {
	return &MyJob{name}
}

func (job *MyJob) Run() {
	for i := 0; i < 5; i++ {
		time.Sleep(time.Millisecond * 100)
		log.Println(job.Name, i)
	}
}

func main() {
	cfg, err := config.InitConfig("config.yaml")
	if err != nil {
		log.Fatal(err)
	}
	DB, err := db.InitPostgres()
	if err != nil {
		log.Fatal(err)
	}
	scheduler := services.NewScheduler(cfg, repository.NewRepository(DB))
	scheduler.Start(context.Background())
}
