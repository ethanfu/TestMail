package TestMail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created with IntelliJ IDEA.
 * Gmail只是一个示例，现在流行的各个主流网站例如yahoomail等都可以利用此配置完成
 * 需要注意的是使用smtp的端口号等即可
 * User: ethan
 * Date: 13-1-28
 * Time: 下午4:20
 * To change this template use File | Settings | File Templates.
 */
public class GMailSend {

    private static final String SMTP_HOST_NAME = "smtp.sgcc.com.cn";
    private static final String SMTP_PORT = "25";
    private static final String emailMsgTxt = "Test Message Contents";
    private static final String emailSubjectTxt = "Yet another test from gmail";
    private static final String emailFromAddress = "you@example.com";
    private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
    private static final String[] sendTo = {"fuxueliang0924@rayootech.com"};

    public static void main(String args[]) throws Exception {
        GMailSend sm = new GMailSend();
        sm.sendSSLMessage(sendTo, emailSubjectTxt, emailMsgTxt, emailFromAddress);
    }

    private void sendSSLMessage(String sendTo[], String emailSubjectTxt, String emailMsgTxt, String emailFromAddress) throws MessagingException {
        boolean debug = true;
        Properties props = new Properties();
        props.put("username", "oipms@sgid.sgcc.com.cn");
        props.put("password", "Sgid@123");

        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtps.host", SMTP_HOST_NAME);
        props.put("mail.smtps.auth", "true");
        props.put("mail.smtps.port", SMTP_PORT);
        props.put("mail.smtps.timeout", 1000);
        props.put("mail.smtps.socketFactory.port", 443);
        props.put("mail.smtps.socketFactory.class", SSL_FACTORY);
        props.put("mail.smtps.socketFactory.fallback", "false");
        props.put("mail.debug", debug);

        SMTPAuthenticator auth = new SMTPAuthenticator();
        auth.setProperties(props);
        Session session = Session.getDefaultInstance(props);

        session.setDebug(debug);
        Transport transport = session.getTransport();
        Message msg = new MimeMessage(session);
        InternetAddress addressFrom = new InternetAddress(emailFromAddress);
        msg.setFrom(addressFrom);

        InternetAddress[] addressTo = new InternetAddress[sendTo.length];
        for (int i = 0; i < sendTo.length; i++) {
            addressTo[i] = new InternetAddress(sendTo[i]);
        }
        msg.setRecipients(Message.RecipientType.TO, addressTo);

        msg.setSubject(emailSubjectTxt);
        msg.setContent(emailMsgTxt, "text/plain");

        transport.connect
                (SMTP_HOST_NAME, 25, props.getProperty("username"), props.getProperty("password"));
        transport.sendMessage(msg,
                msg.getRecipients(Message.RecipientType.TO));
        transport.close();
    }

    /**
     * Simple implementation with the addition of a method to set the Properties file, within which
     * are the authentication details
     *
     * @author mkns
     */
    class SMTPAuthenticator extends javax.mail.Authenticator {

        Properties props = null;

        void setProperties(Properties props) {
            this.props = props;
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(props.getProperty("username"), props.getProperty("password"));
        }
    }
}
