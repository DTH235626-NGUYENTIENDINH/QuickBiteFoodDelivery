package com.example.quickbuyfooddelivery;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
public class EmailSender {
    // Thay bằng email và mật khẩu ứng dụng của bạn
    private static final String SENDER_EMAIL = "nguyentiendinh3322@gmail.com";
    private static final String APP_PASSWORD = "dehdljsrexhhqppy";

    public static void sendOTP(String recipientEmail, String otpCode) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, APP_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            // Thiết lập tên hiển thị người gửi là "QuickBite Food" thay vì hiện mỗi email
            message.setFrom(new InternetAddress(SENDER_EMAIL, "QuickBiteFoodDelivery"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));

            // Tiêu đề email có kèm mã OTP để khách xem nhanh ở thông báo
            message.setSubject(otpCode + " là mã xác minh của bạn");

            // --- THIẾT KÊ GIAO DIỆN EMAIL (HTML) ---
            String htmlContent = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; border: 1px solid #e0e0e0; border-radius: 12px; overflow: hidden;'>" +
                    "    <div style='background-color: #FF9800; padding: 25px; text-align: center;'>" +
                    "        <h1 style='color: white; margin: 0; font-size: 28px; text-transform: uppercase;'>QuickBite Food 6Delivery</h1>" +
                    "    </div>" +
                    "    <div style='padding: 30px; background-color: #ffffff; text-align: center; color: #333333;'>" +
                    "        <h2 style='color: #222222;'>Xác thực tài khoản</h2>" +
                    "        <p style='font-size: 16px;'>Cảm ơn bạn đã lựa chọn QuickBite. Vui lòng sử dụng mã dưới đây để hoàn tất quy trình xác minh tài khoản:</p>" +
                    "        <div style='background-color: #f8f9fa; border: 2px dashed #FF9800; display: inline-block; padding: 15px 40px; margin: 25px 0; border-radius: 8px;'>" +
                    "            <span style='font-size: 36px; font-weight: bold; color: #FF9800; letter-spacing: 6px;'>" + otpCode + "</span>" +
                    "        </div>" +
                    "        <p style='font-size: 14px; color: #777777;'>Mã này sẽ hết hạn sau <b>5 phút</b>.<br>Nếu bạn không thực hiện yêu cầu này, hãy bỏ qua email này.</p>" +
                    "    </div>" +
                    "    <div style='background-color: #333333; color: #ffffff; padding: 15px; text-align: center; font-size: 12px;'>" +
                    "        &copy; 2026 QuickBite Food Delivery Team. Chúc bạn ngon miệng!" +
                    "    </div>" +
                    "</div>";

            // Gửi nội dung dưới dạng HTML thay vì Text thường
            message.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
