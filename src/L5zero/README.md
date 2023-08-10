# Overview

This project target is the Localizer Tool for creating Java, JS, etc.
localization bundles based on Excel definition using Dataferra doc2data
service (d2.work)

# Instruction
____

## Manual usage

### Run "L5zero.sh"
Open a linux terminal or use WSL in the project directory.
Type "./L5zero.sh" to run the flow script.

### Specify the path to the localization excel file
Absolute and relative paths are supported.

### Choose the output format
Supported formats:
- Java
- Swift
- JSON
- ReactI18n

### Check the result
Specify the output folder. Extensions of localization files:
 - ".properties" for Java
 - ".strings" for Swift
 - ".json" for JSON and ReactI18n

## Automatical usage

You can execute the script during project build. To do this, add it to the list of "prebuild" tasks. 
The command must include the following parameters:
`./L5zero.sh path_to_excel_file output_type output_path`
