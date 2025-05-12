package helper;

import org.testng.annotations.BeforeTest;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Hooks {
    protected static String baseUrl;
    protected static String apiUrl;
    protected static String url;
    protected static String accessToken;
    protected static Properties prop;

    @BeforeTest
    public void setUp() {
        try {
            prop = new Properties();
            String sysPath = System.getProperty("user.dir");

            //Calling the config.properties file
            FileInputStream ip = new FileInputStream(sysPath + "/src/test/java/config/config.properties");

            //Loading the properties file
            prop.load(ip);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        baseUrl = prop.getProperty("baseUrl");
        apiUrl = prop.getProperty("apiUrl");
        accessToken = prop.getProperty("accessToken");
        url = baseUrl + apiUrl;
    }
}