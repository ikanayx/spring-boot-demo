package space.itzkana.model;

import lombok.Data;

@Data
public class Doc {

    private String id;
    private String title = "Testing Document";
    private String version;

    public Doc(String id, String version) {
        this.id = id;
        this.version = version;
    }
}
