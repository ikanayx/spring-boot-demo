package space.itzkana.totp.service;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import org.springframework.stereotype.Service;
import space.itzkana.totp.dto.GenTotpAuthDTO;
import space.itzkana.totp.entity.User;

@Service
public class TotpService {

    private final GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();

    public GenTotpAuthDTO generateSecret(User user) {
        GoogleAuthenticatorKey googleAuthenticatorKey = googleAuthenticator.createCredentials();

        GenTotpAuthDTO dto = new GenTotpAuthDTO();
        // note - preferably encrypt it with an externally stored (or even HSM) key
        dto.setKey(googleAuthenticatorKey.getKey());

        String AUTH_ISSUER = "";
        dto.setUrl(GoogleAuthenticatorQRGenerator.getOtpAuthURL(AUTH_ISSUER, user.getEmail(), googleAuthenticatorKey));
        return dto;
    }

    public boolean authorize(User user, String code) {
        return authorize(user.getTwoFactorAuthKey(), Integer.parseInt(code));
    }

    public boolean authorize(String key, Integer code) {
        return googleAuthenticator.authorize(key, code);
    }
}
