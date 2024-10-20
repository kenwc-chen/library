-- 建立 User 表
CREATE TABLE IF NOT EXISTS User (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    phone_number VARCHAR(10) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    user_name VARCHAR(20) NOT NULL,
    registration_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login_time TIMESTAMP
);

-- 建立 Book 表
CREATE TABLE IF NOT EXISTS Book (
    isbn VARCHAR(13) PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    author VARCHAR(50),
    introduction TEXT
);

-- 建立 Inventory 表
CREATE TABLE IF NOT EXISTS Inventory (
    inventory_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    isbn VARCHAR(13) NOT NULL,
    store_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('available', 'borrowed', 'damaged', 'lost') DEFAULT 'available',
    FOREIGN KEY (isbn) REFERENCES Book(isbn)
);

-- 建立 Borrowing_Record 表
CREATE TABLE IF NOT EXISTS Borrowing_Record (
    user_id BIGINT,
    inventory_id BIGINT,
    borrowing_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    return_time TIMESTAMP,
    PRIMARY KEY (user_id, inventory_id),
    FOREIGN KEY (user_id) REFERENCES User(user_id),
    FOREIGN KEY (inventory_id) REFERENCES Inventory(inventory_id)
);
