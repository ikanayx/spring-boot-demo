package space.itzkana.service;

public class Bear implements Animal {
    @Override
    public String getName() {
        return "Bear";
    }

    public Integer getFootQty() {
        return 4;
    }
}
