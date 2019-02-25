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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;

import dj.sjn.djbaedal.DataClass.CheckNetwork;
import dj.sjn.djbaedal.DataClass.DataInstance;
import dj.sjn.djbaedal.DataClass.UnivCaffetteria;
import dj.sjn.djbaedal.DataClass.UnivFoodInterface;
import dj.sjn.djbaedal.DataClass.UnivFoodList;
import dj.sjn.djbaedal.DataClass.list_item;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//TODO 학식 데이터 파싱 (밥랩)

public class PreActivity extends AppCompatActivity {

    int num;
    FirebaseFirestore db;
    String[] category, days;
    boolean check;
    boolean checkResponse1 = false;
    boolean checkResponse2 = false;
    boolean checkResponse3 = false;
    boolean checkResponse4 = false;
    TextView preText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre);
        db = FirebaseFirestore.getInstance();
        category = new String[]{"1_chicken", "2_pizza", "3_chinese", "4_schoolfood", "5_jokbo", "6_korean", "7_hamburger", "8_soup", "9_night"};
        check = false;
        preText = findViewById(R.id.preText);

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
        LoadUnivFoodData();

        new Thread(new Runnable() {
            @Override
            public void run() {
                checkDataAlready();
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
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
        SharedPreferences pref = getSharedPreferences("bookmark", MODE_PRIVATE);
        Map<String, ?> prefsMap = pref.getAll();
        for (Map.Entry<String, ?> entry : prefsMap.entrySet()) {
            try {
                String name = entry.getValue().toString().split("@")[0];
                String tel_no = entry.getValue().toString().split("@")[1];
                String type = entry.getValue().toString().split("@")[2];
                String extra_text = entry.getValue().toString().split("@")[3];
                String thumbnail = entry.getValue().toString().split("@")[4];
                String time = entry.getValue().toString().split("@")[5];
                String img_reg = entry.getValue().toString().split("@")[6];
                if (img_reg.equals("null"))
                    img_reg = null;
                String img_reg2 = entry.getValue().toString().split("@")[7];
                if (img_reg2.equals("null"))
                    img_reg2 = null;
                String img_reg3 = entry.getValue().toString().split("@")[8];
                if (img_reg3.equals("null"))
                    img_reg3 = null;
                DataInstance.getInstance().getLinkedHashMap2().put(name, new list_item(new String[]{img_reg, img_reg2, img_reg3}, name, tel_no, type, extra_text, thumbnail, time));
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "데이터를 불러오는 도중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }
        }
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
                            String type = document.getData().get("type").toString();
                            String extra_text;
                            try {
                                extra_text = document.getData().get("extra_text").toString();
                            } catch (NullPointerException e_et) {
                                extra_text = null;
                            }
                            String time;
                            try {
                                time = document.getData().get("time").toString();
                            } catch (NullPointerException e_time) {
                                time = null;
                            }
                            String thumbnail;
                            switch (num2) {
                                case 0:
                                    try {
                                        thumbnail = document.getData().get("thumbnail").toString();
                                    } catch (NullPointerException e) {
                                        thumbnail = getString(R.string.thumbnail_1);
                                    }
                                    DataInstance.getInstance().getList1().add(new list_item(img_regs, name, tel_no, type, extra_text, thumbnail, time));
                                    break;
                                case 1:
                                    try {
                                        thumbnail = document.getData().get("thumbnail").toString();
                                    } catch (NullPointerException e) {
                                        thumbnail = getString(R.string.thumbnail_2);
                                    }
                                    DataInstance.getInstance().getList2().add(new list_item(img_regs, name, tel_no, type, extra_text, thumbnail, time));
                                    break;
                                case 2:
                                    try {
                                        thumbnail = document.getData().get("thumbnail").toString();
                                    } catch (NullPointerException e) {
                                        thumbnail = getString(R.string.thumbnail_3);
                                    }
                                    DataInstance.getInstance().getList3().add(new list_item(img_regs, name, tel_no, type, extra_text, thumbnail, time));
                                    break;
                                case 3:
                                    try {
                                        thumbnail = document.getData().get("thumbnail").toString();
                                    } catch (NullPointerException e) {
                                        thumbnail = getString(R.string.thumbnail_4);
                                    }
                                    DataInstance.getInstance().getList4().add(new list_item(img_regs, name, tel_no, type, extra_text, thumbnail, time));
                                    break;
                                case 4:
                                    try {
                                        thumbnail = document.getData().get("thumbnail").toString();
                                    } catch (NullPointerException e) {
                                        thumbnail = getString(R.string.thumbnail_5);
                                    }
                                    DataInstance.getInstance().getList5().add(new list_item(img_regs, name, tel_no, type, extra_text, thumbnail, time));
                                    break;
                                case 5:
                                    try {
                                        thumbnail = document.getData().get("thumbnail").toString();
                                    } catch (NullPointerException e) {
                                        thumbnail = getString(R.string.thumbnail_6);
                                    }
                                    DataInstance.getInstance().getList6().add(new list_item(img_regs, name, tel_no, type, extra_text, thumbnail, time));
                                    break;
                                case 6:
                                    try {
                                        thumbnail = document.getData().get("thumbnail").toString();
                                    } catch (NullPointerException e) {
                                        thumbnail = getString(R.string.thumbnail_7);
                                    }
                                    DataInstance.getInstance().getList7().add(new list_item(img_regs, name, tel_no, type, extra_text, thumbnail, time));
                                    break;
                                case 7:
                                    try {
                                        thumbnail = document.getData().get("thumbnail").toString();
                                    } catch (NullPointerException e) {
                                        thumbnail = getString(R.string.thumbnail_8);
                                    }
                                    DataInstance.getInstance().getList8().add(new list_item(img_regs, name, tel_no, type, extra_text, thumbnail, time));
                                    break;
                                case 8:
                                    try {
                                        thumbnail = document.getData().get("thumbnail").toString();
                                    } catch (NullPointerException e) {
                                        thumbnail = getString(R.string.thumbnail_9);
                                    }
                                    DataInstance.getInstance().getList9().add(new list_item(img_regs, name, tel_no, type, extra_text, thumbnail, time));
                                    break;
                            }
                        }
                    }
                }
            });
        }
    }

    private void checkDataAlready() {
        while (true) {
            if (DataInstance.getInstance().getList1().size() > 0
                    && DataInstance.getInstance().getList2().size() > 0
                    && DataInstance.getInstance().getList3().size() > 0
                    && DataInstance.getInstance().getList4().size() > 0
                    && DataInstance.getInstance().getList5().size() > 0
                    && DataInstance.getInstance().getList6().size() > 0
                    && DataInstance.getInstance().getList7().size() > 0
                    && DataInstance.getInstance().getList8().size() > 0
                    && DataInstance.getInstance().getList9().size() > 0) {
                if (checkResponse1 && checkResponse2 && checkResponse3 && checkResponse4) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                    finish();
                    break;
                }
            }
            try {
                Thread.sleep(200);
            } catch (Exception e) {}
        }
    }

    private void LoadUnivFoodData() {
        for (int i = 0; i <= 7; i++) {
            DataInstance.getInstance().getCaf1()[i].clear();
            DataInstance.getInstance().getCaf2()[i].clear();
            DataInstance.getInstance().getCaf3()[i].clear();
            DataInstance.getInstance().getCaf4()[i].clear();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://bablabs.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UnivFoodInterface foodInterface = retrofit.create(UnivFoodInterface.class);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        days = new String[5];
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        days[0] = simpleDateFormat.format(cal.getTime());
        for (int i = 1; i < 5; i++) {
            cal.add(Calendar.DATE, 1);
            days[i] = simpleDateFormat.format(cal.getTime());
        }
        DataInstance.getInstance().setDays(days);

            for (int i = 0; i < 5; i++) {
            final int j = i;
            Call<UnivFoodList> info1 = foodInterface.getFoodList1(days[i]); // 학생회관
            info1.enqueue(new Callback<UnivFoodList>() {
                @Override
                public void onResponse(Call<UnivFoodList> call, Response<UnivFoodList> response) {
                    checkResponse1 = true;
                    UnivFoodList body = response.body();
                    if (body.getStore().getMenus().size() != 0) {
                        for (UnivFoodList.Store.Menu menu : body.getStore().getMenus()) {
                            int time = menu.getTime();
                            String menuObject = menu.getDescription();
                            DataInstance.getInstance().getCaf1()[j].add(new UnivCaffetteria(time, menuObject));
                        }
                    } else
                        DataInstance.getInstance().getCaf1()[j].add(new UnivCaffetteria(-1, null));
                }

                @Override
                public void onFailure(Call<UnivFoodList> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "학식 데이터를 불러오는 도중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
                }
            });

            Call<UnivFoodList> info2 = foodInterface.getFoodList2(days[i]); // 남자기숙사
            info2.enqueue(new Callback<UnivFoodList>() {
                @Override
                public void onResponse(Call<UnivFoodList> call, Response<UnivFoodList> response) {
                    checkResponse2 = true;
                    UnivFoodList body = response.body();
                    if (body.getStore().getMenus().size() != 0) {
                        for (UnivFoodList.Store.Menu menu : body.getStore().getMenus()) {
                            int time = menu.getTime();
                            String menuObject = menu.getDescription();
                            DataInstance.getInstance().getCaf2()[j].add(new UnivCaffetteria(time, menuObject));
                        }
                    } else
                        DataInstance.getInstance().getCaf2()[j].add(new UnivCaffetteria(-1, null));
                }

                @Override
                public void onFailure(Call<UnivFoodList> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "학식 데이터를 불러오는 도중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
                }
            });

            Call<UnivFoodList> info3 = foodInterface.getFoodList3(days[i]); // 여자기숙사
            info3.enqueue(new Callback<UnivFoodList>() {
                @Override
                public void onResponse(Call<UnivFoodList> call, Response<UnivFoodList> response) {
                    checkResponse3 = true;
                    UnivFoodList body = response.body();
                    if (body.getStore().getMenus().size() != 0) {
                        for (UnivFoodList.Store.Menu menu : body.getStore().getMenus()) {
                            int time = menu.getTime();
                            String menuObject = menu.getDescription();
                            DataInstance.getInstance().getCaf3()[j].add(new UnivCaffetteria(time, menuObject));
                        }
                    } else
                        DataInstance.getInstance().getCaf3()[j].add(new UnivCaffetteria(-1, null));
                }

                @Override
                public void onFailure(Call<UnivFoodList> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "학식 데이터를 불러오는 도중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
                }
            });

            Call<UnivFoodList> info4 = foodInterface.getFoodList4(days[i]); // 교수회관
            info4.enqueue(new Callback<UnivFoodList>() {
                @Override
                public void onResponse(Call<UnivFoodList> call, Response<UnivFoodList> response) {
                    checkResponse4 = true;
                    UnivFoodList body = response.body();
                    if (body.getStore().getMenus().size() != 0) {
                        for (UnivFoodList.Store.Menu menu : body.getStore().getMenus()) {
                            int time = menu.getTime();
                            String menuObject = menu.getDescription();
                            DataInstance.getInstance().getCaf4()[j].add(new UnivCaffetteria(time, menuObject));
                        }
                    } else
                        DataInstance.getInstance().getCaf4()[j].add(new UnivCaffetteria(-1, null));
                }

                @Override
                public void onFailure(Call<UnivFoodList> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "학식 데이터를 불러오는 도중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
