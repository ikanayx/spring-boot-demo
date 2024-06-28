package space.itzkana.totp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import space.itzkana.totp.dto.GenTotpAuthDTO;
import space.itzkana.totp.entity.User;
import space.itzkana.totp.service.TotpService;
import space.itzkana.totp.service.UserService;

import java.util.Objects;

@RequestMapping("totp")
@RestController
@RequiredArgsConstructor
public class TotpController {

    private final UserService userService;
    private final TotpService totpService;

    @PostMapping(value = "/init2fa")
    public String initTwoFactorAuth(@RequestParam("email") String email) {
        User user = userService.getUser(email); // get from token, cookie, any certificate
        GenTotpAuthDTO totp = totpService.generateSecret(user);

        userService.save2faKey(user.getUid(), totp.getKey());
        return totp.getUrl();
    }

    @PostMapping(value = "/confirm2fa")
    public boolean confirmTwoFactorAuth(@RequestParam("email") String email, @RequestParam("code") String code) {
        User user = userService.getUser(email); // get from token, cookie, any certificate
        boolean confirmed = totpService.authorize(user, code);
        if (confirmed) {
            userService.enable2faKey(user.getUid());
        }
        return confirmed;
    }

    @GetMapping(value = "/disable2fa")
    public void disableTwoFactorAuth(@RequestParam("email") String email) {
        User user = userService.getUser(email); // get from token, cookie, any certificate
        userService.disable2faKey(user.getUid());
    }

    @PostMapping(value = "/requires2fa")
    public boolean login(@RequestParam("email") String email) {
        // TODO consider verifying the password here in order not to reveal that a given user uses 2FA
        return Objects.equals(userService.getUser(email).getTwoFactorAuthEnabled(), 1);
    }
}
