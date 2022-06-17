package db

import (
	"fmt"
	"github.com/joho/godotenv"
	"gorm.io/driver/postgres"
	"gorm.io/gorm"
	"log"
)

func InitPostgres() (*gorm.DB, error) {
	env, err := godotenv.Read(".env")
	if err != nil {
		return nil, err
	}
	config := fmt.Sprintf("host=%s user=%s password=%s dbname=%s sslmode=disable port=%s", env["HOST"], env["USER"], env["PASSWORD"], env["NAME"], env["DBPORT"])
	log.Println(config)
	return gorm.Open(postgres.Open(config), &gorm.Config{})
}
