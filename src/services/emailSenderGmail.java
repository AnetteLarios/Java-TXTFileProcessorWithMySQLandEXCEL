package services;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.JOptionPane;
public class emailSenderGmail {

    public static void sendEmailWithGmail(String userRecipient, String subject,
                                          String  bodyOfTheMessage){
        String userSender = "codingRepositories@gmail.com";
        String password = "uezs rbhd ygnt sovy";

        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.user", userSender);
        properties.put("mail.smtp.clave", password);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getDefaultInstance(properties);
        MimeMessage message = new MimeMessage(session);

        try{
            message.setFrom(new InternetAddress(userSender));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(userRecipient));
            message.setSubject(subject);
            message.setText(bodyOfTheMessage);
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", userSender, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

        }catch(MessagingException me){
            me.printStackTrace();
        }
    }

    public static void emailExecutor(){
        String userRecipient = "codingRepositories@gmail.com";
        String subject = "EXCEL file successfully generated";
        String bodyOfTheMessage = "Customers file generated successfully";

        sendEmailWithGmail(userRecipient, subject, bodyOfTheMessage);
    }

}