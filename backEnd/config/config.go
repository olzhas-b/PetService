package config

import (
	"sync"
	"time"
)

type Config struct {
	Debug       bool
	TokenConfig TokenConfig
}

type TokenConfig struct {
	AccessSecret  string
	RefreshSecret string
	AccessTtl     time.Duration
	RefreshTtl    time.Duration
}

var (
	GlobalConfig = Config{
		TokenConfig: TokenConfig{
			AccessSecret:  "asdflsadaqjwe123DEavlkjl12312312",
			RefreshSecret: "fadsf0ivoi@vlka0sd123,vk234/adsf;1!1231$$$#123",
			AccessTtl:     time.Minute * 5,
			RefreshTtl:    time.Hour * 100000,
		}}
	once sync.Once
)

func Get() *Config {
	return &GlobalConfig
}
