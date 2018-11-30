package myImplement;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;


/**
 * 邮件用单例模式
 * 成员变量 发件人地址，收件人地址，发件人账号，发件人密码
 * 参数设置用Properties类来设置，设置的变量包括 mail.smtp.auth, mail.transport.protocol, mail.host
 * 通过以后的参数建立会话Session
 * 通过会话Session建立邮件，邮件设置包括发件人和收件人（收件人可以有很多个），日期，邮件主题，邮件内容
 * 通过会话建立通道，通道需要设置账号和密码。通过通道发送邮件，需要两个参数，邮件和收件人，收件人可以从邮件获取
 */



public class Mail {
    private static Mail mail = null;
    private static String senderAddress = "tstxxy@163.com";
    private static String recipientAddress = "tstxxy@gmail.com";
    private static String senderAccount = "tstxxy@163.com";
    private static String senderPassword = "";

    private Mail() {
    }

    public static Mail getInstance() {
        if (mail == null) {
            synchronized (Mail.class) {
                if (mail == null) {
                    mail = new Mail();
                }
            }
        }
        return mail;
    }

    public void send(String html) throws MessagingException {
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.host", "smtp.163.com");
        Session session = Session.getInstance(properties);
        session.setDebug(false);
        MimeMessage mimeMessage = getMimeMessage(session, html);
        Transport transport = session.getTransport();
        transport.connect(senderAccount, senderPassword);
        transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
    }


    private MimeMessage getMimeMessage(Session session, String html) throws MessagingException {
        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.setSender(new InternetAddress(senderAddress));
        mimeMessage.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(recipientAddress));
        mimeMessage.setSentDate(new Date());

        mimeMessage.setSubject("邮件主题", "UTF-8");
        mimeMessage.setContent(html, "text/html;charset=UTF-8");

        return mimeMessage;
    }

}
