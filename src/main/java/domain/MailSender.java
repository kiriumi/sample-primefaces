package domain;

import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender {

    private static final String smtpHost = ResourceBundle.getBundle("ApplicationConfig").getString("mail.smtp.host");

    public static void sendMail(String email, String title, String message)
            throws MessagingException, AddressException {

        Properties property = new Properties();
        property.put("mail.smtp.host", smtpHost);

        Session session = Session.getInstance(property, null);
        MimeMessage mimeMessage = new MimeMessage(session);

        InternetAddress toAddress = new InternetAddress(email);
        mimeMessage.setRecipient(Message.RecipientType.TO, toAddress);

        InternetAddress fromAddress = new InternetAddress("sender@example.com");
        mimeMessage.setFrom(fromAddress);

        mimeMessage.setSubject(title, "UTF-8");
        mimeMessage.setText(message, "UTF-8");

        Transport.send(mimeMessage);
    }
}
