package tools

import (
	"bytes"
	"io"
	"mime/multipart"
	"time"
)

func ReadProperly(fileHeader *multipart.FileHeader) []byte {
	if fileHeader == nil {
		return []byte{}
	}
	file, err := fileHeader.Open()
	if err != nil {
		return []byte{}
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
		return []byte{}
	}
	return buf.Bytes()
}

func GetCurrentTimePtr() *time.Time {
	timeNow := time.Now()
	return &timeNow
}
