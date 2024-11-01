package com.example.bankapp.controller;

import com.example.bankapp.dto.AccountDTO;
import com.example.bankapp.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/register")
    public String showRegistrationFrom(Model model) {
        model.addAttribute("accountDTO", new AccountDTO());
        return "register";
    }

    @PostMapping("/register")
    public String registerAccount(@ModelAttribute @Valid AccountDTO accountDTO, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            // Nếu có lỗi validation, trả về trang đăng ký cùng với thông báo lỗi
            return "register";
        }
        try {
            accountService.registerAccount(accountDTO);
            redirectAttributes.addFlashAttribute("error",  "dang ky thanh cong");
            return "redirect:/login";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/forgotPassword")
    public String showforgotPassword(Model model) {
        model.addAttribute("accountDTO", new AccountDTO());
        return "forgotpassword";
    }
    @GetMapping("/changePassword")
    public String changePassword(Model model) {
        model.addAttribute("accountDTO", new AccountDTO());
        return "changepassword";
    }

    @PostMapping("/changePassword")
    public String changePassword(@ModelAttribute @Valid AccountDTO accountDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            // Nếu có lỗi validation, trả về trang đăng ký cùng với thông báo lỗi
            return "changepassword";
        }
        try {
            accountService.changePassword(accountDTO);
                return "redirect:/login";

        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "changepassword";
        }
    }

    @PostMapping("/forgotPassword")
    public String forgotPassword(@ModelAttribute @Valid AccountDTO accountDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            // Nếu có lỗi validation, trả về trang đăng ký cùng với thông báo lỗi
            return "forgotpassword";
        }
        try {
            accountService.forgotPassword(accountDTO) ;
                return "redirect:/login";

        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "forgotpassword";
        }
    }
}
