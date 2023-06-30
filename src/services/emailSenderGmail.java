package services;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.Json;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Base64;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;
import javax.mail.util.ByteArrayDataSource;
import com.google.api.services.gmail.model.*;
import com.google.api.services.gmail.model.ModifyMessageRequest;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.activation.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;
public class emailSenderGmail {

    private static final String APPLICATION_NAME = "Gmail API Java OAuth2";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = List.of(GmailScopes.GMAIL_SEND);

    public static void callHttpTransport() throws Exception {
        final HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        //Uploading credentials from Json's file
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                new FileReader("C:\\Users\\SpectrumByte\\Downloads\\" +
                        "client_secret_1065365837178-cjmv3r738luang50m99mptpffvge2luc.apps.googleusercontent.com.json"));

        //Setting up authorization flow
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

        //Obtaining authorization's url
        String authorizationUrl = flow.newAuthorizationUrl().setRedirectUri("urn:ietf:wg:oauth:2.0:oob").build();
        System.out.println("Open the next link on your browser and authorize the app");
        System.out.println(authorizationUrl);

        //Read authorization's code from console
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please enter the authorization code");
        String authorizationCode = br.readLine();

        //Exchanging authorization code for access code
        GoogleTokenResponse tokenResponse = flow.newTokenRequest(authorizationCode)
                .setRedirectUri("urn:ietf:wg:oauth:2.0:oob")
                .execute();
        Credential credential = new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(JSON_FACTORY)
                .setClientSecrets(clientSecrets)
                .build()
                .setFromTokenResponse(tokenResponse);

        //Creating a Gmail instance
        Gmail gmail = new Gmail.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        //Data to send emailT
        String receiver = "codingRepositories@gmail.com";
        String subject = "Customers EXCEL File Generated Successfully";
        String bodyText = "Excel File Generated: ";
        //Strim builder set HTML
        MimeMessage message = createEmail(receiver, subject, bodyText);
        sendMessage(gmail, "me", message);

        JOptionPane.showMessageDialog(null, "Email sent successfully");
    }
    private static MimeMessage createEmail(String receiver, String subject, String bodyText) throws MessagingException, FileNotFoundException , IOException {
        Properties properties = new Properties();
        Session session = Session.getDefaultInstance(properties,null);
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress("codingRepositories@gmail.com"));
        email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(receiver));
        email.setSubject(subject);

        String htmlTemplate = readEmailTemplate("C:\\Users\\SpectrumByte\\Documents\\CódigosPaola" +
                                                                    "\\Java-TXTFileProcessorWithMySQLandEXCEL\\src" +
                                                                    "\\services\\EmailTemplate.html");
        MimeBodyPart htmlBodyPart = new MimeBodyPart();
        htmlBodyPart.setContent(htmlTemplate, "text/html");
        //Creating a specific bodyPart
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(bodyText);
        //Creating a specific part in the email to attach the Excel File
        MimeBodyPart attachmentBodyPart = new MimeBodyPart();
        try {
            ByteArrayDataSource dataSource = new ByteArrayDataSource(new FileInputStream("C:\\Users\\SpectrumByte\\Documents" +
                    "\\CódigosPaola\\Java-TXTFileProcessorWithMySQLandEXCEL\\src\\Resources\\" +
                    "customers.xlsx"), "application/octet-stream");
            attachmentBodyPart.setDataHandler(new DataHandler(dataSource));
            attachmentBodyPart.setFileName("customers.xlsx");

            //Combining the parts of the bodymessage
            MimeMultipart multipartMessage = new MimeMultipart();
            multipartMessage.addBodyPart(messageBodyPart);
            multipartMessage.addBodyPart(attachmentBodyPart);
            multipartMessage.addBodyPart(htmlBodyPart);

            //Setting the content of the email to the email object
            email.setContent(multipartMessage);
        } catch (MessagingException | FileNotFoundException exception){
            exception.printStackTrace();
        } catch (IOException exceptionTwo){
            exceptionTwo.printStackTrace();
        }
        return email;
    }

    private static void sendMessage(Gmail service, String userId, MimeMessage email)throws MessagingException, IOException
    {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);

        Message message = new Message();
        message.setRaw(encodedEmail);
        service.users().messages().send(userId,message).execute();

    }

    private static String readEmailTemplate(String emailTemplateFilePath)throws IOException{
        StringBuilder contentBuilder = new StringBuilder();

        try(BufferedReader br = new BufferedReader(new FileReader(emailTemplateFilePath))){
            String line;
            while((line = br.readLine()) != null){
                contentBuilder.append(line);
            }
        }
        return contentBuilder.toString();
        
    }
}

