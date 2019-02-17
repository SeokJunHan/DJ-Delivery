package dj.sjn.djbaedal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

import dj.sjn.djbaedal.DataClass.CheckNetwork;
import dj.sjn.djbaedal.DataClass.DataInstance;
import dj.sjn.djbaedal.DataClass.list_item;

//TODO 학식 데이터 파싱 (밥랩)

public class PreActivity extends AppCompatActivity {

    int num;
    FirebaseFirestore db;
    String[] category;
    boolean check;
    TextView preCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre);
        db = FirebaseFirestore.getInstance();
        db.enableNetwork();
        category = new String[]{"1_chicken", "2_pizza", "3_chinese", "4_schoolfood", "5_jokbo", "6_korean", "7_hamburger", "8_soup", "9_night"};
        check = false;
        preCount = findViewById(R.id.preCount);

        //check network status.
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

        preloadGlideImage();
        loadListFromFirebase();
        loadBookMarkList();

        new Thread(new Runnable() {
            @Override
            public void run() {
                checkDataAlready();
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        db.disableNetwork();
                        recreate();
                    }
                });
            }
        }).start();
    }

    private void preloadGlideImage() {

        for (int i = 0; i < category.length; i++) {
            num = 0;
            db.collection(category[i]).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        loop:
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            try {
                                if (num++ > 5)
                                    break loop;
                                String img_reg = document.getData().get("img_reg").toString();
                                Glide.with(getApplicationContext())
                                        .load(img_reg)
                                        .thumbnail(Glide.with(getApplicationContext())
                                                .load("https://t1.daumcdn.net/cfile/tistory/995168475C4DA4702A")
                                                .apply(new RequestOptions().override(250, 250).circleCrop().diskCacheStrategy(DiskCacheStrategy.ALL).priority(Priority.HIGH)))
                                        .apply(new RequestOptions().circleCrop().diskCacheStrategy(DiskCacheStrategy.ALL))
                                        .preload(250, 250);
                            } catch (NullPointerException e) {
                            }
                        }
                    }
                }
            });
        }
    }

    private void loadBookMarkList() {
        DataInstance.getInstance().getLinkedHashMap2().clear();
        SharedPreferences pref = getSharedPreferences("bookmark",MODE_PRIVATE);
        Map<String, ?> prefsMap = pref.getAll();
        for(Map.Entry<String, ?> entry : prefsMap.entrySet()) {
            try {
                String name = entry.getValue().toString().split("~")[0];
                String tel_no = entry.getValue().toString().split("~")[1];
                String img_reg = entry.getValue().toString().split("~")[2];
                if (img_reg.equals("null"))
                    img_reg = null;
                String img_reg2 = entry.getValue().toString().split("~")[3];
                if (img_reg2.equals("null"))
                    img_reg2 = null;
                String img_reg3 = entry.getValue().toString().split("~")[4];
                if (img_reg3.equals("null"))
                    img_reg3 = null;
                DataInstance.getInstance().getLinkedHashMap2().put(name, new list_item(new String[]{img_reg, img_reg2, img_reg3}, name, tel_no));
            } catch (ArrayIndexOutOfBoundsException e) {
                Toast.makeText(getApplicationContext(), "데이터를 불러오는 도중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();}
        }
        Log.e("prefs", prefsMap.toString());
    }

    private void loadListFromFirebase() {
        //TODO DataInstance.getInstance().getList1() 등 List 접근시 그 값이 기억됨.
        DataInstance.getInstance().getList1().clear();
        DataInstance.getInstance().getList2().clear();
        DataInstance.getInstance().getList3().clear();
        DataInstance.getInstance().getList4().clear();
        DataInstance.getInstance().getList5().clear();
        DataInstance.getInstance().getList6().clear();
        DataInstance.getInstance().getList7().clear();
        DataInstance.getInstance().getList8().clear();
        DataInstance.getInstance().getList9().clear();

        for (int i = 0; i < category.length; i++) {
            final int num2 = i;
            db.collection(category[i]).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String[] img_regs = new String[3];
                            img_regs[0] = (document.getData().get("img_reg").toString());
                            try {
                                if (document.getData().get("img_reg2").toString() != null) {
                                    img_regs[1] = document.getData().get("img_reg2").toString();
                                    if (document.getData().get("img_reg3").toString() != null)
                                        img_regs[2] = document.getData().get("img_reg3").toString();
                                }
                            } catch (NullPointerException e) {
                            }
                            String name = document.getData().get("name").toString();
                            String tel_no = document.getData().get("tel_no").toString();
                            switch (num2) {
                                case 0:
                                    DataInstance.getInstance().getList1().add(new list_item(img_regs, name, tel_no));
                                    break;
                                case 1:
                                    DataInstance.getInstance().getList2().add(new list_item(img_regs, name, tel_no));
                                    break;
                                case 2:
                                    DataInstance.getInstance().getList3().add(new list_item(img_regs, name, tel_no));
                                    break;
                                case 3:
                                    DataInstance.getInstance().getList4().add(new list_item(img_regs, name, tel_no));
                                    break;
                                case 4:
                                    DataInstance.getInstance().getList5().add(new list_item(img_regs, name, tel_no));
                                    break;
                                case 5:
                                    DataInstance.getInstance().getList6().add(new list_item(img_regs, name, tel_no));
                                    break;
                                case 6:
                                    DataInstance.getInstance().getList7().add(new list_item(img_regs, name, tel_no));
                                    break;
                                case 7:
                                    DataInstance.getInstance().getList8().add(new list_item(img_regs, name, tel_no));
                                    break;
                                case 8:
                                    DataInstance.getInstance().getList9().add(new list_item(img_regs, name, tel_no));
                                    break;
                            }
                        }
                    }
                }
            });
        }
    }

    private void checkDataAlready() {
        //전설의 9중 if문
        for (int i = 0; ; i++) {
            preCount.setText(String.valueOf(i));
            if (DataInstance.getInstance().getList1().size() > 0) {
                if (DataInstance.getInstance().getList2().size() > 0) {
                    if (DataInstance.getInstance().getList3().size() > 0) {
                        if (DataInstance.getInstance().getList4().size() > 0) {
                            if (DataInstance.getInstance().getList5().size() > 0) {
                                if (DataInstance.getInstance().getList6().size() > 0) {
                                    if (DataInstance.getInstance().getList7().size() > 0) {
                                        if (DataInstance.getInstance().getList8().size() > 0) {
                                            if (DataInstance.getInstance().getList9().size() > 0) {
                                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                                                finish();
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            try {
                Log.e("count", String.valueOf(i));
                Thread.sleep(200);
            } catch (Exception e) {
            }
        }
    }
}
