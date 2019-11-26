package dj.sjn.djbaedal;

import dj.sjn.djbaedal.DataClass.DataInstance;

public class Util {

    public static boolean CheckFoodList() {
        boolean check = false;
        if (DataInstance.getInstance().getList1().size() == 0)
            check = true;
        else if (DataInstance.getInstance().getList2().size() == 0)
            check = true;
        else if (DataInstance.getInstance().getList3().size() == 0)
            check = true;
        else if (DataInstance.getInstance().getList4().size() == 0)
            check = true;
        else if (DataInstance.getInstance().getList5().size() == 0)
            check = true;
        else if (DataInstance.getInstance().getList6().size() == 0)
            check = true;
        else if (DataInstance.getInstance().getList7().size() == 0)
            check = true;
        else if (DataInstance.getInstance().getList8().size() == 0)
            check = true;
        else if (DataInstance.getInstance().getList9().size() == 0)
            check = true;

        return check;
    }
}
