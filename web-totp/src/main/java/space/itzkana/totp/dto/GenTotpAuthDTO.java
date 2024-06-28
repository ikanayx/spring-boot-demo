package space.itzkana.totp.dto;

import lombok.Data;

@Data
public class GenTotpAuthDTO {

    private String key;

    private String url;
}
