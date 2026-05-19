package com.bus.config;

import com.bus.entity.Route;
import com.bus.entity.User;
import com.bus.repository.RouteRepository;
import com.bus.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired private UserRepository userRepository;
    @Autowired private RouteRepository routeRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("123456"));
            admin.setFullName("Quản Trị Viên");
            admin.setPhone("0988888888");
            admin.setRole(User.Role.ADMIN);
            userRepository.save(admin);
        }

        if (routeRepository.count() == 0) {
            Route r1 = new Route();
            r1.setStartLocation("Hà Nội"); r1.setEndLocation("Hải Phòng");
            r1.setDistanceKm(120.0); r1.setBasePrice(150000.0);

            Route r2 = new Route();
            r2.setStartLocation("TP.Hồ Chí Minh"); r2.setEndLocation("Đà Lạt");
            r2.setDistanceKm(300.0); r2.setBasePrice(250000.0);

            routeRepository.saveAll(Arrays.asList(r1, r2));
            System.out.println("Đã khởi tạo sẵn các Tuyến Đường mẫu vào DB!");
        }

        if (userRepository.findByUsername("staff").isEmpty()) {
            User staff = new User();
            staff.setUsername("staff");
            staff.setPassword(passwordEncoder.encode("123456"));
            staff.setFullName("Nhân Viên Phòng Vé");
            staff.setPhone("0977777777");
            staff.setRole(User.Role.STAFF);

            userRepository.save(staff);
            System.out.println("Đã khởi tạo tài khoản Staff mặc định: staff / 123456");
        }
    }
}