package dj.sjn.djbaedal.DataClass;

import java.util.ArrayList;

public class list_item {

    private String name, tel_no;
    private String[] image;

    public list_item(String[] image, String name, String tel_no) {
        this.image = image;
        this.name = name;
        this.tel_no = tel_no;
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
}
