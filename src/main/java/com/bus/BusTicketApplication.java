package com.bus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BusTicketApplication {
    public static void main(String[] args) {
        SpringApplication.run(BusTicketApplication.class, args);
        System.out.println("====== HỆ THỐNG BUS TICKET PRO ĐÃ SẴN SÀNG TẠI PORT 8080 ======");
    }
}