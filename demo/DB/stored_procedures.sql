DELIMITER $$
CREATE PROCEDURE BorrowBook (
    IN p_user_id BIGINT,
    IN p_inventory_id BIGINT
)
BEGIN
    DECLARE book_status ENUM('available', 'borrowed', 'damaged', 'lost');
    
    -- 檢查指定庫存的狀態
    SELECT status INTO book_status FROM Inventory WHERE inventory_id = p_inventory_id;
    
    -- 如果書籍不可借閱，則報錯
    IF book_status != 'available' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'This specific book is not available for borrowing';
    END IF;

    -- 檢查使用者是否有針對該庫存的未完成借閱
    IF EXISTS (SELECT 1 FROM Borrowing_Record WHERE user_id = p_user_id AND inventory_id = p_inventory_id AND return_time IS NULL) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'User has already borrowed this specific book and has not returned it yet';
    ELSE
        -- 插入或更新借閱記錄
        INSERT INTO Borrowing_Record (user_id, inventory_id, borrowing_time)
        VALUES (p_user_id, p_inventory_id, CURRENT_TIMESTAMP)
        ON DUPLICATE KEY UPDATE borrowing_time = CURRENT_TIMESTAMP, return_time = NULL;
        
        -- 更新庫存狀態
        UPDATE Inventory SET status = 'borrowed' WHERE inventory_id = p_inventory_id;
    END IF;
END$$

-- 還書存儲過程
CREATE PROCEDURE ReturnBook (
    IN p_user_id BIGINT,
    IN p_inventory_id BIGINT
)
BEGIN
    -- 檢查是否有進行中的借閱記錄
    IF NOT EXISTS (SELECT 1 FROM Borrowing_Record 
                   WHERE user_id = p_user_id 
                   AND inventory_id = p_inventory_id 
                   AND return_time IS NULL) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No active borrowing record found for this book';
    END IF;
    
    -- 開始還書交易
    START TRANSACTION;
    
    -- 更新庫存狀態
    UPDATE Inventory SET status = 'available' WHERE inventory_id = p_inventory_id;
    
    -- 設置還書時間
    UPDATE Borrowing_Record 
    SET return_time = CURRENT_TIMESTAMP 
    WHERE user_id = p_user_id 
    AND inventory_id = p_inventory_id;
    
    COMMIT;
END$$

-- 恢復分隔符
DELIMITER ;
