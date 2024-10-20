package com.example.demo.dto;

public class InventoryDto {

    private Long inventoryId;      // 庫存的唯一 ID
    private String status;         // 庫存狀態（可借閱、已借閱、損壞、遺失等）
    private String isbn;           // 書籍的 ISBN
    private String name;           // 書名
    private String author;         // 作者
    private String introduction;   // 書籍簡介

    // Getters and Setters
    public Long getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Long inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}
