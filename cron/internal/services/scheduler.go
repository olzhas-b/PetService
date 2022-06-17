package services

import (
	"context"
	"cron/internal/config"
	"cron/internal/models"
	"cron/internal/repository"
	"fmt"
	"github.com/robfig/cron"
	"log"
	"net/http"
	"sync"
	"time"
)

type Scheduler struct {
	url              string
	authorizationURL string
	username         string
	password         string
	Duration         time.Duration
	repository       *repository.Repository
	mu               sync.Mutex

	crons map[string]*cron.Cron
	token string
}

func NewScheduler(cfg config.Config, repo *repository.Repository) *Scheduler {
	return &Scheduler{
		url:        cfg.Scheduler.URL,
		repository: repo,
		crons:      make(map[string]*cron.Cron),
		Duration:   time.Minute * time.Duration(cfg.Scheduler.Duration),
	}
}

func (scheduler *Scheduler) Start(ctx context.Context) {
	ticker := time.NewTicker(scheduler.Duration)
	for {
		jobs, err := scheduler.repository.GetAllJobs()
		if err != nil {
			log.Println(err)
		}
		scheduler.scheduleJobs(jobs)

		select {
		case <-ctx.Done():
			return
		case <-ticker.C:
		}
	}
}

func (scheduler *Scheduler) scheduleJobs(jobs []models.Job) {
	for _, job := range jobs {
		c := cron.New()
		job := job
		if err := c.AddFunc(job.Expression, func() {
			for i := 0; i < job.MaxAttempts; i++ {
				log.Println(job.Name, "activated")
				if err := scheduler.sendJobRequest(job); err == nil {
					break
				}
			}
		}); err != nil {
			log.Println("cron.Add.Func got error: ", err)
		}
		scheduler.crons[job.Code] = c
		c.Start()
	}
}

func (scheduler *Scheduler) sendJobRequest(job models.Job) error {
	url := fmt.Sprintf("%s/%s/start", scheduler.url, job.Code)
	log.Println(url)
	req, err := http.NewRequest(http.MethodPost, url, nil)
	if err != nil {
		return err
	}
	req.Header.Add("Content-Type", "application/json")

	client := http.Client{
		Timeout: time.Second * 5,
	}
	if resp, err := client.Do(req); resp != nil || err != nil {
		return fmt.Errorf("http post request response %v and error %v", resp, err)
	}
	return nil
}
