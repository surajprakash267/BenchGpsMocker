# BenchGpsMocker 🚗📍

BenchGpsMocker is an Android-based GPS and speed mocking tool designed for
bench-level testing of telematics devices, dashcams, and ADAS/DMS systems
without real vehicle movement.

## 🔧 Features
- Mock GPS latitude & longitude
- Mock vehicle speed (km/h → m/s conversion)
- Real-time speed control using SeekBar
- Start / Stop mock location service
- Designed for Android 9 (Dashcam / Embedded devices)
- Suitable for bench testing, QA validation, and R&D

## 📱 Use Cases
- Telematics device testing on bench
- ADAS / DMS algorithm validation
- Speed-based alert simulation
- GPS-dependent feature testing without driving

## 🛠 Tech Stack
- Android (Kotlin)
- Foreground Service (Mock Location)
- Android Location APIs
- Tested on Dashcam hardware (Android 9)

## 🚀 How to Use
1. Enable **Mock Location App** in Developer Options
2. Install BenchGpsMocker APK
3. Enter latitude & longitude
4. Adjust speed using the slider
5. Start mock → device receives simulated movement

## 📌 Note
This tool is intended for testing and validation purposes only.

