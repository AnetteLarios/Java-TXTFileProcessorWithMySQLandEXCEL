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

import processor.FileProcessorService;

import sql.DataBaseConnection;

import models.Client;
/*
emailSenderGmail class is in charge to make the connection with a google API to make able for the user to send an
email when the excel file is created.
@author Anette Larios
@since 27.06.2023

 */
public class emailSenderGmail {

    private static final String APPLICATION_NAME = "Gmail API Java OAuth2";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = List.of(GmailScopes.GMAIL_SEND);

    public static void callHttpTransport() throws Exception {
        final HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        //Uploading credentials from Json's file
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                new FileReader("Filepath"));

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
        String receiver = "ReceiverEmail";
        String subject = "Customers EXCEL File Generated Successfully";
        //Strim builder set HTML
        MimeMessage message = createEmail(receiver, subject);
        sendMessage(gmail, "me", message);

        JOptionPane.showMessageDialog(null, "Email sent successfully");
    }

    private static MimeMessage createEmail(String receiver, String subject) throws MessagingException, FileNotFoundException, IOException {
        Properties properties = new Properties();
        Session session = Session.getDefaultInstance(properties, null);
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress("WriterEmail"));
        email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(receiver));
        email.setSubject(subject);

        String htmlTemplate = readEmailTemplate("htmlFilePath");
        MimeBodyPart htmlBodyPart = new MimeBodyPart();
        htmlBodyPart.setContent(htmlTemplate, "text/html");
        //Creating a specific bodyPart

        //Creating a specific part in the email to attach the Excel File
        MimeBodyPart attachmentBodyPart = new MimeBodyPart();
        try {
            ByteArrayDataSource dataSource = new ByteArrayDataSource(new FileInputStream("XLSX " +
                    "FILE PATH"), "application/octet-stream");
            attachmentBodyPart.setDataHandler(new DataHandler(dataSource));
            attachmentBodyPart.setFileName("customers.xlsx");

            //Combining the parts of the bodymessage
            MimeMultipart multipartMessage = new MimeMultipart();
            multipartMessage.addBodyPart(htmlBodyPart);
            multipartMessage.addBodyPart(attachmentBodyPart);


            //Setting the content of the email to the email object
            email.setContent(multipartMessage);
        } catch (MessagingException | FileNotFoundException exception) {
            exception.printStackTrace();
        } catch (IOException exceptionTwo) {
            exceptionTwo.printStackTrace();
        }
        return email;
    }

    private static void sendMessage(Gmail service, String userId, MimeMessage email) throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);

        Message message = new Message();
        message.setRaw(encodedEmail);
        service.users().messages().send(userId, message).execute();

    }

    private static String readEmailTemplate(String emailTemplateFilePath) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(emailTemplateFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                contentBuilder.append(line);
            }
        }

        return contentBuilder.toString().replace("{textToReplace}", "Aqui va la tabla");

    }
    public static void extractDataFromClientsList(List<Client>clientList){
        String firstFiveClients = "";
        for(int i= 0; i<5; i++){
          firstFiveClients += String.format(i+1 + " %s, %s, %s, %s, %s, %d, %f, %s, %f, \n",
                                           clientList.get(i).getName(),
                                           clientList.get(i).getPhoneNumber(),
                                           clientList.get(i).getAddress(),
                                           clientList.get(i).getEmail(),
                                           clientList.get(i).getCountry(),
                                           clientList.get(i).getNumberRange(),
                                           clientList.get(i).getBalance(),
                                           clientList.get(i).getRfc(),
                                           clientList.get(i).getTax());

        }
        firstFiveClients.replace("null","");
        System.out.println(firstFiveClients);

    }

}

