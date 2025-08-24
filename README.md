# 💘 DatingApp - Ứng dụng hẹn hò Android

DatingApp là ứng dụng hẹn hò trên nền tảng Android, giúp người dùng **tìm kiếm, kết nối và trò chuyện** với nhau.  
Ứng dụng được xây dựng bằng **Java + Android Studio** và tích hợp **Firebase** để quản lý dữ liệu thời gian thực.

---

## ✨ Tính năng chính
- 🔐 **Đăng ký & Đăng nhập**
  - Bằng email hoặc số điện thoại (xác minh OTP)
  - Lưu hồ sơ người dùng trên Firebase
- 🖼 **Tạo & chỉnh sửa hồ sơ**
  - Upload ảnh đại diện (Firebase Storage / Cloudinary)
  - Thông tin cá nhân: tên, tuổi, sở thích...
- 💌 **Ghép đôi & tìm kiếm**
  - Vuốt trái/phải giống Tinder để like hoặc bỏ qua
  - Kiểm tra match khi cả hai cùng like
- 💬 **Chat & kết nối**
  - Nhắn tin văn bản, hình ảnh, âm thanh
  - Hỗ trợ video/audio call (Agora/SDK)
- 🔔 **Thông báo**
  - Thông báo khi có người like, match, tin nhắn mới
- 🛡 **Bảo mật**
  - Firebase Authentication
  - Quản lý dữ liệu an toàn trên Firestore

---

## 🛠 Công nghệ sử dụng
- **Ngôn ngữ**: Java  
- **IDE**: Android Studio  
- **Backend**: Firebase Authentication, Firestore, Firebase Storage  
- **Realtime & Notification**: Firebase Cloud Messaging  
- **UI/UX**: Material Design, CardStackView (vuốt Tinder)  

---

## 🚀 Cài đặt & chạy thử

### 1. Clone dự án
```bash
git clone https://github.com/ten-ban/DatingApp.git
cd DatingApp
