package dj.sjn.djbaedal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;

import dj.sjn.djbaedal.Adapter.UnivFoodListAdapter;
import dj.sjn.djbaedal.DataClass.CheckNetwork;
import dj.sjn.djbaedal.DataClass.DataInstance;
import dj.sjn.djbaedal.DataClass.UnivCaffetteria;

public class UnivFoodActivity extends AppCompatActivity {

    Toolbar toolbar;
    Spinner spinner;
    UnivFoodListAdapter adapter1, adapter2, adapter3, adapter4;
    dj.sjn.djbaedal.DataClass.MyListView listView1, listView2, listView3, listView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schoolfood);

        toolbar = findViewById(R.id.toolbarUnivFood);
        spinner = findViewById(R.id.spinner);
        listView1 = findViewById(R.id.food_list1);
        listView2 = findViewById(R.id.food_list2);
        listView3 = findViewById(R.id.food_list3);
        listView4 = findViewById(R.id.food_list4);

        if (!new CheckNetwork().getNetworkInfo(getApplicationContext())) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("네트워크에 연결할 수 없습니다.");
            alertDialogBuilder
                    .setMessage("연결상태를 확인해주세요.")
                    .setCancelable(false)
                    .setPositiveButton("재시도", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            recreate();
                        }
                    })
                    .setNegativeButton("종료", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finishAffinity();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        Calendar cal = Calendar.getInstance();
        int today_week = cal.get(Calendar.DAY_OF_WEEK);

        final ArrayList<String> list = new ArrayList<>();
        for(int i=0; i<7; i++) {
            String date = DataInstance.getInstance().getDays()[i];
            String week = "";
            switch(i) {
                case 0:
                    week = "일";
                    break;
                case 1:
                    week = "월";
                    break;
                case 2:
                    week = "화";
                    break;
                case 3:
                    week = "수";
                    break;
                case 4:
                    week = "목";
                    break;
                case 5:
                    week = "금";
                    break;
                case 6:
                    week = "토";
                    break;
            }
            String month = date.split("-")[1];
            String day = date.split("-")[2];
            list.add(month + "/" + day + " " + "(" + week + ")");
        }

        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, list);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(today_week-1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void SpinnerSelected(int position) {
        ArrayList<UnivCaffetteria> arrayList1 = new ArrayList<>();
        adapter1 = new UnivFoodListAdapter(UnivFoodActivity.this, arrayList1);
        if(DataInstance.getInstance().getCaf1()[position].size() == 0)  {
            arrayList1.add(null);
        }
        else {
            for (UnivCaffetteria menu : DataInstance.getInstance().getCaf1()[position]) {
                arrayList1.add(new UnivCaffetteria(menu.getTime(), menu.getMenu()));
            }
        }
        listView1.setAdapter(adapter1);

        ArrayList<UnivCaffetteria> arrayList2 = new ArrayList<>();
        adapter2 = new UnivFoodListAdapter(UnivFoodActivity.this, arrayList2);
        if(DataInstance.getInstance().getCaf2()[position].size() == 0)  {
            arrayList2.add(null);
        }
        else {
            for (UnivCaffetteria menu : DataInstance.getInstance().getCaf2()[position]) {
                arrayList2.add(new UnivCaffetteria(menu.getTime(), menu.getMenu()));
            }
        }
        listView2.setAdapter(adapter2);

        ArrayList<UnivCaffetteria> arrayList3 = new ArrayList<>();
        adapter3 = new UnivFoodListAdapter(UnivFoodActivity.this, arrayList3);
        if(DataInstance.getInstance().getCaf3()[position].size() == 0)  {
            arrayList3.add(null);
        }
        else {
            for (UnivCaffetteria menu : DataInstance.getInstance().getCaf3()[position]) {
                arrayList3.add(new UnivCaffetteria(menu.getTime(), menu.getMenu()));
            }
        }
        listView3.setAdapter(adapter3);

        ArrayList<UnivCaffetteria> arrayList4 = new ArrayList<>();
        adapter4 = new UnivFoodListAdapter(UnivFoodActivity.this, arrayList4);
        if(DataInstance.getInstance().getCaf4()[position].size() == 0)  {
            arrayList4.add(null);
        }
        else {
            for (UnivCaffetteria menu : DataInstance.getInstance().getCaf4()[position]) {
                arrayList4.add(new UnivCaffetteria(menu.getTime(), menu.getMenu()));
            }
        }
        listView4.setAdapter(adapter4);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home : {
                finish();
            }
        }
        return true;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right); //slide to left
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        if (check) {
            overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right); //slide to left
            finishAffinity();
            startActivity(new Intent(getApplicationContext(), PreActivity.class));
        }
    }
}
