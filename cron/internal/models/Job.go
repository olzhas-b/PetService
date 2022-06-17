package models

import (
	"fmt"
	"log"
	"net/http"
	"time"
)

type Job struct {
	ID          int64
	MaxAttempts int
	Name        string
	Url         string
	Code        string
	Expression  string // example "* * * * * *" => every second
	IsActive    bool
	IsRunning   bool
	IsDeleted   bool
}

func (j *Job) TableName() string {
	return "job"
}

func (j *Job) Run() {
	//if err := scheduler.cron.AddFunc(job.Expression, func() {
	for i := 0; i < 3; i++ {
		log.Println(j.Name, "activated")
		//if err := j.sendJobRequest(); err == nil {
		//	break
		//}
	}
}

func (j *Job) sendJobRequest() error {
	url := fmt.Sprintf("%s/%s", j.Url, j.Code)

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
