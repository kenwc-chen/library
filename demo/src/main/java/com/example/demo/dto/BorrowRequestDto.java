package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;

public class BorrowRequestDto {

    @NotNull(message = "Inventory ID is required")
    private Long inventoryId;

    // Getters and Setters
    public Long getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Long inventoryId) {
        this.inventoryId = inventoryId;
    }
}
