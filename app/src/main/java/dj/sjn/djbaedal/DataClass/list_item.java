package dj.sjn.djbaedal.DataClass;

import java.util.ArrayList;

public class list_item {

    private String name, tel_no, type, extra_text, thumbnail;
    private String[] image;

    public list_item(String[] image, String name, String tel_no, String type, String extra_text, String thumbnail) {
        this.image = image;
        this.name = name;
        this.tel_no = tel_no;
        this.type = type;
        this.extra_text = extra_text;
        this.thumbnail = thumbnail;
    }

    public String[] getImage() {
        return image;
    }

    public void setImage(String[] image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel_no() {
        return tel_no;
    }

    public void setTel_no(String tel_no) {
        this.tel_no = tel_no;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExtra_text() {
        return extra_text;
    }

    public void setExtra_text(String extra_text) {
        this.extra_text = extra_text;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
