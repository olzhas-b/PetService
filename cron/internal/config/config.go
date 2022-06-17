package config

import (
	"fmt"
	"gopkg.in/yaml.v3"
	"io/ioutil"
)

var config Config

type Config struct {
	Scheduler Scheduler `yaml:"Scheduler"`
}

type Authorization struct {
	URL      string `yaml:"URL"`
	Username string `yaml:"Username"`
	Password string `yaml:"Password"`
}

type Scheduler struct {
	URL           string        `yaml:"URL"`
	Duration      int64         `yaml:"Duration"`
	Authorization Authorization `yaml:"Authorization"`
}

func GetConfig() Config {
	return config
}

func InitConfig(fileName string) (Config, error) {
	file, err := ioutil.ReadFile(fileName)
	if err != nil {
		return Config{}, fmt.Errorf("couldn't read config file got error %v", err)
	}

	if err = yaml.Unmarshal(file, &config); err != nil {
		return Config{}, fmt.Errorf("cound't parse config file got error %v", err)
	}

	return config, nil
}
