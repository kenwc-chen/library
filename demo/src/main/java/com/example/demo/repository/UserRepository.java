package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 檢查手機是否存在
    public boolean existsByPhoneNumber(String phoneNumber) {
        String sql = "SELECT COUNT(*) FROM User WHERE phone_number = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, phoneNumber);
        return count != null && count > 0;
    }

    // 檢查UserName是否存在
    public boolean existsByUserName(String userName) {
        String sql = "SELECT COUNT(*) FROM User WHERE user_name = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userName);
        return count != null && count > 0;
    }

    // 儲存到資料庫
    public void saveUser(User user) {
        String sql = "INSERT INTO User (phone_number, password, user_name) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, user.getPhoneNumber(), user.getPassword(), user.getUserName());
    }

    // 登入時利用手機號碼找用戶
    public Optional<User> findByPhoneNumber(String phoneNumber) {
        String sql = "SELECT * FROM User WHERE phone_number = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, userRowMapper(), phoneNumber));
        } catch (Exception e) {
            return Optional.empty();  // 如果找不到用戶則返回空
        }
    }
    public Long findUserIdByPhoneNumber(String phoneNumber) {
        String sql = "SELECT user_id FROM User WHERE phone_number = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, phoneNumber);
    }
    // RowMapper to map result set to User object
    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> {
            User user = new User();
            user.setUserId(rs.getLong("user_id"));
            user.setPhoneNumber(rs.getString("phone_number"));
            user.setPassword(rs.getString("password"));
            user.setUserName(rs.getString("user_name"));
            user.setRegistrationTime(rs.getTimestamp("registration_time").toLocalDateTime());
            user.setLastLoginTime(rs.getTimestamp("last_login_time") != null
                    ? rs.getTimestamp("last_login_time").toLocalDateTime()
                    : null);
            return user;
        };
    }
}
