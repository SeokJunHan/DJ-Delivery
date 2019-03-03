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

    private LinkedHashMap<String, list_item> linkedHashMap2 = new LinkedHashMap<>();
    private ArrayList<UnivCaffetteria> Caf1[], Caf2[], Caf3[], Caf4[]; // 학생식당, 남자기숙사, 여자기숙사, 교수회관

    private ArrayList<list_item> list1, list2, list3, list4, list5, list6, list7, list8, list9;
    private ArrayList<list_item> search_list;
    private String[] days;
    private int[] week;

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
        search_list = new ArrayList<>();
        Caf1 = new ArrayList[7];
        Caf2 = new ArrayList[7];
        Caf3 = new ArrayList[7];
        Caf4 = new ArrayList[7];
        days = new String[7];
        week = new int[7];
        for(int i=0; i<7; i++) {
            Caf1[i] = new ArrayList<>();
            Caf2[i] = new ArrayList<>();
            Caf3[i] = new ArrayList<>();
            Caf4[i] = new ArrayList<>();
        }
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

    public LinkedHashMap<String, list_item> getLinkedHashMap2() {
        return linkedHashMap2;
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

    public ArrayList<UnivCaffetteria>[] getCaf1() {
        return Caf1;
    }

    public ArrayList<UnivCaffetteria>[] getCaf2() {
        return Caf2;
    }

    public ArrayList<UnivCaffetteria>[] getCaf3() {
        return Caf3;
    }

    public ArrayList<UnivCaffetteria>[] getCaf4() {
        return Caf4;
    }

    public String[] getDays() {
        return days;
    }

    public void setDays(String[] days) {
        this.days = days;
    }

    public int[] getWeek() {
        return week;
    }

    public void setWeek(int[] week) {
        this.week = week;
    }

    public ArrayList<list_item> getSearch_list() {
        return search_list;
    }

    public void setSearch_list(ArrayList<list_item> search_list) {
        this.search_list = search_list;
    }
}
