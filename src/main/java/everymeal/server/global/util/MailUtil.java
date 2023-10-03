package everymeal.server.global.util;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMessage.RecipientType;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MailUtil {
    private final JavaMailSender javaMailSender;

    public void sendMail(String email, String title, String text) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            mimeMessage.setSubject(title);
            mimeMessage.setText(text);
            mimeMessage.setContent(text, "text/html; charset=utf-8");
            mimeMessage.setRecipients(RecipientType.TO, email);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
