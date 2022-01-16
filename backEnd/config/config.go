package config

import "sync"

type Config struct {
	Debug bool
}

var (
	GlobalConfig Config
	once         sync.Once
)

func Get() *Config {
	return &GlobalConfig
}
