package com.example.demo.service;

import com.example.demo.dto.InventoryDto;
import com.example.demo.dto.ReturnRequestDto;
import com.example.demo.repository.InventoryRepository;
import com.example.demo.repository.UserRepository;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;  // 確保使用的是 java.util.List

@Service
public class ReturnService {

    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;

    public ReturnService(InventoryRepository inventoryRepository, UserRepository userRepository) {
        this.inventoryRepository = inventoryRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public InventoryDto returnBook(ReturnRequestDto returnRequestDto) {
        // 從 SecurityContext 中獲取當前使用者的 phoneNumber
        String currentUserPhoneNumber = getCurrentUserPhoneNumber();

        // 根據 phoneNumber 查詢當前使用者的 userId
        Long userId = userRepository.findUserIdByPhoneNumber(currentUserPhoneNumber);

        // 使用 findInventoryDetailsById 查詢庫存和書籍詳細資訊
        InventoryDto inventoryDto = inventoryRepository.findInventoryDetailsById(returnRequestDto.getInventoryId())
                .orElseThrow(() -> new IllegalArgumentException("Inventory record not found."));

        // 檢查庫存狀態，確保書籍是已借出狀態
        if (!"borrowed".equals(inventoryDto.getStatus())) {
            throw new IllegalArgumentException("This book has not been borrowed and cannot be returned.");
        }

        // 檢查當前使用者是否是借閱此書籍的人
        boolean isBorrowedByCurrentUser = inventoryRepository.isBookBorrowedByUser(userId, returnRequestDto.getInventoryId());
        if (!isBorrowedByCurrentUser) {
            throw new SecurityException("You cannot return this book because it was not borrowed by you.");
        }

        // 調用存儲過程，執行還書操作
        inventoryRepository.returnBook(userId, returnRequestDto.getInventoryId());

        // 返回庫存和書籍的詳細資訊
        return inventoryDto;
    }

    // 從 SecurityContext 中獲取當前登入使用者的 phoneNumber
    private String getCurrentUserPhoneNumber() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();  // 假設 phoneNumber 被存儲為 username
    }

    // 新增方法：查詢當前用戶借閱的書籍
    public List<InventoryDto> getBorrowedBooks() {
        String currentUserPhoneNumber = getCurrentUserPhoneNumber();
        Long userId = userRepository.findUserIdByPhoneNumber(currentUserPhoneNumber);

        // 調用 InventoryRepository 查詢該用戶借閱的書籍
        return inventoryRepository.findBorrowedBooksByUserId(userId);
    }
}