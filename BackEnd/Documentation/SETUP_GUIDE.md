# 🚀 Complete Setup Guide - SIH Sports Assessment Platform

This comprehensive guide will help you set up the entire SIH Sports Assessment Platform on your system.

## 📋 Table of Contents

1. [System Requirements](#system-requirements)
2. [Pre-installation Checklist](#pre-installation-checklist)
3. [Backend Setup (Python)](#backend-setup-python)
4. [Frontend Setup (Next.js)](#frontend-setup-nextjs)
5. [WOW Features Setup](#wow-features-setup)
6. [Testing Installation](#testing-installation)
7. [Troubleshooting](#troubleshooting)
8. [Performance Optimization](#performance-optimization)

## 💻 System Requirements

### Minimum Requirements
- **OS:** Windows 10/11, macOS 10.15+, Ubuntu 18.04+
- **RAM:** 4GB (8GB recommended)
- **Storage:** 2GB free space
- **Camera:** Built-in webcam or USB camera
- **Internet:** For initial setup and dependencies

### Recommended Specifications
- **OS:** Windows 11, macOS 12+, Ubuntu 20.04+
- **RAM:** 8GB+ for optimal performance
- **CPU:** Multi-core processor (Intel i5/AMD Ryzen 5 or better)
- **GPU:** Optional - NVIDIA GPU for enhanced performance
- **Camera:** HD webcam (1080p) for best results

### Software Prerequisites
- **Python:** 3.11.9 or higher
- **Node.js:** 18.0 or higher
- **npm:** 9.0 or higher (comes with Node.js)
- **Git:** For cloning and version control

## ✅ Pre-installation Checklist

### 1. Check Python Version
```bash
python --version
# Should show Python 3.11.9 or higher
```

### 2. Check Node.js Version
```bash
node --version
# Should show v18.0.0 or higher

npm --version
# Should show 9.0.0 or higher
```

### 3. Check Camera Access
- Ensure your camera is working
- Test with default camera app
- Check camera permissions

### 4. Create Project Directory
```bash
mkdir SIH-Sports-Platform
cd SIH-Sports-Platform
```

## 🐍 Backend Setup (Python)

### Step 1: Navigate to Python Backend
```bash
cd Python-Backend
```

### Step 2: Create Virtual Environment (Recommended)
```bash
# Windows
python -m venv venv
venv\Scripts\activate

# macOS/Linux
python3 -m venv venv
source venv/bin/activate
```

### Step 3: Install Dependencies
```bash
# Install all required packages
pip install -r requirements.txt

# If you encounter issues, install individually:
pip install opencv-python==4.8.1.78
pip install mediapipe==0.10.7
pip install numpy==1.24.3
pip install scipy==1.11.4
pip install scikit-learn==1.3.2
pip install pandas==2.1.4
pip install matplotlib==3.8.2
pip install plotly==5.17.0
pip install pyttsx3==2.90
pip install SpeechRecognition==3.10.0
pip install requests==2.31.0
pip install flask==3.0.0
pip install flask-cors==4.0.0
pip install pillow==10.1.0
```

### Step 4: Verify Installation
```bash
# Test core pose detection
python pose.py

# Test rep counter
python improved_rep_counter.py

# Test cheat detection
python cheat_detection_system.py
```

### Step 5: Test Main Demo
```bash
# Run the complete sports assessment demo
python sports_assessment_demo.py
```

## ⚛️ Frontend Setup (Next.js)

### Step 1: Navigate to Frontend
```bash
cd Frontend/digital-combine-frontend
```

### Step 2: Install Dependencies
```bash
# Install all Node.js packages
npm install

# If you encounter issues, try:
npm install --legacy-peer-deps

# Or install specific packages:
npm install next@latest react@latest react-dom@latest
npm install typescript @types/node @types/react @types/react-dom
npm install tailwindcss postcss autoprefixer
npm install lucide-react axios
```

### Step 3: Verify Installation
```bash
# Check if all packages are installed
npm list

# Start development server
npm run dev
```

### Step 4: Open in Browser
- Open your browser and go to `http://localhost:3000`
- You should see the SIH Sports Assessment Platform homepage

## 🌟 WOW Features Setup

### Step 1: Navigate to WOW Features
```bash
cd WOW-Features
```

### Step 2: Install Additional Dependencies
```bash
# These should already be installed from backend setup
pip install joblib
pip install scipy

# For TTS features (optional)
pip install pyttsx3
pip install pyaudio  # May require additional setup on some systems
```

### Step 3: Test Individual Features

#### AI Voice Coach
```bash
python ai_voice_coach.py
```

#### Talent Predictor
```bash
python talent_predictor.py
```

#### Olympic Predictor
```bash
python olympic_predictor.py
```

#### Integrated System
```bash
python integrated_wow_system.py
```

## 🧪 Testing Installation

### Complete System Test
```bash
# From project root, test all components
python Python-Backend/sports_assessment_demo.py
```

### Frontend Test
```bash
# In Frontend/digital-combine-frontend/
npm run dev
# Visit http://localhost:3000
```

### Individual Component Tests
```bash
# Test pose detection
python Python-Backend/pose.py

# Test rep counting
python Python-Backend/improved_rep_counter.py

# Test cheat detection
python Python-Backend/cheat_detection_system.py

# Test WOW features
python WOW-Features/integrated_wow_system.py
```

## 🔧 Troubleshooting

### Common Issues and Solutions

#### 1. Camera Not Working
**Problem:** Camera access denied or not detected
**Solutions:**
- Check camera permissions in system settings
- Ensure camera is not being used by another application
- Try different camera index in code (0, 1, 2, etc.)
- Restart the application

#### 2. Import Errors
**Problem:** ModuleNotFoundError for various packages
**Solutions:**
```bash
# Reinstall specific package
pip uninstall [package-name]
pip install [package-name]

# Check Python path
python -c "import sys; print(sys.path)"

# Ensure virtual environment is activated
source venv/bin/activate  # macOS/Linux
venv\Scripts\activate     # Windows
```

#### 3. Performance Issues
**Problem:** Low FPS or laggy performance
**Solutions:**
- Close unnecessary applications
- Reduce camera resolution
- Update graphics drivers
- Use GPU acceleration if available

#### 4. Audio/TTS Issues
**Problem:** Voice coaching not working
**Solutions:**
```bash
# Install/reinstall TTS dependencies
pip uninstall pyttsx3
pip install pyttsx3

# Check audio devices
python -c "import pyttsx3; engine = pyttsx3.init(); print('TTS working')"
```

#### 5. Frontend Build Issues
**Problem:** npm install fails or build errors
**Solutions:**
```bash
# Clear npm cache
npm cache clean --force

# Delete node_modules and reinstall
rm -rf node_modules package-lock.json
npm install

# Use different registry if needed
npm install --registry https://registry.npmjs.org/
```

### Platform-Specific Issues

#### Windows
- Install Visual Studio Build Tools if compilation errors occur
- Ensure Windows Defender doesn't block Python/Node.js
- Use PowerShell as administrator if permission issues

#### macOS
- Install Xcode Command Line Tools: `xcode-select --install`
- Use Homebrew for Python/Node.js installation
- Check camera permissions in System Preferences

#### Linux
- Install system dependencies:
```bash
sudo apt-get update
sudo apt-get install python3-dev python3-pip nodejs npm
sudo apt-get install libgl1-mesa-glx libglib2.0-0 libsm6 libxext6 libxrender-dev libgomp1
```

## ⚡ Performance Optimization

### Python Backend Optimization

#### 1. Enable GPU Acceleration (Optional)
```bash
# Install CUDA version of OpenCV if NVIDIA GPU available
pip uninstall opencv-python
pip install opencv-contrib-python-headless
```

#### 2. Optimize Camera Settings
```python
# In pose.py, adjust camera properties
cap.set(cv2.CAP_PROP_FRAME_WIDTH, 640)   # Reduce resolution for better FPS
cap.set(cv2.CAP_PROP_FRAME_HEIGHT, 480)
cap.set(cv2.CAP_PROP_FPS, 30)
```

#### 3. Reduce Processing Load
```python
# In pose detection, reduce model complexity
pose = mp_pose.Pose(
    model_complexity=0,  # Use lighter model
    min_detection_confidence=0.7,  # Increase threshold
    min_tracking_confidence=0.7
)
```

### Frontend Optimization

#### 1. Production Build
```bash
# Create optimized production build
npm run build
npm start
```

#### 2. Enable Caching
```bash
# Use service worker for caching
npm install next-pwa
```

### System-Level Optimization

#### 1. Close Unnecessary Applications
- Close browser tabs and other applications
- Disable background processes
- Free up RAM and CPU resources

#### 2. Camera Optimization
- Use good lighting conditions
- Position camera at appropriate distance
- Ensure stable camera mounting

## 🚀 Advanced Setup

### Docker Setup (Optional)
```dockerfile
# Dockerfile for backend
FROM python:3.11-slim

WORKDIR /app
COPY requirements.txt .
RUN pip install -r requirements.txt

COPY . .
CMD ["python", "sports_assessment_demo.py"]
```

### Cloud Deployment (Optional)
- AWS EC2 for backend processing
- Vercel/Netlify for frontend hosting
- CloudFront for global CDN

## ✅ Verification Checklist

After completing setup, verify these components work:

- [ ] Python backend starts without errors
- [ ] Camera feed displays correctly
- [ ] Pose detection shows skeleton overlay
- [ ] Rep counting works for basic exercises
- [ ] Form analysis detects violations
- [ ] Frontend loads at http://localhost:3000
- [ ] WOW features (if enabled) provide coaching
- [ ] Voice output works (if TTS enabled)
- [ ] All exercise types are selectable
- [ ] Demo mode functions properly

## 📞 Getting Help

If you encounter issues not covered in this guide:

1. **Check Error Messages:** Read the complete error output
2. **Verify Prerequisites:** Ensure all requirements are met
3. **Update Dependencies:** Try updating to latest versions
4. **Search Documentation:** Check other documentation files
5. **Community Support:** Reach out to the development team

## 🎉 Success!

Once setup is complete, you'll have:
- ✅ Fully functional sports assessment platform
- ✅ Real-time AI-powered analysis
- ✅ Multi-exercise support
- ✅ Advanced WOW features (if enabled)
- ✅ Professional-grade sports evaluation system

**Congratulations! Your SIH Sports Assessment Platform is ready to transform athletic talent identification!** 🏆

---

*For additional help, refer to the main README.md or other documentation files in the `/Documentation/` folder.*



