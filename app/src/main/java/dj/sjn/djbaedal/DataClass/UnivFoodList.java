package dj.sjn.djbaedal.DataClass;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UnivFoodList {

    @SerializedName("result")
    Result result;
    @SerializedName("store")
    Store store;

    public Result getResult() {
        return result;
    }

    public Store getStore() {
        return store;
    }

    public class Result {
        @SerializedName("status")
        String status;
        @SerializedName("message")
        String message;

        public String getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }

    public class Store {
        @SerializedName("id")
        String id;
        @SerializedName("name")
        String name;
        @SerializedName("description")
        String description;
        @SerializedName("open")
        Boolean open;
        @SerializedName("phones")
        ArrayList<String> phones;
        @SerializedName("menus")
        ArrayList<Menu> menus;
        @SerializedName("menu_summary")
        String menu_summary;
        @SerializedName("menu_description")
        String menu_description;
        @SerializedName("delivery")
        Delivery delivery;
        @SerializedName("open_hours")
        Open_hours open_hours;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public Boolean getOpen() {
            return open;
        }

        public ArrayList<String> getPhones() {
            return phones;
        }

        public ArrayList<Menu> getMenus() {
            return menus;
        }

        public String getMenu_summary() {
            return menu_summary;
        }

        public String getMenu_description() {
            return menu_description;
        }

        public Delivery getDelivery() {
            return delivery;
        }

        public Open_hours getOpen_hours() {
            return open_hours;
        }

        public class Menu {
            @SerializedName("type")
            String type;
            @SerializedName("date")
            String date;
            @SerializedName("time")
            int time;
            @SerializedName("name")
            String name;
            @SerializedName("description")
            String description;
            @SerializedName("price")
            int price;

            public String getType() {
                return type;
            }

            public String getDate() {
                return date;
            }

            public int getTime() {
                return time;
            }

            public String getName() {
                return name;
            }

            public String getDescription() {
                return description;
            }

            public int getPrice() {
                return price;
            }
        }

        public class Delivery {
            @SerializedName("description")
            String description;

            public String getDescription() {
                return description;
            }
        }

        public class Open_hours{
            @SerializedName("mon")
            String mon;
            @SerializedName("tue")
            String tue;
            @SerializedName("wed")
            String wed;
            @SerializedName("thu")
            String thu;
            @SerializedName("fri")
            String fri;
            @SerializedName("sat")
            String sat;
            @SerializedName("sun")
            String sun;
            @SerializedName("description")
            String description;

            public String getMon() {
                return mon;
            }

            public String getTue() {
                return tue;
            }

            public String getWed() {
                return wed;
            }

            public String getThu() {
                return thu;
            }

            public String getFri() {
                return fri;
            }

            public String getSat() {
                return sat;
            }

            public String getSun() {
                return sun;
            }

            public String getDescription() {
                return description;
            }
        }
    }
}
