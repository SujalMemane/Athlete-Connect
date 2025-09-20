# SIH Sports Assessment Platform - Frontend Starter
Write-Host "🏆 Starting SIH Sports Assessment Frontend..." -ForegroundColor Cyan
Write-Host ""

# Navigate to frontend directory
Set-Location "Frontend\digital-combine-frontend"

# Check if node_modules exists
if (-not (Test-Path "node_modules")) {
    Write-Host "📦 Installing dependencies..." -ForegroundColor Yellow
    npm install
}

# Start development server
Write-Host "🚀 Starting development server..." -ForegroundColor Green
Write-Host "🌐 Frontend will be available at: http://localhost:3000" -ForegroundColor Cyan
Write-Host ""
npm run dev
