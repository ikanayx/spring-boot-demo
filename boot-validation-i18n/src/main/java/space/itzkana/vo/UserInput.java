package space.itzkana.vo;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import space.itzkana.annotation.IsNationalPhone;
import space.itzkana.validator.group.UpdateValidateGroup;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInput {

    @NotNull(message = "{user.info.uid.not-null}", groups = {UpdateValidateGroup.class})
    private Long uid;

    @NotEmpty(message = "{user.info.phone.not-empty}")
    @Length(min = 11, message = "{user.info.phone.length-not-match}")
    @IsNationalPhone(message = "{user.info.phone.regex-not-match}", accept = "CN")
    private String phone;
}
