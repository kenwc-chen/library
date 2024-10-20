package com.example.demo.service;

import com.example.demo.dto.BorrowRequestDto;
import com.example.demo.dto.InventoryDto;
import com.example.demo.repository.InventoryRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List; 


@Service
public class BorrowService {

    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;

    public BorrowService(InventoryRepository inventoryRepository, UserRepository userRepository) {
        this.inventoryRepository = inventoryRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public InventoryDto borrowBook(BorrowRequestDto borrowRequestDto) {
        // 獲取當前登入使用者的 phoneNumber
        String currentUserPhoneNumber = getCurrentUserPhoneNumber();

        // 根據 phoneNumber 查詢 userId
        Long userId = userRepository.findUserIdByPhoneNumber(currentUserPhoneNumber);

        // 查詢庫存狀態，確認是否可借
        String status = inventoryRepository.findStatusByInventoryId(borrowRequestDto.getInventoryId());
        if (!"available".equals(status)) {
            throw new IllegalArgumentException("This inventory item is not available for borrowing.");
        }

        // 檢查該庫存是否已經被當前使用者借閱且未歸還
        if (inventoryRepository.isBookBorrowedByUser(userId, borrowRequestDto.getInventoryId())) {
            throw new IllegalArgumentException("You have already borrowed this inventory item.");
        }

        // 調用存儲過程更新庫存狀態，執行借書操作
        inventoryRepository.borrowBook(userId, borrowRequestDto.getInventoryId());

        // 查詢庫存詳細信息，並返回 InventoryDto
        InventoryDto inventoryDto = inventoryRepository.findInventoryDetailsById(borrowRequestDto.getInventoryId())
                                  .orElseThrow(() -> new IllegalArgumentException("Inventory item not found."));

        return inventoryDto;
    }

    // 從 SecurityContext 中獲取當前登入使用者的 phoneNumber
    private String getCurrentUserPhoneNumber() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();  // 假設 phoneNumber 被存儲為 username
    }

    // 新增獲取可借閱庫存的方法
    public List<InventoryDto> getAvailableBooks() {
        // 查詢所有可借閱的庫存
        List<InventoryDto> availableBooks = inventoryRepository.findAvailableBooks();

        return availableBooks;
    }
}
