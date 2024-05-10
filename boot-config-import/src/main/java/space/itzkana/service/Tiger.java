package space.itzkana.service;

public class Tiger implements Animal {

    private final String name;

    public Tiger(String alias) {
        this.name = "Tiger, aka:" + alias;
    }

    @Override
    public String getName() {
        return name;
    }
}
