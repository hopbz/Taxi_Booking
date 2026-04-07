# 🚕 Hệ Thống Đặt & Điều Xe Taxi (Taxi Booking System)

## 📚 Giới Thiệu Khóa Học

**Chương Trình Đào Tạo: Java Spring Boot Backend (24 Buổi)**

Khóa học được thiết kế để xây dựng nền tảng vững chắc về Spring Boot Backend thông qua dự án thực hành xuyên suốt: **Hệ thống Đặt & Điều xe Taxi**. 

### Mục Tiêu Khóa Học

- **Hiểu sâu Spring Core**: Nắm vững IoC/DI, không code máy móc
- **Thành thạo JPA & Database**: Thiết kế ERD, thao tác CRUD, quan hệ bảng
- **Bảo mật thực tế**: JWT Authentication, Role-based Authorization
- **Deploy Production**: Docker, CI/CD, Cloud Deployment (Render/Railway)
- **Best Practices**: DTO Pattern, Exception Handling, Unit Testing, Caching

### Cấu Trúc Khóa Học

Khóa học được chia thành **4 giai đoạn**:

1. **Giai đoạn 1 (Buổi 1-4)**: Khởi động & Nền tảng Spring Core
2. **Giai đoạn 2 (Buổi 5-10)**: Database & Data JPA
3. **Giai đoạn 3 (Buổi 11-18)**: Bảo mật & Nghiệp vụ Nâng cao
4. **Giai đoạn 4 (Buổi 19-24)**: Deployment & Hoàn thiện

---

## 🎯 Dự Án: Hệ Thống Đặt & Điều Xe Taxi

### Tổng Quan Dự Án

Hệ thống **Taxi Booking** là một ứng dụng backend cho phép:
- **Hành khách (Passenger)**: Đặt xe, xem lịch sử chuyến đi, thanh toán
- **Tài xế (Driver)**: Nhận chuyến, cập nhật trạng thái, hoàn thành chuyến
- **Quản trị viên (Admin)**: Quản lý người dùng, theo dõi doanh thu

### Công Nghệ Sử Dụng

- **Framework**: Spring Boot 3.x
- **Database**: MySQL 8.0
- **Security**: Spring Security + JWT
- **Build Tool**: Maven
- **Java Version**: JDK 17+
- **Caching**: Redis (optional)
- **Testing**: JUnit 5 + Mockito
- **Deployment**: Docker + Render/Railway

---

## 🗄️ Thiết Kế Cơ Sở Dữ Liệu

### 1. Bảng `users` (Người dùng)

Lưu trữ thông tin chung cho cả Hành khách, Tài xế và Quản trị viên.

| Cột | Kiểu | Mô tả |
|-----|------|-------|
| `id` | Long (PK, Auto Increment) | Khóa chính |
| `email` | String (Unique) | Tên đăng nhập |
| `password` | String | Mật khẩu đã mã hóa (BCrypt) |
| `full_name` | String | Họ tên đầy đủ |
| `phone` | String | Số điện thoại liên hệ |
| `role` | Enum | Phân quyền: `ROLE_PASSENGER`, `ROLE_DRIVER`, `ROLE_ADMIN` |
| `balance` | BigDecimal | Ví tiền ảo (để thanh toán giả lập) |
| `vehicle_type` | String (Nullable) | Loại xe (chỉ dành cho Driver: 4 chỗ, 7 chỗ, xe máy) |
| `is_active` | Boolean | Trạng thái kích hoạt tài khoản |
| `avatar_url` | String (Nullable) | Đường dẫn ảnh đại diện |
| `created_at` | DateTime | Thời gian tạo tài khoản |
| `updated_at` | DateTime | Thời gian cập nhật |

### 2. Bảng `bookings` (Chuyến xe)

Lưu trữ thông tin giao dịch đặt xe.

| Cột | Kiểu | Mô tả |
|-----|------|-------|
| `id` | Long (PK, Auto Increment) | Khóa chính |
| `passenger_id` | Long (FK → users.id) | Người đặt xe |
| `driver_id` | Long (FK → users.id, Nullable) | Tài xế nhận chuyến (null khi mới đặt) |
| `pickup_location` | String | Địa điểm đón |
| `dropoff_location` | String | Địa điểm đến |
| `distance_km` | Double | Khoảng cách ước tính (km) |
| `total_price` | BigDecimal | Giá cước chuyến đi |
| `status` | Enum | Trạng thái: `PENDING`, `ACCEPTED`, `IN_PROGRESS`, `COMPLETED`, `CANCELLED` |
| `created_at` | DateTime | Thời gian đặt |
| `accepted_at` | DateTime (Nullable) | Thời gian tài xế nhận chuyến |
| `completed_at` | DateTime (Nullable) | Thời gian hoàn thành |

