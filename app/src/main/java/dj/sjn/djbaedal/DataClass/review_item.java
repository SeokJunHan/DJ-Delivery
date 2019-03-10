package dj.sjn.djbaedal.DataClass;

public class review_item {
    private String user_id;
    private String user_nickname;
    private int rate;
    private String contents;
    private String timeStamp;

    public review_item(String user_id, String user_nickname, int rate, String contents, String timeStamp) {
        this.user_id = user_id;
        this.user_nickname = user_nickname;
        this.rate = rate;
        this.contents = contents;
        this.timeStamp = timeStamp;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
