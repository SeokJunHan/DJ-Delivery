package dj.sjn.djbaedal;

import android.app.AlertDialog;
import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

import javax.annotation.Nonnull;

import dj.sjn.djbaedal.Adapter.ListAdapter;
import dj.sjn.djbaedal.DataClass.CheckNetwork;
import dj.sjn.djbaedal.DataClass.DataInstance;
import dj.sjn.djbaedal.DataClass.list_item;

public class Main2Activity extends AppCompatActivity {

    ListView listView;
    ListAdapter listAdapter;
    ProgressBar progressBar;
    ArrayList<list_item> list_itemArrayList;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Toolbar toolbar;
    public static Context mContext;
    int reviewCount, reviewSum;

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
        mContext = this;

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();

//        listView.setOverScrollMode(View.OVER_SCROLL_IF_CONTENT_SCROLLS);
//        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag && mLockListView == false) {
//                    progressBar.setVisibility(View.VISIBLE);
//                    getItem();
//                }
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
//            }
//        });

        list_itemArrayList = new ArrayList<>();

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
                        list_itemArrayList.add(listItem);
                    break;
                case 2:
                    title.setText("피자");
                    for (list_item listItem : DataInstance.getInstance().getList2())
                        list_itemArrayList.add(listItem);
                    break;
                case 3:
                    title.setText("중식");
                    for (list_item listItem : DataInstance.getInstance().getList3())
                        list_itemArrayList.add(listItem);
                    break;
                case 4:
                    title.setText("분식");
                    for (list_item listItem : DataInstance.getInstance().getList4())
                        list_itemArrayList.add(listItem);
                    break;
                case 5:
                    title.setText("족·보");
                    for (list_item listItem : DataInstance.getInstance().getList5())
                        list_itemArrayList.add(listItem);
                    break;
                case 6:
                    title.setText("한식");
                    for (list_item listItem : DataInstance.getInstance().getList6())
                        list_itemArrayList.add(listItem);
                    break;
                case 7:
                    title.setText("햄버거");
                    for (list_item listItem : DataInstance.getInstance().getList7())
                        list_itemArrayList.add(listItem);
                    break;
                case 8:
                    title.setText("찜·탕");
                    for (list_item listItem : DataInstance.getInstance().getList8())
                        list_itemArrayList.add(listItem);
                    break;
                case 9:
                    title.setText("야식");
                    for (list_item listItem : DataInstance.getInstance().getList9())
                        list_itemArrayList.add(listItem);
                    break;
            }

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            for(int i=0; i<list_itemArrayList.size(); i++) {
                final int j = i;
                list_itemArrayList.get(i).setRate("평점 : -");
                db.collection("review").document(list_itemArrayList.get(i).getName()).collection("review").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@Nonnull Task<QuerySnapshot> task) {
                        reviewCount = 0;
                        reviewSum = 0;
                        if(task.isSuccessful()) {
                            for(DocumentSnapshot document : task.getResult()) {
                                int rates;
                                try {
                                    rates = Integer.parseInt(document.getData().get("rates").toString());
                                    reviewSum += rates;
                                    reviewCount++;
                                } catch (NullPointerException null_e) {}
                            }
                            if(reviewCount != 0)
                            list_itemArrayList.get(j).setRate("평점 : " + String.format("%.2f", (double)reviewSum/reviewCount) + " ( " + reviewCount + "개의 리뷰가 있습니다 )");
                            listAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }



            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getItem();
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

    public void renewRate(String name, String text) {
        for(int i=0; i<list_itemArrayList.size(); i++) {
            if(name.equals(list_itemArrayList.get(i).getName())){
                list_itemArrayList.get(i).setRate(text);
                listAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

//    private void getItem() {
//        mLockListView = true;
//
//        if (list_itemArrayList.size() == temp_itemArrayList.size()) {
//            if(check)
//                Toast.makeText(getApplicationContext(), "더 이상 항목이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
//        }
//
//        for (int i = 0; i < OFFSET; i++) {
//            if (temp_itemArrayList.size() > page * OFFSET + i)
//                list_itemArrayList.add(temp_itemArrayList.get((page * OFFSET) + i));
//        }
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                page++;
//                listAdapter.notifyDataSetChanged();
//                progressBar.setVisibility(View.GONE);
//                mLockListView = false;
//                check = true;
//            }
//        }, 700);
//    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right); //slide to left
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Util.CheckFoodList()) {
            overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right); //slide to left
            finishAffinity();
            startActivity(new Intent(getApplicationContext(), PreActivity.class));
        }
    }

}