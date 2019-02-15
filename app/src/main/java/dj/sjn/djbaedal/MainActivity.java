package dj.sjn.djbaedal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

import dj.sjn.djbaedal.Adapter.RecentAdapter;
import dj.sjn.djbaedal.DataClass.CheckNetwork;
import dj.sjn.djbaedal.DataClass.DataInstance;
import dj.sjn.djbaedal.DataClass.list_item;

public class MainActivity extends AppCompatActivity {

    ImageView button1, button2, button3, button4, button5, button6, button7, button8, button9;
    ImageView[] buttons;
    private long lastTimeBackPressed;
    ListView listView;
    RecentAdapter recentAdapter;
    ArrayList<list_item> arrayList;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    public static Context mContext;

    //TODO 스프라이트 이미지
    //TODO DB 이미지 깨끗하게
    //TODO 즐겨찾기 (?)
    //TODO 학식정보 파싱

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_email : {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                try {
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"gkstjrwns123@gmail.com"});
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "문의 내용 : 메뉴 추가 / 버그 / 건의사항\n" +
                            "상세 내용 : \n\n메뉴 추가 메일에는 전화번호와 메뉴가 잘 보이는 사진을 함께 찍어서 보내주세요.");
                    emailIntent.setType("text/html");
                    emailIntent.setPackage("com.google.android.gm");

                    startActivity(emailIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                    emailIntent.setType("text/html");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"gkstjrwns123@gmail.com"});

                    startActivity(Intent.createChooser(emailIntent, "이메일 보내기"));
                }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();

        mContext = this;
        arrayList = new ArrayList<>();

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
        buttons = new ImageView[]{button1, button2, button3, button4, button5, button6, button7, button8, button9};

        Toolbar toolbar = findViewById(R.id.toolbar1);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
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
            recentAdapter = new RecentAdapter(this, arrayList);
            listView.setAdapter(recentAdapter);
            getPreference();
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

            SharedPreferences settings = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor2 = settings.edit();
            String checkFirst = settings.getString("first", null);
            if (checkFirst == null) { // on first execute
                editor2.putString("first", "0");
                editor2.commit();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("앱을 설치해주셔서 감사합니다!")
                        .setMessage("메뉴 추가 및 버그 / 건의사항은 우측상단 Gmail을 이용해주세요!")
                        .setPositiveButton("확인", null)
                        .setCancelable(false);
                AlertDialog firstExeDialog = alertDialogBuilder.create();
                firstExeDialog.show();
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

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - lastTimeBackPressed < 1000) {
            finishAffinity();
            return;
        }
        Toast.makeText(this, "'뒤로'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        lastTimeBackPressed = System.currentTimeMillis();
    }

    //최초 어플리케이션 실행시 정보 받아옴.
    public void getPreference() {
        for (int i = 1; i <= 3; i++) {
            String name = pref.getString("name" + String.valueOf(i), null);
            if (name != null) {
                String img_reg = pref.getString("img_reg" + String.valueOf(i), null);
                String img_reg2 = pref.getString("img_reg2" + String.valueOf(i), null);
                String img_reg3 = pref.getString("img_reg3" + String.valueOf(i), null);
                String tel_no = pref.getString("tel_no" + String.valueOf(i), null);
                arrayList.add(0, new list_item(new String[]{img_reg, img_reg2, img_reg3}, name, tel_no));
                DataInstance.getInstance().getLinkedHashMap().put(name, new list_item(new String[]{img_reg, img_reg2, img_reg3}, name, tel_no));
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
            arrayList.add(0, new list_item(new String[]{img_reg, img_reg2, img_reg3}, name, tel_no));
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
                    editor.putString("name" + String.valueOf(i), name);
                    editor.putString("img_reg" + String.valueOf(i), img_reg);
                    editor.putString("img_reg2" + String.valueOf(i), img_reg2);
                    editor.putString("img_reg3" + String.valueOf(i), img_reg3);
                    editor.putString("tel_no" + String.valueOf(i), tel_no);
                    i++;
                    arrayList.add(0, new list_item(new String[]{img_reg, img_reg2, img_reg3}, name, tel_no));
                }
                editor.commit();
                recentAdapter.notifyChanged();
            }
        });
    }
}
