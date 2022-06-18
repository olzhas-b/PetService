package tools

import (
	"strconv"
	"time"
)

func StrToInt64(sNum string) int64 {
	num, _ := strconv.Atoi(sNum)
	return int64(num)
}

func StrToInt(sNum string) int {
	num, _ := strconv.Atoi(sNum)
	return num
}

func IntToStr(num int) string {
	return strconv.Itoa(num)
}

func Int64ToStr(num int64) string {
	return strconv.FormatInt(num, 10)
}

func StrToBool(str string) bool {
	if str == "true" || str == "True" || str == "TRUE" {
		return true
	}
	return false
}

func StrToTime(s string) time.Time {
	fTime, _ := time.Parse("02.01.2006", s)
	return fTime
}

// EndOfDay returns time at the end of the day t
func EndOfDay(t time.Time) time.Time {
	y, m, d := t.Year(), t.Month(), t.Day()
	return time.Date(y, m, d, 23, 59, 59, 999999999, t.Location())
}

// StartOfDay returns time at the start of the day t
func StartOfDay(t time.Time) time.Time {
	y, m, d := t.Year(), t.Month(), t.Day()
	return time.Date(y, m, d, 0, 0, 0, 0, t.Location())
}

func SliceToMapInt64(slice []int64) map[int64]bool {
	m := make(map[int64]bool)
	for _, val := range slice {
		m[val] = true
	}
	return m
}