### 3. Bảng `feedbacks` (Đánh giá) - Optional

| Cột | Kiểu | Mô tả |
|-----|------|-------|
| `id` | Long (PK, Auto Increment) | Khóa chính |
| `booking_id` | Long (FK → bookings.id) | Chuyến xe được đánh giá |
| `rating` | Integer | Điểm đánh giá (1-5 sao) |
| `comment` | String | Nội dung góp ý |
| `created_at` | DateTime | Thời gian tạo |

### Quan Hệ Giữa Các Bảng

```
users (1) ────< (N) bookings (passenger_id)
users (1) ────< (N) bookings (driver_id)
bookings (1) ────< (N) feedbacks
```

---

## 🔄 Các Luồng Nghiệp Vụ Chính

### 1. Luồng Đặt Xe (Booking Flow - Passenger)

```
1. Passenger đăng nhập
2. Chọn điểm đi, điểm đến
3. Gọi API tính giá: POST /api/bookings/calculate
   → System trả về giá cước dự kiến
4. Xác nhận đặt xe: POST /api/bookings
   → System tạo Booking với status = PENDING
```

### 2. Luồng Nhận Chuyến (Acceptance Flow - Driver)

```
1. Driver đăng nhập
2. Xem các chuyến đang chờ: GET /api/bookings/available
3. Chọn chuyến và nhận: POST /api/bookings/{id}/accept
   → System kiểm tra:
      - Nếu chuyến vẫn PENDING: Gán driver_id, status = ACCEPTED ✅
      - Nếu đã bị nhận/hủy: Trả về lỗi ❌
```

### 3. Luồng Hoàn Thành Chuyến (Completion Flow)

```
1. Driver đến đón khách → Update status = IN_PROGRESS
2. Driver đưa khách đến nơi → Update status = COMPLETED
3. System trừ tiền trong ví (nếu có)
4. System gửi email hóa đơn cho Passenger
```

### 4. Luồng Hủy Chuyến Tự Động (Auto Cancellation)

```
1. Scheduler chạy ngầm mỗi 5 phút
2. Quét bookings có status = PENDING và created_at > 15 phút
3. Tự động cập nhật status = CANCELLED
```

---

## 📁 Cấu Trúc Dự Án

```
taxi-booking-java-be/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── taxi/
│   │   │           ├── controller/      # REST Controllers
│   │   │           ├── service/         # Business Logic
│   │   │           ├── repository/      # Data Access Layer
│   │   │           ├── entity/          # JPA Entities
│   │   │           ├── dto/             # Data Transfer Objects
│   │   │           ├── security/        # JWT, Security Config
│   │   │           ├── exception/       # Custom Exceptions
│   │   │           ├── config/           # Configuration Classes
│   │   │           └── TaxiBookingApplication.java
│   │   └── resources/
│   │       ├── application.properties    # Database, JWT config
│   │       └── application-dev.properties
│   └── test/
│       └── java/                        # Unit Tests
├── docker-compose.yml                   # Docker setup
├── Dockerfile                           # Container image
├── pom.xml                              # Maven dependencies
└── README.md
```

---

## 🚀 Hướng Dẫn Setup

### Yêu Cầu Hệ Thống

- **JDK**: 17 hoặc cao hơn
- **Maven**: 3.8+
- **MySQL**: 8.0+
- **IDE**: VS Code (với Java Extension Pack) hoặc IntelliJ IDEA
- **Postman**: Để test API
- **Git**: Để quản lý version control

### Cài Đặt

1. **Clone repository:**
```bash
git clone <repository-url>
cd taxi-booking-java-be
```

2. **Cấu hình Database:**
   - Tạo database MySQL: `taxi_booking_db`
   - Cập nhật `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/taxi_booking_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

3. **Chạy ứng dụng:**
```bash
mvn spring-boot:run
```

4. **Test API:**
   - Import Postman collection (nếu có)
   - Hoặc test trực tiếp: `GET http://localhost:8080/api/welcome`

