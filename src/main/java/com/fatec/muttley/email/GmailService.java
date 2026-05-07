package com.fatec.muttley.email;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.internet.*;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.Base64;
import java.util.List;
import java.util.Properties;

@Service
public class GmailService {

    private static final String APPLICATION_NAME = "Muttley";
    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_PATH = "tokens"; // pasta onde salva o token OAuth
    private static final List<String> SCOPES = List.of(GmailScopes.GMAIL_SEND);
    private static final String CREDENTIALS_FILE = "classpath:credentials.json";

    private static Gmail buildGmailClient() throws Exception {
        NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();

        InputStream in = ResourceUtils.getFile(CREDENTIALS_FILE).toURI().toURL().openStream();
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                transport, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_PATH)))
                .setAccessType("offline")
                .build();

        Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");

        return new Gmail.Builder(transport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public void enviarEmailComPdf(String destinatario, String assunto, String corpo, File pdf) throws Exception {
        Gmail gmail = buildGmailClient();

        // Monta o e-mail MIME com anexo
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress("me"));
        email.addRecipient(jakarta.mail.Message.RecipientType.TO, new InternetAddress(destinatario));
        email.setSubject(assunto);

        // Parte do texto
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText(corpo, "utf-8");

        // Parte do PDF
        MimeBodyPart attachmentPart = new MimeBodyPart();
        DataSource source = new FileDataSource(pdf);
        attachmentPart.setDataHandler(new DataHandler(source));
        attachmentPart.setFileName(pdf.getName());

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(textPart);
        multipart.addBodyPart(attachmentPart);
        email.setContent(multipart);

        // Converte para o formato da Gmail API
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        String encodedEmail = Base64.getUrlEncoder().encodeToString(buffer.toByteArray());

        Message message = new Message();
        message.setRaw(encodedEmail);

        gmail.users().messages().send("me", message).execute();
    }

    public static void enviarEmail(String destinatario, String assunto, String corpo) throws Exception {
        Gmail gmail = buildGmailClient();

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress("me"));
        email.addRecipient(jakarta.mail.Message.RecipientType.TO, new InternetAddress(destinatario));
        email.setSubject(assunto);
        email.setText(corpo, "utf-8");

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        String encodedEmail = Base64.getUrlEncoder().encodeToString(buffer.toByteArray());

        Message message = new Message();
        message.setRaw(encodedEmail);

        gmail.users().messages().send("me", message).execute();
    }
}