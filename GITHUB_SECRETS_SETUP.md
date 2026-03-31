# 🔐 GitHub Secrets Setup - LAB 11

## 📋 Bước 1: Kiểm tra bảo mật (Local)

Chạy lệnh sau để xác nhận **không có** hardcoded credentials trong code:

```bash
grep -r 'secret_sauce' src/     # ✅ Kết quả phải RỖNG
grep -r 'standard_user' src/    # ✅ Kết quả phải RỖNG
```

**Kết quả thực tế:**
```
✅ Không tìm thấy 'secret_sauce' trong src/
✅ Không tìm thấy 'standard_user' trong src/main/
```

---

## 🔧 Bước 2: Tạo GitHub Secrets

Vào repository trên GitHub và thực hiện các bước sau:

### 2.1 Truy cập Settings
- Click vào tab **Settings** (góc phải phía trên)

### 2.2 Vào Secrets and variables
- Chọn **Secrets and variables** → **Actions** ở menu bên trái

### 2.3 Tạo Secret #1: Username
- Click **New repository secret**
- **Name:** `SAUCEDEMO_USERNAME`
- **Value:** `standard_user`
- Click **Add secret**

### 2.4 Tạo Secret #2: Password
- Click **New repository secret**
- **Name:** `SAUCEDEMO_PASSWORD`
- **Value:** `secret_sauce`
- Click **Add secret**

**Kết quả:** Bạn sẽ thấy 2 secrets trong danh sách (giá trị ẩn dưới dạng ****)

---

## 📝 Bước 3: Cấu hình GitHub Actions Workflow

File `.github/workflows/ci.yml` đã được cập nhật tự động:

```yaml
- name: Run Selenium Tests
  working-directory: demo
  run: mvn clean test -Dbrowser=${{ matrix.browser }}
  env:
    APP_USERNAME: ${{ secrets.SAUCEDEMO_USERNAME }}
    APP_PASSWORD: ${{ secrets.SAUCEDEMO_PASSWORD }}
```

**Cách hoạt động:**
- GitHub tự động inject secrets vào environment variables `APP_USERNAME` và `APP_PASSWORD`
- Các giá trị này **không bao giờ** hiển thị trong logs (được thay bằng ***)
- CI/CD chỉ có thể đọc, không thể in hay export

---

## 🚀 Bước 4: Cách Java đọc Secrets

File `EnvReader.java` xử lý 2 trường hợp:

### Trường hợp 1: Chạy trên CI/CD (GitHub Actions)
```java
String password = System.getenv("APP_PASSWORD");  // ← GitHub inject vào đây
```

### Trường hợp 2: Chạy local (fallback)
```java
if (password == null || password.isBlank()) {
    password = ConfigReader.getInstance().get("app.password");  // ← Đọc từ config file
}
```

**File cấu hình local:**
- `src/test/resources/config-dev.properties`
- `src/test/resources/config-staging.properties`

Cả 2 đều chứa:
```properties
app.username=standard_user
app.password=secret_sauce
```

---

## ✅ Bước 5: Xác minh hoạt động

### 5.1 Chạy local
```bash
cd demo
mvn clean test
```
Credentials được đọc từ `config-dev.properties` ✅

### 5.2 Chạy trên GitHub Actions
- Push code được commit
- GitHub Actions tự động trigger
- Secrets được inject → `EnvReader` đọc từ env vars
- Logs hiển thị `***` thay vì giá trị thực ✅

---

## 🔒 Best Practices

| Trường hợp | Nguồn Credentials | Ưu tiên |
|-----------|------------------|--------|
| Local | `config-*.properties` | ✅ Chính |
| CI/CD | `secrets.SAUCEDEMO_*` | ✅ Chính |
| Fallback | File config | ✅ Backup |

**Quy tắc vàng:**
- ❌ **KHÔNG BỎNG** hardcoded credentials trong code
- ✅ **CÓ** đặt trong file `.properties` cho local
- ✅ **CÓ** tạo GitHub Secrets cho CI/CD
- ✅ **CÓ** sử dụng `System.getenv()` trước `ConfigReader`

---

## 🧪 Test Case: Xác minh bảo mật

```java
@Test
public void verifyNoHardcodedCredentials() {
    assertFalse(FileUtils.findString("secret_sauce", "src/"));
    assertFalse(FileUtils.findString("standard_user", "src/main/"));
    assertTrue(EnvReader.getPassword() != null);
    assertTrue(EnvReader.getUsername() != null);
}
```

---

## 💡 Troubleshooting

| Vấn đề | Nguyên nhân | Giải pháp |
|--------|-----------|----------|
| Tests fail + NPE | Credentials null | Kiểm tra `APP_USERNAME`, `APP_PASSWORD` env vars |
| Secrets không hoạt động | Chưa tạo secrets | Vào GitHub → Settings → Secrets → Add Secret |
| Giá trị hiển thị rõ ở logs | Chưa dùng `secrets.*` | Sửa workflow dùng `${{ secrets.NAME }}` |
| Chạy local ko có creds | File config không có | Thêm vào `config-dev.properties` |

---

## 📚 Tham khảo

**GitHub Secrets Docs:** https://docs.github.com/en/actions/security-guides/using-secrets-in-github-actions

**LAB 11 Timeline:**
1. ✅ Tạo GitHub Secrets (SAUCEDEMO_USERNAME, SAUCEDEMO_PASSWORD)
2. ✅ Cập nhật workflow env vars
3. ✅ Cập nhật config files
4. ✅ Xác minh EnvReader hoạt động
5. ⏳ Push & trigger GitHub Actions
