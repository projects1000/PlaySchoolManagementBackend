# 🔐 Authentication Guide for PlaySchool Management API

## 🚨 The Issue
You're getting a 401 Unauthorized error because the `/api/students` endpoint requires authentication.

## 🧪 **Test Endpoints (No Authentication Required)**

Try these public endpoints first to verify your API is working:

### **1. Public Student Endpoints:**
```bash
# Test student API
GET https://playschoolmanagementbackend.onrender.com/api/students/public/test

# Get student count
GET https://playschoolmanagementbackend.onrender.com/api/students/public/count
```

### **2. Home Endpoints:**
```bash
# API welcome
GET https://playschoolmanagementbackend.onrender.com/api/home/

# Health check
GET https://playschoolmanagementbackend.onrender.com/api/home/health
```

### **3. Test Endpoints:**
```bash
# Public content
GET https://playschoolmanagementbackend.onrender.com/api/test/all
```

## 🔑 **How to Authenticate (For Protected Endpoints)**

### **Step 1: Create an Admin User**
```bash
curl -X POST https://playschoolmanagementbackend.onrender.com/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "email": "admin@playschool.com",
    "password": "Admin123456",
    "firstName": "Admin",
    "lastName": "User",
    "role": ["admin"]
  }'
```

### **Step 2: Login to Get JWT Token**
```bash
curl -X POST https://playschoolmanagementbackend.onrender.com/api/auth/signin \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "Admin123456"
  }'
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "admin",
  "email": "admin@playschool.com",
  "firstName": "Admin",
  "lastName": "User",
  "roles": ["ROLE_ADMIN"]
}
```

### **Step 3: Use Token in Headers**
```bash
curl -X GET https://playschoolmanagementbackend.onrender.com/api/students \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"
```

## 🌐 **Frontend JavaScript Example**

```javascript
// 1. Login
const loginResponse = await fetch('https://playschoolmanagementbackend.onrender.com/api/auth/signin', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    username: 'admin',
    password: 'Admin123456'
  })
});

const loginData = await loginResponse.json();
const token = loginData.token;

// 2. Use token for protected endpoints
const studentsResponse = await fetch('https://playschoolmanagementbackend.onrender.com/api/students', {
  method: 'GET',
  headers: {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  }
});

const students = await studentsResponse.json();
console.log(students);
```

## 📋 **Available Endpoints**

### **🟢 Public (No Auth Required):**
- `GET /api/test/all` - Test endpoint
- `GET /api/home/` - API info
- `GET /api/home/health` - Health check
- `GET /api/students/public/test` - Student API test
- `GET /api/students/public/count` - Student count
- `POST /api/auth/signup` - Create account
- `POST /api/auth/signin` - Login

### **🔒 Protected (Auth Required):**
- `GET /api/students` - Get all students (ADMIN/TEACHER/STAFF)
- `POST /api/students/register` - Register student (ADMIN/STAFF)
- `PUT /api/students/{id}` - Update student (ADMIN/STAFF)
- `DELETE /api/students/{id}` - Deactivate student (ADMIN only)

## 🚀 **Quick Test Sequence**

1. **Test Public Endpoint:**
   ```bash
   curl https://playschoolmanagementbackend.onrender.com/api/students/public/test
   ```

2. **Create Admin User:**
   ```bash
   curl -X POST https://playschoolmanagementbackend.onrender.com/api/auth/signup \
     -H "Content-Type: application/json" \
     -d '{"username":"admin","email":"admin@test.com","password":"Admin123456","firstName":"Admin","lastName":"User","role":["admin"]}'
   ```

3. **Login:**
   ```bash
   curl -X POST https://playschoolmanagementbackend.onrender.com/api/auth/signin \
     -H "Content-Type: application/json" \
     -d '{"username":"admin","password":"Admin123456"}'
   ```

4. **Use Token:**
   ```bash
   curl -H "Authorization: Bearer YOUR_TOKEN" https://playschoolmanagementbackend.onrender.com/api/students
   ```

