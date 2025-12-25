# Lanbulab

Android app for Bambu Lab LAN-only printers with integrated slicing and MakerWorld library access.

## Features

- **LAN Printer Discovery**: Automatically discover Bambu Lab printers on your local network
- **Printer Management**: Add, monitor, and control your printers
- **MakerWorld Integration**: Browse and download models from MakerWorld library
- **Integrated Slicing**: Slice models directly from MakerWorld with customizable settings
- **Print Job Management**: Track and manage your print jobs
- **Send to Printer**: Upload sliced files and start prints directly from the app

## Key Components

### Printer Management
- Automatic network discovery of Bambu Lab printers
- Real-time printer status monitoring
- Support for multiple printers

### MakerWorld Browser
- Browse popular models from MakerWorld
- Search functionality
- Direct download of 3D models
- **Slice button on each model** for quick slicing

### Slicing Engine
- Integrated slicing with customizable settings
- Support for different filament types (PLA, ABS, PETG, TPU)
- Adjustable layer height, infill, print speed
- Temperature control
- Support generation options

### Print Jobs
- Queue management
- Job status tracking
- Cancel running prints

## Requirements

- Android 7.0 (API 24) or higher
- Local network connection with Bambu Lab printer
- Internet connection for MakerWorld access

## Building

```bash
./gradlew assembleDebug
```

## Installation

Install the APK on your Android device:
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```
