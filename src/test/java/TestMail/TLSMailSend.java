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
 * 该种情况（拿foxmail举例）适合于在设置smtp的地方，在ssl的选项上不选中，但是在starttls的选项上选中
 * 该测试用例依赖于javamail 1.4.3以及以上版本，低版本会不通过
 * 此测试不需要在应用的jdk上安装CA认证的证书即可
 * 如有疑问可参照http://qmail.jms1.net/test-auth.shtml先通过命令行测试通过，保证对方的邮件服务器是没有问题即可，网页中给
 * 出了通过命令行的方式来实现测试
 * User: ethan
 * Date: 13-1-28
 * Time: 下午7:21
 * To change this template use File | Settings | File Templates.
 */
public class TLSMailSend extends TestCase{
    private static final String SMTP_HOST_NAME = "smtp.*.com.cn";
    private static final int SMTP_HOST_PORT = 25;
    private static final String SMTP_AUTH_USER = "**@**.com.cn";
    private static final String SMTP_AUTH_PWD = "****";

    /**
     * 这个是根据国家电网某项目来调整的测试用例
     * @throws Exception
     */
    public void testTls() throws Exception{
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", SMTP_HOST_NAME);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.smtp.trust", "*");//该处*代表任何网站，也可以添加单独domain或者list ，
        props.put("mail.smtp.starttls.enable", true);


        props.put("mail.smtp.timeout", 1000);


        Session mailSession = Session.getDefaultInstance(props);
        mailSession.setDebug(true);
        Transport transport = mailSession.getTransport();
        MimeMessage message = new MimeMessage(mailSession);
        message.setSubject("Testing SMTP");
        message.setFrom(new InternetAddress(SMTP_AUTH_USER));
        message.setContent("This is a test!!!", "text/plain");

        message.addRecipient(Message.RecipientType.TO,
                new InternetAddress("fuxueliang0924@rayootech.com"));

        transport.connect
                (SMTP_HOST_NAME, SMTP_HOST_PORT, SMTP_AUTH_USER, SMTP_AUTH_PWD);
        transport.sendMessage(message,
                message.getRecipients(Message.RecipientType.TO));
        transport.close();
    }
}
