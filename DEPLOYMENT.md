# Deployment Guide

## 🚀 Deploying PlaySchool Management Backend

### Prerequisites
- ✅ PostgreSQL database (already configured)
- ✅ GitHub repository
- ✅ Cloud platform account (Render, Heroku, Railway, etc.)

## 📋 Deployment Checklist

### 1. **Security Setup** ✅
- [x] Environment variables configured
- [x] Credentials removed from code
- [x] .gitignore updated
- [x] JWT secret externalized

### 2. **GitHub Deployment**

#### Step 1: Push to GitHub
```bash
git add .
git commit -m "Initial commit - Spring Boot backend"
git push origin main
```

#### Step 2: Verify Build
- GitHub Actions will automatically build and test
- Check the Actions tab for build status

### 3. **Cloud Platform Deployment**

#### Option A: Render.com (Recommended)
1. Connect your GitHub repository
2. Use the `render.yaml` configuration
3. Set environment variables:
   ```
   DATABASE_URL=postgresql://root:dsyarysXiegZNRVVXX77fttA9bVmm3ew@dpg-d1p425bipnbc73fkdfa0-a/educare
   SPRING_PROFILES_ACTIVE=cloud
   JWT_SECRET=your-generated-secret
   ```

#### Option B: Heroku
1. Create new Heroku app
2. Connect GitHub repository
3. Set config vars (environment variables)
4. Deploy from main branch

#### Option C: Railway
1. Connect GitHub repository
2. Set environment variables
3. Deploy automatically

### 4. **Environment Variables to Set**

**Required:**
```
DATABASE_URL=postgresql://root:dsyarysXiegZNRVVXX77fttA9bVmm3ew@dpg-d1p425bipnbc73fkdfa0-a/educare
SPRING_PROFILES_ACTIVE=cloud
JWT_SECRET=your-64-character-secret-key
```

**Optional:**
```
PORT=8080
ALLOWED_ORIGINS=https://your-frontend-domain.com
```

### 5. **Post-Deployment Testing**

#### Test Endpoints:
```bash
# Health check
curl https://your-app.onrender.com/api/test/all

# Should return: "Public Content."
```

#### Test Database Connection:
- Check logs for database connection success message
- Look for: "✅ Database connection successful!"

### 6. **Common Issues & Solutions**

#### Issue: Build Fails
**Solution:** Check Java version in `system.properties`
```
java.runtime.version=17
```

#### Issue: Database Connection Fails
**Solution:** Verify environment variables are set correctly

#### Issue: CORS Errors
**Solution:** Update `ALLOWED_ORIGINS` with your frontend URL

### 7. **Production Checklist**

- [ ] Environment variables set
- [ ] Database connected
- [ ] JWT secret configured
- [ ] CORS origins configured
- [ ] Health endpoints working
- [ ] Logs show no errors

## 🔧 Quick Commands

### Local Testing:
```bash
# Test with cloud profile
mvn spring-boot:run -Dspring.profiles.active=cloud

# Build for deployment
mvn clean package -DskipTests
```

### Deployment URLs:
- **API Base:** `https://your-app.onrender.com/api`
- **Health Check:** `https://your-app.onrender.com/api/test/all`
- **Auth Endpoint:** `https://your-app.onrender.com/api/auth/signin`

## 📱 Frontend Integration

Update your frontend to use the deployed API:
```javascript
const API_BASE_URL = 'https://your-app.onrender.com/api';
```

Your backend is now ready for deployment! 🎉
