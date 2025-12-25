# Slice Functionality Documentation

## Overview
The app includes integrated slicing functionality that allows users to slice 3D models directly from the MakerWorld library without needing external software.

## Features

### 1. Slice Button on MakerWorld Models
Every model in the MakerWorld browser includes a **"Slice"** button that opens the slice settings dialog.

Location: `item_makerworld_model.xml`
```xml
<Button
    android:id="@+id/slice_button"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="Slice" />
```

### 2. Slice Settings Dialog
When the slice button is clicked, a comprehensive settings dialog appears with the following options:

#### Basic Settings
- **Layer Height**: Controls the vertical resolution of the print (default: 0.2mm)
- **Infill Density**: Percentage of internal structure (default: 15%)
- **Print Speed**: Overall print speed percentage (default: 100%)

#### Temperature Settings
- **Filament Type**: Dropdown selector for PLA, ABS, PETG, TPU
  - Automatically adjusts temperatures when changed
- **Bed Temperature**: Heated bed temperature in Celsius
- **Nozzle Temperature**: Hot end temperature in Celsius

#### Advanced Settings
- **Enable Supports**: Checkbox to add support structures

### 3. Workflow

1. **Browse MakerWorld**: User browses models in the MakerWorld tab
2. **Select Model**: User sees a model they want to print
3. **Click Slice**: User clicks the "Slice" button on the model card
4. **Configure Settings**: Dialog opens with slicing parameters
5. **Download**: If not already downloaded, the app downloads the model
6. **Slice**: App processes the model with selected settings
7. **Ready to Print**: Sliced file is saved and ready to send to printer

### 4. Code Structure

#### MakerWorldAdapter.kt
Handles the slice button click:
```kotlin
binding.sliceButton.setOnClickListener {
    onSliceClick(model)
}
```

#### MakerWorldFragment.kt
Shows the slice dialog:
```kotlin
private fun showSliceDialog(model: MakerWorldModel) {
    val dialog = SliceDialogFragment.newInstance(model)
    dialog.show(childFragmentManager, "slice_dialog")
}
```

#### SliceDialogFragment.kt
Main slice dialog implementation:
- Displays slice settings UI
- Downloads model if needed
- Calls slicing service
- Shows progress indicators
- Handles errors gracefully

#### SlicingService.kt
Backend slicing implementation:
- `sliceModel()`: Main slicing function
- `canSliceFile()`: Validates file format
- `getDefaultSettings()`: Provides filament-specific defaults

### 5. Supported File Formats
- 3MF (primary format for Bambu Lab)
- STL
- OBJ

### 6. Future Enhancements
The current implementation provides the structure for:
- Integration with Bambu Lab's cloud slicer API
- Local slicing engine integration
- Custom profile management
- Advanced settings (infill patterns, wall thickness, etc.)
- Preview generation
- Print time estimation

### 7. User Experience Flow

```
MakerWorld Browser
    ↓
[Model Card with Slice Button]
    ↓
Click Slice
    ↓
[Slice Settings Dialog]
    ↓
Configure Parameters
    ↓
Click "Slice Model"
    ↓
Download Model (if needed)
    ↓
Slice Processing
    ↓
Success! File Ready
    ↓
Can Send to Printer
```

## Technical Notes

### Model Download
Models are downloaded to the device's external files directory:
```kotlin
val downloadDir = requireContext().getExternalFilesDir("models")
```

### Sliced Files
Output files are saved to:
```kotlin
val outputDir = requireContext().getExternalFilesDir("sliced")
```

### Temperature Presets
The app includes smart temperature presets for common filament types:
- **PLA**: 220°C nozzle, 60°C bed
- **ABS**: 240°C nozzle, 100°C bed
- **PETG**: 230°C nozzle, 80°C bed

These automatically update when the user changes filament type in the dialog.

## Testing the Slice Feature

1. Open the app
2. Navigate to the "MakerWorld" tab
3. Browse models (sample data will be shown)
4. Click the "Slice" button on any model
5. Adjust settings as desired
6. Click "Slice Model" to process

The implementation currently uses placeholder slicing logic but provides the complete UI/UX flow and integration points for real slicing functionality.
