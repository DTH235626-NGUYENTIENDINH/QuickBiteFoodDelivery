package com.example.quickbuyfooddelivery;

import android.os.AsyncTask;
import java.util.Properties;
import java.util.Random;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailSenders {
    private static final String SENDER_EMAIL = "trigamo112005@gmail.com";
    private static final String SENDER_PASSWORD = "your_app_password";

    public interface EmailCallback {
        void onEmailSent(String verificationCode);
        void onEmailFailed(String error);
    }

    public static String generateCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    public static void sendVerificationEmail(String recipientEmail, String verificationCode, EmailCallback callback) {
        new AsyncTask<Void, Void, Boolean>() {
            private String errorMessage;

            @Override
            protected Boolean doInBackground(Void... voids) {
                try {
                    Properties props = new Properties();
                    props.put("mail.smtp.auth", "true");
                    props.put("mail.smtp.starttls.enable", "true");
                    props.put("mail.smtp.host", "smtp.gmail.com");
                    props.put("mail.smtp.port", "587");

                    Session session = Session.getInstance(props, new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
                        }
                    });

                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(SENDER_EMAIL));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
                    message.setSubject("Mã xác minh QuickBite");
                    message.setText(
                        "Xin chào!\n\n" +
                        "Mã xác minh của bạn là: " + code + "\n\n" +
                        "Mã có hiệu lực trong 5 phút.\n" +
                        "Vui lòng không chia sẻ mã này với ai.\n\n" +
                        "QuickBite Team"
                    );
                    Transport.send(message);
                    return true;
                } catch (MessagingException e) {
                    errorMessage = e.getMessage();
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean success) {
                if (success) {
                    callback.onSuccess(code);
                } else {
                    callback.onFailure(errorMessage);
                }
            }
        }.execute();
    }
}
