package com.moneyManagement.controller;

import com.moneyManagement.dto.ExpenseDTO;
import com.moneyManagement.service.ExpensesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/expenses")
public class ExpensesController {

    private final ExpensesService expensesService;

    @PostMapping
    public ResponseEntity<ExpenseDTO> addExpense(@RequestBody ExpenseDTO expenseDTO) {
        ExpenseDTO saved = expensesService.addExpenses(expenseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<ExpenseDTO>> getExpenses() {
        List<ExpenseDTO> expenses = expensesService.getCurrentMonthExpensesForCurrentUser();
        return ResponseEntity.ok(expenses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expensesService.deleteExpenses(id);
        return ResponseEntity.noContent().build();
    }
}
