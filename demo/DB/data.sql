-- 初始化書籍資料
INSERT IGNORE INTO Book (isbn, name, author, introduction)
VALUES ('1', 'ESUN Book 1', 'ESUN Author 1', 'Introduction of Book 1'),
       ('2', 'ESUN Book 2', 'ESUN Author 2', 'Introduction of Book 2'),
       ('3', 'ESUN Book 3', 'ESUN Author 3', 'Introduction of Book 3'),
       ('4', 'ESUN Book 4', 'ESUN Author 4', 'Introduction of Book 4'),
       ('5', 'ESUN Book 5', 'ESUN Author 5', 'Introduction of Book 5');

-- 初始化庫存資料，每本書加入 2 個庫存
INSERT IGNORE INTO Inventory (isbn, status) 
VALUES 
('1', 'available'), ('1', 'available'), 
('2', 'available'), ('2', 'available'), 
('3', 'available'), ('3', 'available'), 
('4', 'available'), ('4', 'available'), 
('5', 'available'), ('5', 'available');