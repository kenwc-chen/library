package com.example.demo.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import com.example.demo.dto.InventoryDto;

@Repository
public class InventoryRepository {

    private final JdbcTemplate jdbcTemplate;

    public InventoryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 根據 Inventory ID 查詢書籍的當前狀態
    public String findStatusByInventoryId(Long inventoryId) {
        String sql = "SELECT status FROM Inventory WHERE inventory_id = ?";
        return jdbcTemplate.queryForObject(sql, String.class, inventoryId);
    }

    // 根據 Inventory ID 查詢庫存詳細信息
    public Optional<InventoryDto> findInventoryDetailsById(Long inventoryId) {
        String sql = "SELECT i.inventory_id, i.status, b.isbn, b.name, b.author, b.introduction " +
                     "FROM Inventory i JOIN Book b ON i.isbn = b.isbn WHERE i.inventory_id = ?";
        try {
            InventoryDto inventoryDto = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                InventoryDto dto = new InventoryDto();
                dto.setInventoryId(rs.getLong("inventory_id"));
                dto.setStatus(rs.getString("status"));
                dto.setIsbn(rs.getString("isbn"));
                dto.setName(rs.getString("name"));
                dto.setAuthor(rs.getString("author"));
                dto.setIntroduction(rs.getString("introduction"));
                return dto;
            }, inventoryId);
            return Optional.ofNullable(inventoryDto);
        } catch (Exception e) {
            return Optional.empty(); // 捕獲異常返回空
        }
    }

    // 調用存儲過程執行借書操作
    public void borrowBook(Long userId, Long inventoryId) {
        String sql = "CALL BorrowBook(?, ?)";
        jdbcTemplate.update(sql, userId, inventoryId);
    }

    // 調用存儲過程執行還書操作
    public void returnBook(Long userId, Long inventoryId) {
        String sql = "CALL ReturnBook(?, ?)";
        jdbcTemplate.update(sql, userId, inventoryId);
    }

    // 檢查該書籍是否是由指定的 userId 借閱
    public boolean isBookBorrowedByUser(Long userId, Long inventoryId) {
        String sql = "SELECT COUNT(*) FROM Borrowing_Record WHERE user_id = ? AND inventory_id = ? AND return_time IS NULL";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, inventoryId);
        return count != null && count > 0;
    }

    // 查詢所有可借閱的庫存
    public List<InventoryDto> findAvailableBooks() {
        String sql = "SELECT i.inventory_id, i.status, b.isbn, b.name, b.author, b.introduction " +
                     "FROM Inventory i JOIN Book b ON i.isbn = b.isbn WHERE i.status = 'available'";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            InventoryDto inventoryDto = new InventoryDto();
            inventoryDto.setInventoryId(rs.getLong("inventory_id"));
            inventoryDto.setStatus(rs.getString("status"));
            inventoryDto.setIsbn(rs.getString("isbn"));
            inventoryDto.setName(rs.getString("name"));
            inventoryDto.setAuthor(rs.getString("author"));
            inventoryDto.setIntroduction(rs.getString("introduction"));
            return inventoryDto;
        });
    }
    // 根據 userId 查詢該用戶借閱的書籍
    public List<InventoryDto> findBorrowedBooksByUserId(Long userId) {
        String sql = "SELECT i.inventory_id, i.status, b.isbn, b.name, b.author, b.introduction " +
                     "FROM Borrowing_Record br " +
                     "JOIN Inventory i ON br.inventory_id = i.inventory_id " +
                     "JOIN Book b ON i.isbn = b.isbn " +
                     "WHERE br.user_id = ? AND br.return_time IS NULL";  // 只查詢未還書籍
                     
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            InventoryDto inventoryDto = new InventoryDto();
            inventoryDto.setInventoryId(rs.getLong("inventory_id"));
            inventoryDto.setStatus(rs.getString("status"));
            inventoryDto.setIsbn(rs.getString("isbn"));
            inventoryDto.setName(rs.getString("name"));
            inventoryDto.setAuthor(rs.getString("author"));
            inventoryDto.setIntroduction(rs.getString("introduction"));
            return inventoryDto;
        }, userId);
    }
}
