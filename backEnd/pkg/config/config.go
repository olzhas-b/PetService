package config

import (
	"fmt"
	"github.com/joho/godotenv"
	"github.com/olzhas-b/PetService/backEnd/tools"

	"log"
	"time"
)

type Config struct {
	Debug    bool
	Redis    RedisConfig
	Database DatabaseConfig
	Token    TokenConfig
	HTTP     HTTPConfig
	TimeOut  bool
}

type HTTPConfig struct {
	Http string
	Name string
	Port string
}

func (h *HTTPConfig) DNS() string {
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
	envs, err := godotenv.Read(".env")
	if err != nil {
		log.Fatal(err)
	}

	GlobalConfig = Config{

		Token: TokenConfig{
			AccessSecret:  "asdflsadaqjwe123DEavlkjl12312312",
			RefreshSecret: "fadsf0ivoi@vlka0sd123,vk234/adsf;1!1231$$$#123",
			AccessTtl:     time.Hour * 1000000,
			RefreshTtl:    time.Hour * 1000000,
		},
		HTTP: HTTPConfig{
			Name: envs["HTTP-NAME"],
			Port: envs["HTTP-PORT"],
			Http: envs["HTTP-TYPE"],
		},
		Redis: RedisConfig{
			Addr:     envs["REDIS-ADDR"],
			Password: envs["REDIS-PASSWORD"],
		},
		TimeOut: tools.StrToBool(envs["TIME-OUT"]),
	}
}

func Get() *Config {
	return &GlobalConfig
}
