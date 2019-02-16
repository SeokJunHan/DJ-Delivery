package dj.sjn.djbaedal.DataClass;

import java.util.List;

public class RetrofitRepo {
    String name;
    String output;
    List<RetroItem> info;

    public String getName() {
        return name;
    }

    public String getOutput() {
        return output;
    }

    public List<RetroItem> getInfo() {
        return info;
    }
}
