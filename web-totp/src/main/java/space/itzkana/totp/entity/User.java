package space.itzkana.totp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tb_user")
public class User {

    @Id
    private Long uid;

    private String username;

    private String email;

    private String twoFactorAuthKey;

    private Integer twoFactorAuthEnabled;
}
