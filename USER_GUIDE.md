# Lanbulab User Guide

Complete guide for using the Lanbulab Android app for Bambu Lab 3D printers.

## Table of Contents
1. [Getting Started](#getting-started)
2. [Managing Printers](#managing-printers)
3. [Browsing Models](#browsing-models)
4. [Slicing Models](#slicing-models)
5. [Managing Print Jobs](#managing-print-jobs)
6. [Troubleshooting](#troubleshooting)

## Getting Started

### First Launch
When you first launch Lanbulab, you'll see three main tabs at the bottom:
- **Printers**: Manage your 3D printers
- **MakerWorld**: Browse and slice 3D models
- **Jobs**: View and manage print jobs

### Permissions
The app requires the following permissions:
- **Internet**: For downloading models
- **Network Access**: For discovering printers on your local network
- **Storage**: For saving sliced G-code files

## Managing Printers

### Adding a Printer Manually

1. Navigate to the **Printers** tab
2. Tap the **Add** button in the top right
3. Fill in the printer details:
   - **Printer Name**: A friendly name (e.g., "My X1 Carbon")
   - **IP Address**: Your printer's IP address (e.g., "192.168.1.100")
   - **Access Code**: (Optional) Your printer's access code
   - **Printer Model**: (Optional) Model name (e.g., "X1 Carbon")
4. Tap **Add Printer**

**Finding Your Printer's IP Address:**
- Check your printer's display screen
- Look in your router's DHCP client list
- Check your printer's network settings

### Discovering Printers on Network

1. Navigate to the **Printers** tab
2. Tap the **Scan** button
3. The app will search for Bambu Lab printers on your network
4. Found printers will be automatically added to your list

### Deleting a Printer

1. Navigate to the **Printers** tab
2. Long-press on the printer you want to delete
3. Select **Delete** from the menu
4. Confirm the deletion

### Printer Status Indicators

- **Green dot**: Printer is online
- **Gray dot**: Printer is offline

## Browsing Models

### Viewing Available Models

1. Navigate to the **MakerWorld** tab
2. Browse through the available models
3. Each model card shows:
   - Model name
   - Author
   - Number of likes (♥)
   - Number of downloads (↓)
   - **Slice** button

### Searching for Models

1. In the **MakerWorld** tab, enter a search term in the search box
2. Tap **Search**
3. Results will show models matching your search

**Search Tips:**
- Search by model name (e.g., "phone stand")
- Search by category (e.g., "office")
- Search by type (e.g., "organizer")

### Refreshing the Model List

- Tap the **Refresh** button to reload the model list

## Slicing Models

### How to Slice a Model

1. Navigate to the **MakerWorld** tab
2. Find a model you want to print
3. Tap the **Slice** button on the model card
4. The Slice Settings dialog will appear

### Configuring Slice Settings

**Basic Settings:**
- **Layer Height**: Vertical resolution (0.1-0.3mm recommended)
  - Lower = better quality, longer print time
  - Higher = faster print, lower quality
  - Default: 0.2mm

- **Infill Density**: Internal structure percentage (0-100%)
  - 15-20% for decorative items
  - 50-100% for functional parts
  - Default: 15%

- **Print Speed**: Overall speed percentage (50-150%)
  - Lower = better quality
  - Higher = faster prints
  - Default: 100%

**Filament Settings:**
- **Filament Type**: Choose from:
  - **PLA**: Easy to print, good for most items (220°C / 60°C)
  - **ABS**: Strong, heat-resistant (240°C / 100°C)
  - **PETG**: Durable, flexible (230°C / 80°C)
  - **TPU**: Flexible filament

*Note: Temperatures auto-adjust when you change filament type*

**Advanced Settings:**
- **Enable Supports**: Check for overhanging features
- **Bed Temperature**: Heating bed temperature
- **Nozzle Temperature**: Hot end temperature

### Starting the Slice

1. Configure your settings
2. Tap **Slice Model**
3. The app will:
   - Download the model (if needed)
   - Generate G-code with your settings
   - Create a print job automatically
4. Check the **Jobs** tab to see your new print job

## Managing Print Jobs

### Viewing Print Jobs

1. Navigate to the **Jobs** tab
2. All your print jobs are listed here
3. Jobs are color-coded by status:
   - **Orange**: Pending (ready to send)
   - **Blue**: Uploading
   - **Green**: Uploaded/Printing/Completed
   - **Red**: Failed
   - **Gray**: Cancelled

### Sending a Job to Printer

**Method 1: Using the Send Button**
1. Find a **PENDING** job
2. Tap the **Send to Printer** button
3. Select a printer from the list
4. Job will be uploaded to the printer

**Method 2: Tapping the Job**
1. Tap on any **PENDING** job
2. Select a printer from the list
3. Job will be uploaded to the printer

### Cancelling a Job

1. Find an active job (Pending, Uploading, or Printing)
2. Tap the **Cancel** button
3. Confirm cancellation
4. Job status will change to Cancelled

### Deleting a Job

1. Long-press on any job
2. Confirm deletion
3. Job will be removed from history

## Troubleshooting

### Can't Find Printers on Network

**Solutions:**
- Ensure your phone and printer are on the same WiFi network
- Check that your printer is powered on
- Try manually adding the printer using its IP address
- Check your router's firewall settings

### Can't Add Printer Manually

**Common Issues:**
- **Invalid IP Address**: Make sure format is correct (e.g., 192.168.1.100)
- **Printer Name Required**: Name field cannot be empty
- **Network Unreachable**: Verify the IP address is correct

### Slicing Takes Too Long

**Tips:**
- The app generates simplified G-code for demonstration
- Actual slice time is usually under 5 seconds
- If it hangs, try:
  - Closing and reopening the dialog
  - Restarting the app

### Job Won't Send to Printer

**Solutions:**
- Verify the printer is online (green indicator)
- Check the printer's IP address is correct
- Ensure the G-code file exists (try re-slicing)
- Check network connectivity

### App Crashes or Freezes

**Solutions:**
- Clear app data in Android settings
- Reinstall the app
- Check available storage space
- Ensure Android version is 7.0 or higher

## Tips and Best Practices

### Organizing Printers
- Use descriptive names (e.g., "Office X1" vs "Workshop P1P")
- Keep IP addresses up-to-date
- Remove offline printers you no longer use

### Slicing Recommendations
- Start with default settings for new models
- Use PLA for testing and prototypes
- Enable supports for overhangs > 45 degrees
- Lower layer height for detailed models

### Managing Jobs
- Delete completed jobs regularly
- Keep pending jobs for quick re-printing
- Note which settings worked well for each model

## Advanced Features

### Storage Locations

**Model Files:**
- Located in: `{App Storage}/files/models/`
- Format: .3mf files

**Sliced Files:**
- Located in: `{App Storage}/files/sliced/`
- Format: .gcode files

### G-code File Format

Generated G-code files include:
- Header with slice settings
- Start G-code (homing, heating, priming)
- Main print layers
- End G-code (cooling, parking)

### Data Persistence

All data is saved locally:
- Printers: SharedPreferences
- Print Jobs: SharedPreferences
- Files: App-specific storage

**To Reset All Data:**
1. Go to Android Settings
2. Apps → Lanbulab
3. Storage → Clear Data

## Support

For issues, feature requests, or questions:
- Check the [README.md](README.md) for technical details
- Review [IMPLEMENTATION.md](IMPLEMENTATION.md) for architecture
- Create an issue on GitHub

## Appendix

### Filament Temperature Guide

| Filament | Nozzle | Bed | Notes |
|----------|--------|-----|-------|
| PLA | 200-220°C | 50-60°C | Easiest, good adhesion |
| ABS | 230-250°C | 90-110°C | Needs ventilation |
| PETG | 220-250°C | 70-90°C | Durable, flexible |
| TPU | 220-235°C | 30-60°C | Very flexible |

### Common Print Issues

| Problem | Possible Cause | Solution |
|---------|----------------|----------|
| Warping | Bed too cold | Increase bed temp |
| Stringing | Nozzle too hot | Decrease nozzle temp |
| Poor adhesion | Bed too cold | Increase bed temp |
| Layer separation | Nozzle too cold | Increase nozzle temp |

---

**Version:** 1.0  
**Last Updated:** December 2024
