package models

type TokenClaim struct {
	ID       int64
	UserType int64
	Exp      int64
	Iat      int64
}
