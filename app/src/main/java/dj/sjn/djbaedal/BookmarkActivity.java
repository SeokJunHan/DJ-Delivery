package dj.sjn.djbaedal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Map;

import dj.sjn.djbaedal.Adapter.ListAdapter;
import dj.sjn.djbaedal.DataClass.CheckNetwork;
import dj.sjn.djbaedal.DataClass.DataInstance;
import dj.sjn.djbaedal.DataClass.list_item;

public class BookmarkActivity extends AppCompatActivity {

    ListView listView;
    Toolbar toolbar;
    ArrayList<list_item> arrayList;
    ListAdapter listAdapter;
    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        toolbar = findViewById(R.id.toolbarb);
        listView = findViewById(R.id.bookmarkList);
        mContext = this;

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
        else {
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            arrayList = new ArrayList<>();
            for(Map.Entry<String, list_item> entry : DataInstance.getInstance().getLinkedHashMap2().entrySet()) {
                String img_reg = entry.getValue().getImage()[0];
                String img_reg2 = entry.getValue().getImage()[1];
                String img_reg3 = entry.getValue().getImage()[2];
                String name = entry.getValue().getName();
                String tel_no = entry.getValue().getTel_no();
                arrayList.add(new list_item(new String[]{img_reg, img_reg2, img_reg3}, name, tel_no));
            }
            listAdapter = new ListAdapter(BookmarkActivity.this, arrayList);
            listView.setAdapter(listAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getApplicationContext(), Main3Activity.class);
                    final String img_reg = arrayList.get(position).getImage()[0];
                    final String img_reg2 = arrayList.get(position).getImage()[1];
                    final String img_reg3 = arrayList.get(position).getImage()[2];
                    final String name = arrayList.get(position).getName();
                    final String tel_no = arrayList.get(position).getTel_no();

                    intent.putExtra("img_reg", img_reg);
                    intent.putExtra("img_reg2", img_reg2);
                    intent.putExtra("img_reg3", img_reg3);
                    intent.putExtra("name", name);
                    intent.putExtra("tel_no", tel_no);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                }
            });
        }
    }

    public void deleteList (String name) {
        for(list_item item : arrayList) {
            if(item.getName().equals(name)) {
                arrayList.remove(item);
                break;
            }
        }
        listAdapter.notifyChanged();
    }

    public void addList (list_item item) {
        arrayList.add(0, item);
        listAdapter.notifyChanged();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right); //slide to left
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
    protected void onResume() {
        super.onResume();
        boolean check = false;
        if (DataInstance.getInstance().getList1().size() == 0)
            check = true;
        else if (DataInstance.getInstance().getList2().size() == 0)
            check = true;
        else if (DataInstance.getInstance().getList2().size() == 0)
            check = true;
        else if (DataInstance.getInstance().getList2().size() == 0)
            check = true;
        else if (DataInstance.getInstance().getList2().size() == 0)
            check = true;
        else if (DataInstance.getInstance().getList2().size() == 0)
            check = true;
        else if (DataInstance.getInstance().getList2().size() == 0)
            check = true;
        else if (DataInstance.getInstance().getList2().size() == 0)
            check = true;
        else if (DataInstance.getInstance().getList2().size() == 0)
            check = true;
        if (check) {
            overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right); //slide to left
            finishAffinity();
            startActivity(new Intent(getApplicationContext(), PreActivity.class));
        }
    }
}
