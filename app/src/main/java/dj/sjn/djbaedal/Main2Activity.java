package dj.sjn.djbaedal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

import dj.sjn.djbaedal.Adapter.ListAdapter;
import dj.sjn.djbaedal.DataClass.CheckNetwork;
import dj.sjn.djbaedal.DataClass.DataInstance;
import dj.sjn.djbaedal.DataClass.list_item;

public class Main2Activity extends AppCompatActivity {

    ListView listView;
    ListAdapter listAdapter;
    ProgressBar progressBar;
    ArrayList<list_item> list_itemArrayList, temp_itemArrayList;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Boolean lastItemVisibleFlag = false;
    Toolbar toolbar;

    int page = 0;
    final int OFFSET = 10;
    final int MAX_IMAGES = 3; // Main3's images limit.
    boolean mLockListView = false; // 데이터 불러올때 중복 안되게 하는 변수.
    boolean check = false;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home : {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        listView = findViewById(R.id.listView);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        toolbar = findViewById(R.id.toolbar2);
        TextView title = findViewById(R.id.toolbar2_title);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();

        listView.setOverScrollMode(View.OVER_SCROLL_IF_CONTENT_SCROLLS);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag && mLockListView == false) {
                    progressBar.setVisibility(View.VISIBLE);
                    getItem();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
            }
        });

        list_itemArrayList = new ArrayList<>();
        temp_itemArrayList = new ArrayList<>();

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
        } else {
            Intent getIntent = getIntent();
            int num = getIntent.getExtras().getInt("num");

            switch (num) {
                case 1:
                    title.setText("치킨");
                    for (list_item listItem : DataInstance.getInstance().getList1())
                        temp_itemArrayList.add(listItem);
                    break;
                case 2:
                    title.setText("피자");
                    for (list_item listItem : DataInstance.getInstance().getList2())
                        temp_itemArrayList.add(listItem);
                    break;
                case 3:
                    title.setText("중식");
                    for (list_item listItem : DataInstance.getInstance().getList3())
                        temp_itemArrayList.add(listItem);
                    break;
                case 4:
                    title.setText("분식");
                    for (list_item listItem : DataInstance.getInstance().getList4())
                        temp_itemArrayList.add(listItem);
                    break;
                case 5:
                    title.setText("족·보");
                    for (list_item listItem : DataInstance.getInstance().getList5())
                        temp_itemArrayList.add(listItem);
                    break;
                case 6:
                    title.setText("한식");
                    for (list_item listItem : DataInstance.getInstance().getList6())
                        temp_itemArrayList.add(listItem);
                    break;
                case 7:
                    title.setText("햄버거");
                    for (list_item listItem : DataInstance.getInstance().getList7())
                        temp_itemArrayList.add(listItem);
                    break;
                case 8:
                    title.setText("찜·탕");
                    for (list_item listItem : DataInstance.getInstance().getList8())
                        temp_itemArrayList.add(listItem);
                    break;
                case 9:
                    title.setText("야식");
                    for (list_item listItem : DataInstance.getInstance().getList9())
                        temp_itemArrayList.add(listItem);
                    break;
            }

            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getItem();
            listAdapter = new ListAdapter(Main2Activity.this, list_itemArrayList);
            listView.setAdapter(listAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getApplicationContext(), Main3Activity.class);
                    final String img_reg = list_itemArrayList.get(position).getImage()[0];
                    final String img_reg2 = list_itemArrayList.get(position).getImage()[1];
                    final String img_reg3 = list_itemArrayList.get(position).getImage()[2];
                    final String name = list_itemArrayList.get(position).getName();
                    final String tel_no = list_itemArrayList.get(position).getTel_no();
                    final String type = list_itemArrayList.get(position).getType();
                    final String extra_text = list_itemArrayList.get(position).getExtra_text();
                    final String thumbnail = list_itemArrayList.get(position).getThumbnail();
                    final String time = list_itemArrayList.get(position).getTime();

                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            itemClicked(new String[] {img_reg, img_reg2, img_reg3}, name, tel_no, type, extra_text, thumbnail, time);
                        }
                    });
                    intent.putExtra("img_reg", img_reg);
                    intent.putExtra("img_reg2", img_reg2);
                    intent.putExtra("img_reg3", img_reg3);
                    intent.putExtra("name", name);
                    intent.putExtra("tel_no", tel_no);
                    intent.putExtra("type", type);
                    intent.putExtra("extra_text", extra_text);
                    intent.putExtra("thumbnail", thumbnail);
                    intent.putExtra("time", time);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                }
            });
        }
    }

    private void getItem() {
        mLockListView = true;

        if (list_itemArrayList.size() == temp_itemArrayList.size()) {
            if(check)
                Toast.makeText(getApplicationContext(), "더 이상 항목이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
        }

        for (int i = 0; i < OFFSET; i++) {
            if (temp_itemArrayList.size() > page * OFFSET + i)
                list_itemArrayList.add(temp_itemArrayList.get((page * OFFSET) + i));
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                page++;
                listAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                mLockListView = false;
                check = true;
            }
        }, 700);
    }

    private void itemClicked(String[] img_reg, String name, String tel_no, String type, String extra_text, String thumbnail, String time) {
        //중복 데이터 존재
        if (DataInstance.getInstance().getLinkedHashMap().get(name) != null) {
            if (DataInstance.getInstance().getLinkedHashMap().get(name).getName().equals(name))
                DataInstance.getInstance().getLinkedHashMap().remove(name);
        } else if (DataInstance.getInstance().getLinkedHashMap().size() >= MAX_IMAGES + 1) {
            for (Map.Entry<String, list_item> entry : DataInstance.getInstance().getLinkedHashMap().entrySet()) {
                DataInstance.getInstance().getLinkedHashMap().remove(entry.getValue().getName());
                break;
            }
        }
        DataInstance.getInstance().getLinkedHashMap().put(name, new list_item(img_reg, name, tel_no, type, extra_text, thumbnail, time));

        //set pref.
        editor.clear();
        int i = 1;
        for (Map.Entry<String, list_item> entry : DataInstance.getInstance().getLinkedHashMap().entrySet()) {
            editor.putString("img_reg" + String.valueOf(i), entry.getValue().getImage()[0]);
            editor.putString("img_reg2" + String.valueOf(i), entry.getValue().getImage()[1]);
            editor.putString("img_reg3" + String.valueOf(i), entry.getValue().getImage()[2]);
            editor.putString("name" + String.valueOf(i), entry.getValue().getName());
            editor.putString("tel_no" + String.valueOf(i), entry.getValue().getTel_no());
            editor.putString("type" + String.valueOf(i), entry.getValue().getType());
            editor.putString("extra_text" + String.valueOf(i), entry.getValue().getExtra_text());
            editor.putString("thumbnail" + String.valueOf(i), entry.getValue().getThumbnail());
            editor.putString("time" + String.valueOf(i), entry.getValue().getTime());
            i++;
        }
        editor.commit();
        ((MainActivity) MainActivity.mContext).addList();
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