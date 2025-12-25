# Lanbulab

Android app for Bambu Lab LAN-only printers with integrated slicing and MakerWorld library access.

## Features

- **LAN Printer Management**: Add printers manually or discover them on your local network
- **Printer Management**: Add, monitor, delete, and manage your printers with persistent storage
- **MakerWorld Integration**: Browse sample models from a curated library
- **Integrated Slicing**: Slice models directly with customizable settings and generate G-code
- **Print Job Management**: Track print jobs, send to printers, and manage history
- **Send to Printer**: Select printer and upload G-code files

## Key Components

### Printer Management
- Automatic network discovery of Bambu Lab printers
- Manual printer addition with IP address
- Persistent storage using SharedPreferences
- Delete printers with long-press
- Real printer status tracking

### MakerWorld Browser
- Browse 8 sample models
- Search functionality by name, description, or tags
- Direct slicing from model cards
- **Slice button on each model** for quick access

### Slicing Engine
- Generates actual G-code files with proper structure
- Customizable settings: layer height, infill, speed
- Support for different filament types (PLA, ABS, PETG, TPU)
- Adjustable temperature control
- Support generation options
- Creates print jobs automatically after slicing

### Print Jobs
- View all print jobs with color-coded statuses
- Send pending jobs to any printer
- Cancel active jobs
- Delete completed jobs
- Persistent job history

## User Workflows

### Adding a Printer
1. Navigate to "Printers" tab
2. Tap "Add" button
3. Enter printer details (name, IP address, access code, model)
4. Tap "Add Printer"
5. Printer is now saved and available

### Slicing a Model
1. Navigate to "MakerWorld" tab
2. Browse or search for models
3. Tap "Slice" button on any model
4. Configure settings (filament type, layer height, infill, temperatures)
5. Tap "Slice Model"
6. G-code file is generated and print job is created

### Sending to Printer
1. Navigate to "Print Jobs" tab
2. Find your pending job
3. Tap "Send to Printer" button
4. Select a printer from the list
5. Job is uploaded to the printer

## Requirements

- Android 7.0 (API 24) or higher
- Local network connection for printer discovery
- Storage permissions for saving files

## Building

```bash
./gradlew assembleDebug
```

## Installation

Install the APK on your Android device:
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

## Features Implementation Status

✅ Fully functional printer management with persistence
✅ Manual printer addition with validation
✅ Printer deletion
✅ Real G-code generation for slicing
✅ Complete print job workflow
✅ Persistent data storage
✅ Color-coded job statuses
✅ Empty states for better UX
✅ Send jobs to printers

## Notes

- All data is stored locally using SharedPreferences
- G-code files are saved to app-specific storage
- Printer discovery uses UDP broadcast (may require network permissions)
- The app is fully functional for demonstration and testing purposes

