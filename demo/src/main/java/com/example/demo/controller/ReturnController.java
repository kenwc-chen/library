package com.example.demo.controller;

import com.example.demo.dto.InventoryDto;
import com.example.demo.dto.ReturnRequestDto;
import com.example.demo.service.ReturnService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Collections;

@RestController
@RequestMapping("/api/return")
public class ReturnController {

    private final ReturnService returnService;

    public ReturnController(ReturnService returnService) {
        this.returnService = returnService;
    }

    @PostMapping
    public ResponseEntity<?> returnBook(@RequestBody ReturnRequestDto returnRequestDto) {
        try {
            // 調用服務層進行還書處理，返回 InventoryDto
            InventoryDto returnedInventory = returnService.returnBook(returnRequestDto);
            return new ResponseEntity<>(returnedInventory, HttpStatus.OK);  // 正常返回庫存和書籍資訊
        } catch (IllegalArgumentException e) {
            // 返回具體的錯誤訊息
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (SecurityException e) {
            // 返回具體的權限錯誤訊息
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/borrowed-books")
    public ResponseEntity<List<InventoryDto>> getBorrowedBooks() {
        try {
            // 調用服務層返回當前使用者借閱的書籍列表
            List<InventoryDto> borrowedBooks = returnService.getBorrowedBooks();
            return ResponseEntity.ok(borrowedBooks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }
}
