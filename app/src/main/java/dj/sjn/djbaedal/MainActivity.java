package dj.sjn.djbaedal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Debug;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.Random;

import dj.sjn.djbaedal.Adapter.RecentAdapter;
import dj.sjn.djbaedal.DataClass.CheckNetwork;
import dj.sjn.djbaedal.DataClass.DataInstance;
import dj.sjn.djbaedal.DataClass.list_item;

public class MainActivity extends AppCompatActivity {

    ImageView button1, button2, button3, button4, button5, button6, button7, button8, button9, schoolfood, randomFood;
    ImageView[] buttons;
    dj.sjn.djbaedal.DataClass.MyListView listView;
    RecentAdapter recentAdapter;
    ArrayList<list_item> arrayList;
    AdView adView;
    private long lastTimeBackPressed;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();

        mContext = this;
        arrayList = new ArrayList<>();

        MobileAds.initialize(this, getString(R.string.admob_id));

        listView = findViewById(R.id.recentList);
        ScrollView scrollView = findViewById(R.id.scrollView);
        button1 = findViewById(R.id.chicken);
        button2 = findViewById(R.id.pizza);
        button3 = findViewById(R.id.chinese);
        button4 = findViewById(R.id.schoolfood);
        button5 = findViewById(R.id.jokbo);
        button6 = findViewById(R.id.korean);
        button7 = findViewById(R.id.hamburger);
        button8 = findViewById(R.id.soup);
        button9 = findViewById(R.id.night);
        schoolfood = findViewById(R.id.haksik);
        buttons = new ImageView[]{button1, button2, button3, button4, button5, button6, button7, button8, button9};
        adView = findViewById(R.id.adBanner);
        randomFood = findViewById(R.id.randomFood);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.gmail);
        scrollView.smoothScrollBy(0, 0);

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
            MobileAds.initialize(this, getString(R.string.admob_id));
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
            recentAdapter = new RecentAdapter(this, arrayList);
            listView.setAdapter(recentAdapter);
            getPreference(); //Load recent_list.
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getApplicationContext(), Main3Activity.class);
                    final String img_reg = arrayList.get(position).getImage()[0];
                    final String img_reg2 = arrayList.get(position).getImage()[1];
                    final String img_reg3 = arrayList.get(position).getImage()[2];
                    final String name = arrayList.get(position).getName();
                    final String tel_no = arrayList.get(position).getTel_no();
                    final String type = arrayList.get(position).getType();
                    final String extra_text = arrayList.get(position).getExtra_text();
                    final String thumbnail = arrayList.get(position).getThumbnail();
                    final String time = arrayList.get(position).getTime();
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

            // Add Listener to Image 1 ~ 9
            for (int i = 0; i < buttons.length; i++) {
                final int num = i;
                buttons[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                        intent.putExtra("num", num + 1);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                    }
                });
            }
            schoolfood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), UnivFoodActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                }
            });
            randomFood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Random random = new Random();
                    int randomNumber = random.nextInt(DataInstance.getInstance().getSearch_list().size());

                    Intent intent = new Intent(getApplicationContext(), Main3Activity.class);
                    final String img_reg = DataInstance.getInstance().getSearch_list().get(randomNumber).getImage()[0];
                    final String img_reg2 = DataInstance.getInstance().getSearch_list().get(randomNumber).getImage()[1];
                    final String img_reg3 =  DataInstance.getInstance().getSearch_list().get(randomNumber).getImage()[2];
                    final String name =  DataInstance.getInstance().getSearch_list().get(randomNumber).getName();
                    final String tel_no =  DataInstance.getInstance().getSearch_list().get(randomNumber).getTel_no();
                    final String type =  DataInstance.getInstance().getSearch_list().get(randomNumber).getType();
                    final String extra_text =  DataInstance.getInstance().getSearch_list().get(randomNumber).getExtra_text();
                    final String thumbnail =  DataInstance.getInstance().getSearch_list().get(randomNumber).getThumbnail();
                    final String time =  DataInstance.getInstance().getSearch_list().get(randomNumber).getTime();

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

            SharedPreferences settings = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor2 = settings.edit();
            String checkFirst = settings.getString("first", null);
            if (checkFirst == null) { // on first execute
                editor2.putString("first", "0");
                editor2.commit();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("앱을 설치해주셔서 감사합니다!")
                        .setMessage("메뉴 추가 및 버그 / 건의사항은 좌측상단 Gmail을 이용해주세요!")
                        .setPositiveButton("확인", null)
                        .setCancelable(false);
                AlertDialog firstExeDialog = alertDialogBuilder.create();
                firstExeDialog.show();
            }

            Calendar cal = Calendar.getInstance(); // today
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String today = simpleDateFormat.format(cal.getTime());
            cal.add(Calendar.DATE, 1);
            String d2 = "";
            d2 = simpleDateFormat.format(cal.getTime());
            String checkReview = settings.getString("review", "null");
            Log.e(today, d2);
            Log.e("checkReview", checkReview);
            if (checkReview.equals(today)) {
                Log.e("생성", "아무튼 생성됨");
                editor2.putString("review", "done");
                editor2.commit();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("앱의 사용 후기를 말씀해주세요")
                        .setMessage("좋은 리뷰가 더 좋은 앱을 만듭니다.\n리뷰를 남겨주세요!")
                        .setCancelable(false)
                        .setPositiveButton("좋아요", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final String appPackageName = getPackageName();
                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                } catch (android.content.ActivityNotFoundException anf_e) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                }
                            }
                        })
                        .setNegativeButton("싫어요", null);
                AlertDialog reviewDialog = alertDialogBuilder.create();
                reviewDialog.show();
            }
            else if(checkReview.equals("null")) {
                Log.e("없어서 만듬", "암튼 만듬");
                editor2.putString("review", d2);
                editor2.commit();
            }
        }
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

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - lastTimeBackPressed < 750) {
            finishAffinity();
            return;
        }
        Toast.makeText(this, "종료하시려면 '뒤로' 버튼을 한번 더 눌러주세요.", Toast.LENGTH_SHORT).show();
        lastTimeBackPressed = System.currentTimeMillis();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                try {
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"byeolsoft@gmail.com"});
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "" +
                            "\n\n\n==============================\n오늘뭐드실? 앱을 이용해주셔서 감사합니다!\n" +
                            "전단지 추가를 원하신다면 전화번호와 메뉴가 잘 보이게 사진을 찍어서 보내주세요.\n" +
                            "오류 발생시에는 해결을 위해 오류 발생 상황에 대한 자세한 설명을 부탁드립니다.");
                    emailIntent.setType("text/html");
                    emailIntent.setPackage("com.google.android.gm");

                    startActivity(emailIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                    emailIntent.setType("text/html");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"byeolsoft@gmail.com"});

                    startActivity(Intent.createChooser(emailIntent, "이메일 보내기"));
                }
                return true;
            }
            case R.id.action_bookmark: {
                Intent intent = new Intent(getApplicationContext(), BookmarkActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                return true;
            }
            case R.id.action_search: {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    public void getPreference() {
        for (int i = 1; i <= 3; i++) {
            String name = pref.getString("name" + String.valueOf(i), null);
            if (name != null) {
                String img_reg = pref.getString("img_reg" + String.valueOf(i), null);
                String img_reg2 = pref.getString("img_reg2" + String.valueOf(i), null);
                String img_reg3 = pref.getString("img_reg3" + String.valueOf(i), null);
                String tel_no = pref.getString("tel_no" + String.valueOf(i), null);
                String type = pref.getString("type" + String.valueOf(i), null);
                String extra_text = pref.getString("extra_text" + String.valueOf(i), null);
                String thumbnail = pref.getString("thumbnail" + String.valueOf(i), null);
                String time = pref.getString("time" + String.valueOf(i), null);
                arrayList.add(0, new list_item(new String[]{img_reg, img_reg2, img_reg3}, name, tel_no, type, extra_text, thumbnail, time));
                DataInstance.getInstance().getLinkedHashMap().put(name, new list_item(new String[]{img_reg, img_reg2, img_reg3}, name, tel_no, type, extra_text, thumbnail, time));
            }
        }
        recentAdapter.notifyChanged();
    }

    public void addList() {
        arrayList.clear();
        for (Map.Entry<String, list_item> entry : DataInstance.getInstance().getLinkedHashMap().entrySet()) {
            String img_reg = entry.getValue().getImage()[0];
            String img_reg2 = entry.getValue().getImage()[1];
            String img_reg3 = entry.getValue().getImage()[2];
            String name = entry.getValue().getName();
            String tel_no = entry.getValue().getTel_no();
            String type = entry.getValue().getType();
            String extra_text = entry.getValue().getExtra_text();
            String thumbnail = entry.getValue().getThumbnail();
            String time = entry.getValue().getTime();
            arrayList.add(0, new list_item(new String[]{img_reg, img_reg2, img_reg3}, name, tel_no, type, extra_text, thumbnail, time));
        }
        recentAdapter.notifyChanged();
    }

    public void deleteRecentList() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                editor.clear();
                editor.commit();
                arrayList.clear();

                int i = 1;
                for (Map.Entry<String, list_item> entry : DataInstance.getInstance().getLinkedHashMap().entrySet()) {
                    String name = entry.getValue().getName();
                    String img_reg = entry.getValue().getImage()[0];
                    String img_reg2 = entry.getValue().getImage()[1];
                    String img_reg3 = entry.getValue().getImage()[2];
                    String tel_no = entry.getValue().getTel_no();
                    String type = entry.getValue().getType();
                    String extra_text = entry.getValue().getExtra_text();
                    String thumbnail = entry.getValue().getThumbnail();
                    String time = entry.getValue().getTime();
                    editor.putString("name" + String.valueOf(i), name);
                    editor.putString("img_reg" + String.valueOf(i), img_reg);
                    editor.putString("img_reg2" + String.valueOf(i), img_reg2);
                    editor.putString("img_reg3" + String.valueOf(i), img_reg3);
                    editor.putString("tel_no" + String.valueOf(i), tel_no);
                    editor.putString("type" + String.valueOf(i), type);
                    editor.putString("extra_text" + String.valueOf(i), extra_text);
                    editor.putString("thumbnail" + String.valueOf(i), thumbnail);
                    editor.putString("time" + String.valueOf(i), time);
                    i++;
                    arrayList.add(0, new list_item(new String[]{img_reg, img_reg2, img_reg3}, name, tel_no, type, extra_text, thumbnail, time));
                }
                editor.commit();
                recentAdapter.notifyChanged();
            }
        });
    }
}