Your API is working perfectly! You just need to authenticate for protected endpoints. 🎉

## 🔧 **Frontend Integration Fix**

### **Current Error Analysis:**
Your frontend is calling protected endpoints without JWT tokens:
- ❌ `/api/students` (requires auth)
- ❌ `/api/students/register` (requires auth)
- ❌ `/students` (404 - wrong path)

### **Quick Fix - Use Public Endpoints:**

```typescript
// In your Angular service, replace protected calls with public ones:

// OLD (protected - causes 401):
getStudentCount() {
  return this.http.get(`${this.baseUrl}/api/students`);
}

// NEW (public - works without auth):
getStudentCount() {
  return this.http.get(`${this.baseUrl}/api/students/public/count`);
}

// Test API connection
testConnection() {
  return this.http.get(`${this.baseUrl}/api/students/public/test`);
}
```

### **Complete Frontend Service Example:**
```typescript
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class StudentService {
  private baseUrl = 'https://playschoolmanagementbackend.onrender.com';
  private token: string | null = null;

  constructor(private http: HttpClient) {}

  // PUBLIC ENDPOINTS (No auth required)
  getStudentCount(): Observable<any> {
    return this.http.get(`${this.baseUrl}/api/students/public/count`);
  }

  testApi(): Observable<any> {
    return this.http.get(`${this.baseUrl}/api/students/public/test`);
  }

  // AUTHENTICATION
  login(credentials: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/api/auth/signin`, credentials);
  }

  setToken(token: string) {
    this.token = token;
    localStorage.setItem('jwt_token', token);
  }

  getToken(): string | null {
    if (!this.token) {
      this.token = localStorage.getItem('jwt_token');
    }
    return this.token;
  }

  // PROTECTED ENDPOINTS (Auth required)
  private getAuthHeaders(): HttpHeaders {
    const token = this.getToken();
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  getStudents(): Observable<any> {
    return this.http.get(`${this.baseUrl}/api/students`, {
      headers: this.getAuthHeaders()
    });
  }

  registerStudent(student: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/api/students/register`, student, {
      headers: this.getAuthHeaders()
    });
  }
}
```

### **Component Usage:**
```typescript
// In your component
export class StudentComponent implements OnInit {
  studentCount: number = 0;
  students: any[] = [];
  isAuthenticated: boolean = false;

  constructor(private studentService: StudentService) {}

  ngOnInit() {
    // Test public endpoint first
    this.loadPublicData();
    
    // Check if user is authenticated
    if (this.studentService.getToken()) {
      this.isAuthenticated = true;
      this.loadProtectedData();
    }
  }

  loadPublicData() {
    // This works without authentication
    this.studentService.getStudentCount().subscribe(
      data => {
        this.studentCount = data.count || data;
        console.log('Student count:', this.studentCount);
      },
      error => console.error('Error loading public data:', error)
    );
  }

  loadProtectedData() {
    // This requires authentication
    this.studentService.getStudents().subscribe(
      data => {
        this.students = data;
        console.log('Students:', this.students);
      },
      error => {
        console.error('Error loading protected data:', error);
        // Redirect to login if 401
        if (error.status === 401) {
          this.redirectToLogin();
        }
      }
    );
  }

  login() {
    const credentials = {
      username: 'admin',
      password: 'Admin123456'
    };

    this.studentService.login(credentials).subscribe(
      response => {
        this.studentService.setToken(response.token);
        this.isAuthenticated = true;
        this.loadProtectedData();
      },
      error => console.error('Login failed:', error)
    );
  }

  redirectToLogin() {
    // Implement your login redirect logic
    console.log('Please login to access protected data');
  }
}
```

## 🎯 **Immediate Action Plan:**

1. **Replace protected endpoint calls** with public ones in your frontend
2. **Test the public endpoints** first to verify API connection
3. **Implement authentication flow** for protected data
4. **Add error handling** for 401 responses

This will fix your current 401 errors immediately! 🚀
