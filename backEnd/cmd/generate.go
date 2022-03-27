package cmd

import (
	"github.com/spf13/cobra"
)

var generateCmd = &cobra.Command{
	Use:   "generate",
	Short: "generate project from template",
	Run: func(cmd *cobra.Command, args []string) {
		println("hello from gorm")
	},
}
