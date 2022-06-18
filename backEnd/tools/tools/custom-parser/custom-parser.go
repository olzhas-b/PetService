package custom_parser

import (
	"encoding/json"
	"errors"
	"fmt"
	"github.com/gofiber/fiber/v2"
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"mime/multipart"
)

func PetParser(ctx *fiber.Ctx) (pet models.Pet, file *multipart.FileHeader, err error) {
	form, err := ctx.MultipartForm()
	if err != nil && form == nil {
		return pet, file, fmt.Errorf("not enough data to create, got error %w", err)
	}

	body := form.Value["body"]
	if len(body) == 0 {
		return pet, file, errors.New("not enough data to create")
	}
	if err := json.Unmarshal([]byte(body[0]), &pet); err != nil {
		return pet, file, errors.New("не смогли запарсить")

	}

	if form != nil {
		if val, ok := form.File["image"]; ok && len(val) != 0 {
			file = form.File["image"][0]
		}
	}
	return
}
