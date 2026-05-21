package com.bus.controller;

import com.bus.entity.User;
import com.bus.repository.UserRepository;
import com.bus.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired private AuthService authService;
    @Autowired
    private com.bus.repository.UserRepository userRepository;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // Logic tham khảo trong luồng xử lý POST Register của bạn
    @PostMapping("/register")
    public String processRegister(@ModelAttribute("user") User user, RedirectAttributes attrs) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            attrs.addFlashAttribute("error", "Tên đăng nhập đã tồn tại!");
            return "redirect:/register";
        }

        // Mã hóa mật khẩu bảo mật
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Mặc định đăng ký từ web sẽ là phân hệ Hành khách
        user.setRole(User.Role.PASSENGER);

        // Lúc này user đã chứa sẵn cả email và address từ form HTML gửi lên
        userRepository.save(user);

        attrs.addFlashAttribute("success", "Đăng ký thành công! Hãy đăng nhập hệ thống.");
        return "redirect:/login";
    }
}