package space.itzkana.totp.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import space.itzkana.totp.entity.User;

@Repository
public interface UserDao extends JpaRepository<User, Long> {
    @Query(value = "select * from tb_user where email = ?1", nativeQuery = true)
    User findByEmail(String email);
}
