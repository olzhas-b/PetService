package tools

import "strconv"

func StrToInt64(sNum string) int64 {
	num, _ := strconv.Atoi(sNum)
	return int64(num)
}
