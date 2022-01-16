package models

type ResponseError struct {
	Description          string `json:"description"`
	MessageFromDeveloper string `json:"messageFromDeveloper"`
}