---

## 🔐 API Endpoints Chính

### Authentication
- `POST /auth/register` - Đăng ký tài khoản
- `POST /auth/login` - Đăng nhập (trả về JWT Token)

### Bookings
- `POST /api/bookings/calculate` - Tính giá cước
- `POST /api/bookings` - Tạo chuyến đặt xe
- `GET /api/bookings/available` - Xem chuyến đang chờ (Driver)
- `POST /api/bookings/{id}/accept` - Nhận chuyến (Driver)
- `GET /api/bookings/history` - Lịch sử chuyến đi
- `GET /api/bookings/pending` - Chuyến đang chờ xử lý

### Users
- `GET /api/users/profile` - Xem thông tin cá nhân
- `PUT /api/users/profile` - Cập nhật thông tin
- `POST /api/users/avatar` - Upload ảnh đại diện

---

## 🧪 Testing

Chạy Unit Tests:
```bash
mvn test
```

Chạy Integration Tests:
```bash
mvn verify
```

---

## 🐳 Docker Deployment

### Chạy với Docker Compose

```bash
docker-compose up -d
```

File `docker-compose.yml` sẽ tự động:
- Build và chạy Spring Boot app
- Khởi động MySQL container
- Tạo network kết nối giữa các services

### Build Docker Image

```bash
docker build -t taxi-booking-backend .
docker run -p 8080:8080 taxi-booking-backend
```

---

## 📝 Git Workflow

### ⚠️ Lưu Ý Quan Trọng: Mỗi Học Viên Tạo 1 Branch Riêng

Để tránh xung đột code và quản lý tốt hơn, **mỗi học viên phải làm việc trên 1 branch riêng duy nhất cho toàn bộ khóa học**. Tất cả các bài tập và cập nhật sẽ được commit vào cùng 1 branch này.

### Quy Trình Làm Việc

1. **Tạo branch mới (chỉ tạo 1 lần ở buổi đầu):**
```bash
git checkout -b your-name
# Ví dụ: nguyen-van-a
```

2. **Làm việc và commit code trên branch của bạn:**
```bash
git add .
git commit -m "feat: implement welcome API"
git push origin your-name
```

3. **Tiếp tục cập nhật code cho các buổi học sau:**
   - Luôn làm việc trên cùng 1 branch đã tạo
   - Commit và push thường xuyên sau mỗi buổi học
   - Ví dụ:
```bash
git add .
git commit -m "feat: add user registration API"
git push origin your-name
```

4. **Tạo Pull Request (PR):**
   - Vào GitHub repository
   - Click "New Pull Request"
   - Chọn branch của bạn → `main` (hoặc `develop`)
   - Điền mô tả và tạo PR
   - Chờ giảng viên review và merge
   - **Lưu ý**: Có thể tạo PR ngay từ buổi đầu hoặc sau khi hoàn thành một số tính năng

5. **Cập nhật code mới nhất từ main:**
```bash
git checkout main
git pull origin main
git checkout your-name
git merge main  # Hoặc rebase
```

### Quy Tắc Đặt Tên Branch

- Format: `ten-hoc-vien`
- Ví dụ:
  - `nguyen-van-a`
  - `tran-thi-b`
  - `le-van-c`
- **Lưu ý**: Không cần thêm số buổi học vào tên branch

### Commit Message Convention

- `feat:` - Thêm tính năng mới
- `fix:` - Sửa lỗi
- `refactor:` - Refactor code
- `docs:` - Cập nhật documentation
- `test:` - Thêm test cases

**Ví dụ commit messages:**
```bash
git commit -m "feat: implement welcome API (Buổi 3)"
git commit -m "feat: add user registration with DTO (Buổi 7)"
git commit -m "feat: implement JWT authentication (Buổi 12)"
```

---

## 📚 Tài Liệu Tham Khảo

- [Spring Boot Official Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Spring Security](https://spring.io/projects/spring-security)
- [JWT.io](https://jwt.io/) - JWT Debugger
- [MySQL Documentation](https://dev.mysql.com/doc/)

---

## 👥 Đóng Góp

Dự án này là một phần của khóa học. Mọi thắc mắc hoặc đề xuất cải thiện, vui lòng tạo Issue trên GitHub.

---

## 📄 License

Dự án này được sử dụng cho mục đích giáo dục.

---

**Chúc các bạn học tập hiệu quả! 🚀**

