package rapport;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.Properties;

public class Mail {

    private String sender;
    private String password;
    private String to;
    private String[] cc;
    private String subject;
    private String message;
    private String[] files;

    public Mail(String sender, String password, String objet) {
        this.setSender(sender);
        this.setPassword(password);
        this.setSubject(objet);
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String[] getCc() {
        return cc;
    }

    public void setCc(String[] cc) {
        this.cc = cc;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String[] getFiles() {
        return files;
    }

    public void setFiles(String[] files) {
        this.files = files;
    }

    public void send() throws Exception {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.googlemail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        String from = this.getSender();
        String mdp = this.getPassword();

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from, mdp);
                    }
                });

        try {
            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(this.getTo()));

            if (this.getCc() != null) {
                InternetAddress[] tabCC = new InternetAddress[this.getCc().length];
                for (int i = 0; i < tabCC.length; i++) {
                    tabCC[i] = new InternetAddress(this.getCc()[i]);
                }
                message.addRecipients(Message.RecipientType.CC, tabCC);
            }

            message.setSubject(this.getSubject());

            Multipart multipart = new MimeMultipart();

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(this.getMessage(), "text/html; charset=UTF-8");
            multipart.addBodyPart(messageBodyPart);

            if (this.getFiles() != null && this.getFiles().length!=0) {
                for (int i = 0; i < this.getFiles().length; i++) {
                    MimeBodyPart attachPart = new MimeBodyPart();
                    attachPart.attachFile(new File(this.getFiles()[i]));
                    multipart.addBodyPart(attachPart);
                }
            }

            message.setContent(multipart);

            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
