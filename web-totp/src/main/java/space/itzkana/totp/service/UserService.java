package space.itzkana.totp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.itzkana.totp.dao.UserDao;
import space.itzkana.totp.entity.User;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

    public User getUser(String email) {
        return userDao.findByEmail(email);
    }

    public void save2faKey(Long uid, String _2faKey) {
        var user = userDao.findById(uid);
        if (user.isPresent()) {
            user.get().setTwoFactorAuthKey(_2faKey);
            userDao.save(user.get());
        }
    }

    public void enable2faKey(Long uid) {
        var user = userDao.findById(uid);
        if (user.isPresent()) {
            user.get().setTwoFactorAuthEnabled(1);
            userDao.save(user.get());
        }
    }

    public void disable2faKey(Long uid) {
        var user = userDao.findById(uid);
        if (user.isPresent()) {
            user.get().setTwoFactorAuthKey("");
            user.get().setTwoFactorAuthEnabled(0);
            userDao.save(user.get());
        }
    }
}
