package space.itzkana.openapi.model;

import io.swagger.v3.oas.annotations.media.Schema;

@lombok.Data
@lombok.ToString
@Schema(description = "参数集合")
public class ParamsHolder {

    @Schema(description = "名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "称谓")
    private String title;
}
