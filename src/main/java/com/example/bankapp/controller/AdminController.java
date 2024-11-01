package com.example.bankapp.controller;

import com.example.bankapp.dto.AccountDTO;
import com.example.bankapp.model.Account;
import com.example.bankapp.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/admin")
@Controller
public class AdminController {
    @Autowired
    private AccountService accountService;

    @ExceptionHandler(Exception.class)
    public String allException( Model model){
        model.addAttribute("error",  "ban khong co quyen truy cap");
        return "dashboard";
    }


    @RequestMapping(value = "/accounts/{id}", method = {RequestMethod.DELETE, RequestMethod.POST})
    public String deleteAccount(@PathVariable Long id) {
        try {
            accountService.deleteAccountById(id);
            return "redirect:/admin/accounts";
        } catch (RuntimeException e) {
            return "error";
        }
    }


    @GetMapping("/accounts")
    public String listAccounts(Model model) {
        // Lấy danh sách tài khoản và thêm vào model
        model.addAttribute("accounts", accountService.getAllAccounts());
        return "account-list";
    }

    @GetMapping("/updateUser/{id}")
    public String getAccount(@PathVariable Long id,Model model) {

        model.addAttribute("accountDTO",accountService.findAccountById(id));
        return "updateUser";
    }

    @PostMapping("/updateUser")
    public String updateAccount(@ModelAttribute @Valid AccountDTO accountDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            // Nếu có lỗi validation, trả về trang đăng ký cùng với thông báo lỗi
            return "updateUser";
        }
        try {
            accountService.UpdateAccount(accountDTO);
            return "redirect:/admin/accounts";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "updateUser";
        }
    }
}
