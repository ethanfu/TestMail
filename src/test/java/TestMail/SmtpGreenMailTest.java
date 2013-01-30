package TestMail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.icegreen.greenmail.user.UserException;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.sun.mail.smtp.SMTPTransport;
/**
 * Created with IntelliJ IDEA.
 * 使用greenmail来做的一个简单的测试用例，等同于TLSMailSend的javamail版本
 * User: ethan
 * Date: 13-1-29
 * Time: 下午11:07
 * To change this template use File | Settings | File Templates.
 */
public class SmtpGreenMailTest {
    private static final String USER_PASSWORD = "Sgid@123";
    private static final String USER_NAME = "oipms@sgid.sgcc.com.cn";
    private static final String EMAIL_USER_ADDRESS = "oipms@sgid.sgcc.com.cn";
    private static final String EMAIL_TO = "fuxueliang0924@rayootech.com";
    private static final String EMAIL_SUBJECT = "Test E-Mail";
    private static final String EMAIL_TEXT = "This is a test e-mail.";
    private static final String HOST = "smtp.sgcc.com.cn";
    private GreenMail mailServer;

    @Before
    public void setUp() {
        mailServer = new GreenMail(ServerSetupTest.SMTP);
        mailServer.start();
    }

    @After
    public void tearDown() {
        mailServer.stop();
    }

    @Test
    public void getMails() throws IOException, MessagingException,
            UserException, InterruptedException {
        // setup user on the mail server
        mailServer.setUser(EMAIL_USER_ADDRESS, USER_NAME, USER_PASSWORD);

        // create the javax.mail stack with session, message and transport ..
        Properties props = System.getProperties();
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.timeout", 1000);
        props.put("mail.smtp.ssl.trust", HOST);  //如果该配置不填写在本例子中会报如下错：Could not convert socket to TLS
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.smtp.port", 25);
        Session session = Session.getInstance(props, null);
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(EMAIL_USER_ADDRESS));
        msg.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(EMAIL_TO, false));
        msg.setSubject(EMAIL_SUBJECT);
        msg.setText(EMAIL_TEXT);
        msg.setSentDate(new Date());
        SMTPTransport t = (SMTPTransport) session.getTransport("smtp");
        t.connect(HOST, EMAIL_USER_ADDRESS, USER_PASSWORD);
        t.sendMessage(msg, msg.getAllRecipients());

        assertEquals("250 OK\n", t.getLastServerResponse());
        t.close();

        // fetch messages from server
        MimeMessage[] messages = mailServer.getReceivedMessages();
        assertNotNull(messages);
        assertEquals(1, messages.length);
        MimeMessage m = messages[0];
        assertEquals(EMAIL_SUBJECT, m.getSubject());
        assertTrue(String.valueOf(m.getContent()).contains(EMAIL_TEXT));
        assertEquals(EMAIL_TO, m.getFrom()[0].toString());

    }
}
