package config

import (
	"fmt"
	"github.com/joho/godotenv"
	"github.com/olzhas-b/PetService/backEnd/tools"
	"log"
	"os"

	"time"
)

type Config struct {
	Debug        bool
	Redis        RedisConfig
	Database     DatabaseConfig
	Token        TokenConfig
	HTTP         HTTPConfig
	Notification Notification
	TimeOut      bool
	Environment  string
}

type Notification struct {
	InstanceID string
	SecretKey  string
}

type HTTPConfig struct {
	Http string
	Name string
	Port string
}

func (h *HTTPConfig) DNS() string {
	if GlobalConfig.Environment == "PROD" {
		return fmt.Sprintf("%s://%s", h.Http, h.Name)
	}
	return fmt.Sprintf("%s://%s:%s", h.Http, h.Name, h.Port)
}

type RedisConfig struct {
	Enable   bool
	Addr     string
	Port     string
	Password string
	DB       int
}

type DatabaseConfig struct {
	Enable   bool
	Driver   string
	Name     string
	Host     string
	Port     int
	User     string
	Password string
	Dsn      string
}

type TokenConfig struct {
	AccessSecret  string
	RefreshSecret string
	AccessTtl     time.Duration
	RefreshTtl    time.Duration
}

var (
	GlobalConfig = Config{}
)

func InitConfig() {
	env, err := godotenv.Read(".env")
	if err != nil {
		log.Fatal("couldn't read env file %v", err)
	}
	port := os.Getenv("PORT")
	if len(port) == 0 {
		port = env["HTTP-PORT"]
	}
	GlobalConfig = Config{
		Token: TokenConfig{
			AccessSecret:  "asdflsadaqjwe123DEavlkjl12312312",
			RefreshSecret: "fadsf0ivoi@vlka0sd123,vk234/adsf;1!1231$$$#123",
			AccessTtl:     time.Hour * 1000000,
			RefreshTtl:    time.Hour * 1000000,
		},
		HTTP: HTTPConfig{
			Name: env["HTTP-NAME"],
			Port: port,
			Http: env["HTTP-TYPE"],
		},
		Redis: RedisConfig{
			Addr:     env["REDIS-ADDR"],
			Password: env["REDIS-PASSWORD"],
		},
		Notification: Notification{
			InstanceID: env["NOTIFICATION-INSTANCE-ID"],
			SecretKey:  env["NOTIFICATION-SECRET-KEY"],
		},
		TimeOut:     tools.StrToBool(env["TIME-OUT"]),
		Environment: env["ENVIRONMENT"],
	}
}

func Get() *Config {
	return &GlobalConfig
}
