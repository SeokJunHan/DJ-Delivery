package dj.sjn.djbaedal.DataClass;

public class UnivCaffetteria {

    private int time; // 0 1 2 3 4 아침 점심 저녁 종일 기타
    private String Menu;

    public UnivCaffetteria(int time, String menu) {
        this.time = time;
        Menu = menu;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getMenu() {
        return Menu;
    }

    public void setMenu(String menu) {
        Menu = menu;
    }
}
