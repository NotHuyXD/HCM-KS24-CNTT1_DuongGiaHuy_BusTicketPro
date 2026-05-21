package com.bus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendBookingConfirmation(String toEmail, String ticketCode, String routeInfo, String seatName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("🎟 Xác nhận đặt vé thành công - Bus Ticket Pro");

            String content = "Cảm ơn bạn đã đặt vé tại Bus Ticket Pro!\n\n"
                    + "THÔNG TIN CHUYẾN ĐI CỦA BẠN:\n"
                    + "- Mã vé: " + ticketCode + "\n"
                    + "- Tuyến đường: " + routeInfo + "\n"
                    + "- Vị trí ghế: " + seatName + "\n\n"
                    + "Vui lòng hoàn tất thanh toán để giữ chỗ. Xin cảm ơn!";

            message.setText(content);
            mailSender.send(message);
            System.out.println("Đã gửi email ngầm thành công tới: " + toEmail);
        } catch (Exception e) {
            System.out.println("Lỗi gửi email (Luồng chính không bị ảnh hưởng): " + e.getMessage());
        }
    }
}