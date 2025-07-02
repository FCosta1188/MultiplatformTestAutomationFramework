package eu.nets.test.util;

import eu.nets.test.flows.data.models.MpaUser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static eu.nets.test.util.AllureUtil.logError;
import static eu.nets.test.util.AllureUtil.logInfo;

public final class MailUtil {

    private static Folder INBOX;
    private static Store STORE;

    public static String getOtp(MpaUser user, String password, boolean deleteOtpEmail) throws RuntimeException, MessagingException {
        Session session = getSession(user.email(), password);

        String otp = "";
        String otpMessageText = "";
        String otpMessageReadableText = "";
        try {
            STORE = session.getStore("imap");
            STORE.connect();

            INBOX = STORE.getFolder("INBOX");
            INBOX.open(Folder.READ_WRITE);

            int maxTries = 3;
            int waitMs = 5000;

            for (int i = 0; i < maxTries; i++) {
                EnvUtil.safeSleep(waitMs);

                List<Message> messages = List.of(INBOX.getMessageCount() > 0 ? INBOX.getMessages() : new Message[0]);
                List<Message> targetMessages = filterAndSort(user, messages);

                Message otpMessage = targetMessages.stream()
                        .findFirst()
                        .orElse(null);
                //.orElseThrow(() -> new RuntimeException("No emails found for recipient: " + email));

                if (otpMessage == null) {
                    continue;
                }

                otpMessageText = getContentText(otpMessage);
                Document otpDoc = Jsoup.parse(otpMessageText);
                otpMessageReadableText = otpDoc.text(); // Remove HTML tags, etc.

                Pattern pattern = Pattern.compile("\\b\\d{6}\\b");
                Matcher matcher = pattern.matcher(otpMessageReadableText);

                if (matcher.find()) {
                    otp = matcher.group();
                    otpMessage.setFlag(Flags.Flag.DELETED, deleteOtpEmail); // delete permanently, it does not move to the trash
                    break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(logError(e.getMessage()));
        } finally {
            if (INBOX != null && INBOX.isOpen()) {
                INBOX.close(true);
            }
            if (STORE != null && STORE.isConnected()) {
                STORE.close();
            }
        }

        if (otp.isBlank()) {
            logInfo("otpMessageText\n" + otpMessageText);
            logInfo("otpMessageReadableText\n" + otpMessageReadableText);
            throw new RuntimeException(logError("Unable to get OTP from inbox: " + user.email()));
        } else {
            return otp;
        }
    }

    private static List<Message> filterAndSort(MpaUser user, List<Message> messages) {
        try {
            return messages.stream()
                    .filter(msg -> {
                        try {
                            switch (user.type()) {
                                case "CVR":
                                    String subject = msg.getSubject();
                                    if (!subject.toUpperCase().contains("VAT")) {
                                        break;
                                    }
                                default:
                                    if (!msg.isSet(Flags.Flag.SEEN)) { // Only UNREAD emails
                                        Address[] recipients = msg.getRecipients(Message.RecipientType.TO);
                                        if (recipients != null) {
                                            for (Address address : recipients) {
                                                if (address.toString().equalsIgnoreCase(user.email())) { // Only target recipient (To: email)
                                                    return true;
                                                }
                                            }
                                        }
                                    }
                            }
                        } catch (MessagingException e) {
                            throw new RuntimeException(logError(e.getMessage()));
                        }

                        return false;
                    })
                    .sorted((msg1, msg2) -> {
                                try {
                                    Date date1 = msg1.getReceivedDate();
                                    Date date2 = msg2.getReceivedDate();

                                    if (date1 == null && date2 == null) {
                                        return 0;
                                    }
                                    if (date1 == null) {
                                        return -1;
                                    }
                                    if (date2 == null) {
                                        return 1;
                                    }

                                    return date2.compareTo(date1);
                                } catch (Exception e) {
                                    return 0;
                                }
                            }
                    )
                    .toList();
        } catch (Exception e) {
            logError("Error filtering and sorting messages: " + e.getMessage());
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    private static String getContentText(Message message) throws MessagingException, IOException {
        Object content = message.getContent();

        if (content instanceof String) {
            return (String) content;
        } else if (content instanceof MimeMultipart) {
            return getTextFromMimeMultipart((MimeMultipart) content);
        }

        return "";
    }

    private static String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
        StringBuilder result = new StringBuilder();

        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart part = mimeMultipart.getBodyPart(i);
            if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                continue; // skip attachments
            }

            Object partContent = part.getContent();
            if (partContent instanceof String) {
                result.append(partContent);
            } else if (partContent instanceof MimeMultipart) {
                result.append(getTextFromMimeMultipart((MimeMultipart) partContent));
            }
        }

        return result.toString();
    }

    private static Session getSession(String email, String password) {
        String host;
        String port;

        switch (email.split("@")[1]) {
            case "gmail.com":
                host = PropertiesUtil.MPA.getProperty("mail.imap.host.gmail");
                port = PropertiesUtil.MPA.getProperty("mail.imap.port.gmail");
                break;
            case "yahoo.com":
                host = PropertiesUtil.MPA.getProperty("mail.imap.host.yahoo");
                port = PropertiesUtil.MPA.getProperty("mail.imap.port.yahoo");
                break;
            default:
                throw new RuntimeException(logError("Invalid email provider: " + email));
        }

        Properties connProperties = new Properties();
        connProperties.put("mail.imap.host", host);
        connProperties.put("mail.imap.port", port);
        connProperties.put("mail.imap.ssl.enable", "true");
        connProperties.put("mail.imap.auth", "true");
        connProperties.put("mail.imap.ssl.trust", "*"); // Ignore SSL certificate validation - use for test only!!!

        return Session.getInstance(connProperties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });
    }
}
