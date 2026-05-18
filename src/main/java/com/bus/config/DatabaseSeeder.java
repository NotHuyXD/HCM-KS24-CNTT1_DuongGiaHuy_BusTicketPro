package com.bus.config;

import com.bus.entity.User;
import com.bus.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername("admin_pro").isEmpty()) {

            User admin = new User();
            admin.setUsername("admin_pro");
            admin.setPassword(passwordEncoder.encode("123456"));
            admin.setFullName("Quản Trị Viên");
            admin.setPhone("0988888888");

            // Set quyền cao nhất là ADMIN
            admin.setRole(User.Role.ADMIN);

            userRepository.save(admin);

            System.out.println("=========================================================");
            System.out.println("Đã khởi tạo tài khoản Admin mặc định để test:");
            System.out.println("Tên đăng nhập : admin");
            System.out.println("Mật khẩu      : 123456");
            System.out.println("=========================================================");
        }
    }
}