package TestMail;

import org.junit.Test;

import java.util.Properties;
import java.util.Enumeration;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: ethan
 * Date: 13-1-31
 * Time: 上午10:53
 * To change this template use File | Settings | File Templates.
 */
public class TestPropertiesIterator {

    @Test
    public void testPropertiesIteratorOverJdk1_4() {
        Properties javaMailProperties = System.getProperties();
        Properties props = new Properties();
        Enumeration e = javaMailProperties.propertyNames();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            props.put(key, javaMailProperties.get(key));
        }
        assertEquals(60,props.size());

    }

    @Test
    public void testPropertiesIteratorOverJdk1_6() {
        Properties javaMailProperties = System.getProperties();
        Properties props = new Properties();

        for(String key : javaMailProperties.stringPropertyNames()){
            props.put(key,javaMailProperties.get(key));
        }
        assertEquals(60,props.size());

    }
}
