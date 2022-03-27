package services

import (
	"bytes"
	"fmt"
	"github.com/olzhas-b/PetService/backEnd/pkg/models"
	"github.com/olzhas-b/PetService/backEnd/pkg/repositories"
	"github.com/olzhas-b/PetService/backEnd/tools"
	"io"
	"mime/multipart"
	"strings"
)

type ImageService struct {
	repo *repositories.Repositories
}

func NewImageService(repo *repositories.Repositories) *ImageService {
	return &ImageService{repo: repo}
}

func (srv *ImageService) ServiceSaveImage(files []*multipart.FileHeader) (err error) {
	var images []models.Image
	for _, file := range files {
		newImage := models.Image{
			Name:        file.Filename,
			ContentType: file.Header.Get("Content-Type"),
		}
		func() {
			file, err := file.Open()
			if err != nil {
				return
			}
			// handle close error to prevent resources leak
			defer func() {
				closeErr := file.Close()
				if err == nil && closeErr != nil {
					err = closeErr
				}
			}()
			buf := bytes.NewBuffer(nil)
			if _, err = io.Copy(buf, file); err != nil {
				return
			}
			newImage.Content = buf.Bytes()
		}()

		images = append(images, newImage)
	}
	err = srv.repo.SaveImage(&images)
	if err != nil {
		return fmt.Errorf("ImageService.ServiceSaveImage got err: %w", err)
	}
	return err
}

func (srv *ImageService) ServiceGetImageByFileName(name string) (image models.Image, err error) {
	list := strings.Split(name, ".")
	if len(list) == 0 {
		return image, fmt.Errorf("ImageService.ServiceGetImageByFileName got err: %w", err)
	}
	return srv.repo.IImageRepository.GetImageByID(tools.StrToInt64(list[0]))
}
