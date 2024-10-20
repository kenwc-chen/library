# 圖書借閱系統後端
前端部分: https://github.com/kenwc-chen/library-frontend

此專案是一個圖書借閱系統的後端，使用 Spring Boot 開發。該系統提供使用者認證、圖書借閱與歸還、庫存管理等功能。系統採用 JWT 驗證進行安全性管理，並透過存儲過程與資料庫互動，以確保交易的一致性。

## 功能

- **使用者認證**：用戶可以註冊、登入，並獲得 JWT Token 進行安全存取。
- **圖書借閱**：已登入的用戶可以從庫存中借閱書籍。
- **圖書歸還**：使用者可以歸還借閱的書籍，系統確保只有借閱者能夠歸還書籍。

## 使用技術

- **Spring Boot**：作為專案的核心框架。
- **JWT (JSON Web Token)**：用於安全的使用者認證與授權。
- **Spring Security**：用於處理身份驗證與權限控制。
- **JDBC**：用於資料庫存取與交易管理。
- **MySQL**：作為專案的資料庫，存放書籍、使用者及借閱記錄。
- **Maven**：用於專案的依賴管理與建置自動化。

## 安裝

### 需求

- Java 17+
- Maven 3.6+
- MySQL 資料庫

### 步驟


 **需先設置 MySQL 資料庫：**

    建立專案的 MySQL 資料庫： library

    ```sql
    CREATE DATABASE library;
    ```

 **配置資料庫連線：**

    在 `src/main/resources/application.properties` 中配置 MySQL 資料庫連接：

    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/library
    spring.datasource.username=your_username
    spring.datasource.password=your_password
    ```
  **需手動執行資料庫存儲過程：**
    在"library"資料庫中 執行demo/src/main/resources/db/stored_procedures.sql 中的指令


### API 認證：

1. **使用者註冊**：
    - 端點：`/api/users/register`
    - 方法：`POST`
    - 請求範例：
      ```json
      {
        "phoneNumber": "8888888888",
        "password": "password123",
        "userName": "KEN123"
      }
      ```

2. **使用者登入**：
    - 端點：`/api/auth/login`
    - 方法：`POST`
    - 請求範例：
      ```json
      {
        "phoneNumber": "8888888888",
        "password": "password123"
      }
      ```

    - 回應：成功登入後，會返回 JWT Token。

3. **JWT 認證**：
    在後續的請求中，在 `Authorization` 標頭中加入 JWT Token：

    ```http
    Authorization: Bearer your_jwt_token
    ```

## API 端點

### 使用者

- **POST** `/api/users/register`：註冊新使用者。
- **POST** `/api/auth/login`：用戶登入並返回 JWT token。

### 借閱功能

- **GET** `/api/borrow`：查詢所有可借書籍列表。
- **POST** `/api/borrow`：根據庫存 ID 借書（需 JWT 驗證）。

### 歸還功能

- **GET** `/api/return/borrowed-books`：查詢目前使用者所借閱的書籍。
- **POST** `/api/return`：根據庫存 ID 歸還書籍（需 JWT 驗證）。

## 資料庫架構

資料庫包含以下資料表：

- **User**：儲存用戶的基本資訊。
- **Book**：儲存書籍的基本資訊（ISBN、書名、作者等）。
- **Inventory**：追蹤每本書的庫存，包括狀態（`available`、`borrowed` 等）。
- **Borrowing_Record**：記錄借書情況，包括用戶 ID、庫存 ID、借書時間及歸還時間。


