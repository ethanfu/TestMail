package TestMail;

import junit.framework.TestCase;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: ethan
 * Date: 13-1-28
 * Time: 下午7:21
 * To change this template use File | Settings | File Templates.
 */
public class TLSMailSend extends TestCase{
    private static final String SMTP_HOST_NAME = "smtp.sgcc.com.cn";
    private static final int SMTP_HOST_PORT = 25;
    private static final String SMTP_AUTH_USER = "oipms@sgid.sgcc.com.cn";
    private static final String SMTP_AUTH_PWD  = "Sgid@123";

    public void test() throws Exception{
        Properties props = new Properties();
        props.put("username",SMTP_AUTH_USER);
        props.put("password",SMTP_AUTH_PWD);
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", SMTP_HOST_NAME);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", true);

        Session mailSession = Session.getDefaultInstance(props);
        mailSession.setDebug(true);
        Transport transport = mailSession.getTransport();

        MimeMessage message = new MimeMessage(mailSession);
        message.setSubject("Testing SMTP");
        message.setFrom(new InternetAddress("oipms@sgid.sgcc.com.cn"));
        message.setContent("This is a test", "text/plain");

        message.addRecipient(Message.RecipientType.TO,
                new InternetAddress("fuxueliang0924@rayootech.com"));
        transport.connect(SMTP_HOST_NAME, SMTP_HOST_PORT, props.getProperty("username"), props.getProperty("password"));
        transport.sendMessage(message,message.getRecipients(Message.RecipientType.TO));
        transport.close();
    }
}
