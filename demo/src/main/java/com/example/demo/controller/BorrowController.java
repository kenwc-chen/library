package com.example.demo.controller;

import com.example.demo.dto.BorrowRequestDto;
import com.example.demo.dto.InventoryDto;
import com.example.demo.service.BorrowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrow")
public class BorrowController {

    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    // GET API 來獲取所有可借閱的庫存書籍
    @GetMapping
    public ResponseEntity<List<InventoryDto>> getAvailableBooks() {
        List<InventoryDto> availableBooks = borrowService.getAvailableBooks();
        return new ResponseEntity<>(availableBooks, HttpStatus.OK);
    }

    // POST API 來執行借書操作
    @PostMapping
    public ResponseEntity<InventoryDto> borrowBook(@RequestBody BorrowRequestDto borrowRequestDto) {
        try {
            InventoryDto borrowedInventory = borrowService.borrowBook(borrowRequestDto);
            return new ResponseEntity<>(borrowedInventory, HttpStatus.OK);  // 正常返回庫存詳細信息
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);  // 返回具體錯誤訊息
        }
    }
}
