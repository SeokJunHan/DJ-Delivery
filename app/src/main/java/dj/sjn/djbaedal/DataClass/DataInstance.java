package dj.sjn.djbaedal.DataClass;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class DataInstance {

    private static DataInstance datainstance;
    private final int MAP_MAX = 3;

    private LinkedHashMap<String, list_item> linkedHashMap = new LinkedHashMap<String, list_item>() {
        @Override
        protected boolean removeEldestEntry(Entry<String, list_item> eldest) {
            return size() > MAP_MAX ? true : false;
        }
    };

    private ArrayList<list_item> list1, list2, list3, list4, list5, list6, list7, list8, list9;

    private DataInstance() {
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        list3 = new ArrayList<>();
        list4 = new ArrayList<>();
        list5 = new ArrayList<>();
        list6 = new ArrayList<>();
        list6 = new ArrayList<>();
        list7 = new ArrayList<>();
        list8 = new ArrayList<>();
        list9 = new ArrayList<>();
    }

    public static DataInstance getInstance() {
        if (datainstance == null) {
            synchronized (DataInstance.class) {
                if (datainstance == null) {
                    datainstance = new DataInstance();
                }
            }
        }
        return datainstance;
    }

    public LinkedHashMap<String, list_item> getLinkedHashMap() {
        return linkedHashMap;
    }

    public ArrayList<list_item> getList1() {
        return list1;
    }

    public ArrayList<list_item> getList2() {
        return list2;
    }

    public ArrayList<list_item> getList3() {
        return list3;
    }

    public ArrayList<list_item> getList4() {
        return list4;
    }

    public ArrayList<list_item> getList5() {
        return list5;
    }

    public ArrayList<list_item> getList6() {
        return list6;
    }

    public ArrayList<list_item> getList7() {
        return list7;
    }

    public ArrayList<list_item> getList8() {
        return list8;
    }

    public ArrayList<list_item> getList9() {
        return list9;
    }
}
