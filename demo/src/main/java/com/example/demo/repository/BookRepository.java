package com.example.demo.repository;

import com.example.demo.model.Book;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BookRepository {

    private final JdbcTemplate jdbcTemplate;

    public BookRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 根據 ISBN 查詢書籍詳細信息
    public Optional<Book> findByIsbn(String isbn) {
        String sql = "SELECT * FROM Book WHERE isbn = ?";

        List<Book> books = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Book book = new Book();
            book.setIsbn(rs.getString("isbn"));
            book.setName(rs.getString("name"));
            book.setAuthor(rs.getString("author"));
            book.setIntroduction(rs.getString("introduction"));
            return book;
        }, isbn);  // 直接傳遞 isbn 作為參數

        // 返回查詢結果，若無資料則回傳空
        return books.isEmpty() ? Optional.empty() : Optional.of(books.get(0));
    }
}
