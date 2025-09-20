# 🏆 SIH Sports Assessment Platform - Frontend Guide

## 🚨 **Common Errors & Solutions**

### ❌ **Error 1: "Missing script: dev"**
**Problem**: Running `npm run dev` from wrong directory
**Solution**: Navigate to correct directory first

### ❌ **Error 2: PowerShell "&&" syntax error**
**Problem**: PowerShell doesn't support `&&` operator
**Solution**: Use separate commands or PowerShell scripts

## 🚀 **How to Start Frontend (3 Easy Ways)**

### **Method 1: Use Batch Script (Easiest)**
```bash
# Double-click this file or run in terminal:
start-frontend.bat
```

### **Method 2: Use PowerShell Script**
```powershell
# Run in PowerShell:
.\start-frontend.ps1
```

### **Method 3: Manual Commands**
```powershell
# Step 1: Navigate to frontend directory
cd Frontend\digital-combine-frontend

# Step 2: Start development server
npm run dev
```

## 📁 **Correct Directory Structure**

```
D:\Smart India Hakethon\
├── Frontend\
│   └── digital-combine-frontend\    ← Run npm commands HERE
│       ├── package.json
│       ├── src\
│       └── node_modules\
├── Python-Backend\
├── WOW-Features\
└── start-frontend.bat              ← Use this script
```

## ✅ **Verification Steps**

### **1. Check Current Directory**
```powershell
# Should show: D:\Smart India Hakethon\Frontend\digital-combine-frontend
pwd
```

### **2. Check Package.json Exists**
```powershell
# Should show package.json file
ls package.json
```

### **3. Check Scripts Available**
```powershell
# Should show available scripts including "dev"
npm run
```

## 🌐 **Access Your Frontend**

Once started successfully:
- **URL**: http://localhost:3000
- **Status**: Development server running
- **Features**: Hot reload, real-time updates

## 🔧 **Troubleshooting**

### **If Frontend Won't Start:**

1. **Check Node.js Version**
   ```powershell
   node --version  # Should be 18+
   npm --version   # Should be 8+
   ```

2. **Install Dependencies**
   ```powershell
   cd Frontend\digital-combine-frontend
   npm install
   ```

3. **Clear Cache**
   ```powershell
   npm cache clean --force
   rm -rf node_modules
   npm install
   ```

4. **Check Port Availability**
   ```powershell
   # If port 3000 is busy, Next.js will use 3001, 3002, etc.
   ```

### **Common PowerShell Issues:**

1. **Execution Policy Error**
   ```powershell
   Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
   ```

2. **Path Issues**
   ```powershell
   # Use backslashes for Windows paths
   cd Frontend\digital-combine-frontend
   ```

## 🎯 **Quick Commands Reference**

```powershell
# Navigate to project root
cd "D:\Smart India Hakethon"

# Navigate to frontend
cd Frontend\digital-combine-frontend

# Start development server
npm run dev

# Build for production
npm run build

# Install dependencies
npm install

# Check available scripts
npm run
```

## 🏆 **Success Indicators**

When frontend starts successfully, you'll see:
```
▲ Next.js 15.5.2
- Local:        http://localhost:3000
- ready started server on 0.0.0.0:3000
```

## 📱 **Frontend Features**

Your SIH Sports Assessment Platform includes:
- ✅ **Modern UI**: Dark theme with gradients
- ✅ **Exercise Selection**: 4 exercise types
- ✅ **WOW Features**: AI capabilities showcase
- ✅ **Responsive Design**: Works on all devices
- ✅ **Interactive Demo**: Modal system
- ✅ **Professional Branding**: SIH competition ready

---

**Your frontend is now properly configured and ready to run! 🚀**
