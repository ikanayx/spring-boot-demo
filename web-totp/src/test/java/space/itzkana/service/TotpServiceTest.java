package space.itzkana.service;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import space.itzkana.totp.TotpApplication;
import space.itzkana.totp.service.TotpService;

@SpringBootTest(classes = TotpApplication.class)
public class TotpServiceTest {

    @Autowired
    private TotpService totpService;

    @Test
    public void testOffset() {
        String secret = "PB2SPZS5CRCHRMJ6CO5SWH5DBVGYVX5I";
        // String url = "otpauth://totp/:foo@bar.com?secret=PB2SPZS5CRCHRMJ6CO5SWH5DBVGYVX5I&issuer=&algorithm=SHA1&digits=6&period=30";
        System.out.println("last -> " + totpService.authorize(secret, 982022));
        System.out.println("this -> " + totpService.authorize(secret, 48053));
        // totpService.authorize(secret, 0);
    }
}
