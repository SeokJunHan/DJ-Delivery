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
import java.util.Random;
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

public class PreActivity extends AppCompatActivity {

    int num, k;
    FirebaseFirestore db;
    String[] category, days;
    String[] proverbs = {
            "[루드비히 포이에르바하]\n먹는 음식이 곧 자신이다.",
            "[션 스튜어트]\n잘못된 음식이란 것은 없다.",
            "[조프리 네이어]\n좋은 음식은 좋은 대화로 끝이 난다.",
            "[캘빈 트릴린]\n건강식품이 나를 아프게 한다.",
            "[쉴라 그레이엄]\n음식은 가장 원시적인 형태의 위안거리다.",
            "[제임스 비어드]\n음식은 우리의 공통점이요, 보편적 경험이다.",
            "[루크레티우스]\n누군가에게는 음식인 것이 다른 이에게는 쓴 독이다.",
            "[조지 버나드 쇼]\n음식에 대한 사랑보다 더 진실된 사랑은 없다.",
            "[미스 피기]\n들어 옮길 수 있는 양보다 많이 먹지 말라.",
            "[마더 테레사]\n백 사람을 먹일 수 없다면 한 사람이라도 먹여라.",
            "[마크 트웨인]\n인생에서 성공하는 비결 중 하나는 좋아하는\n음식을 먹고 힘내 싸우는 것이다.",
            "[M.F.K. 피셔]\n다른 인간과 음식을 나눠 먹는 것은 가볍게\n빠져서는 안되는 친밀한 행위이다.",
            "[줄리아 차일드]\n화려하고 복잡한 걸작을 요리할 필요는 없다.\n다만 신선한 재료로 좋은 음식을 요리하라.",
            "[소크라테스]\n악인은 먹고 마시기 위해서 살고,\n선인은 살기 위해 먹고 마신다.",
            "[에픽테토스]\n다른 이에게 무엇을 먹어야한다고 설교하지 말라.\n네게 맞게 먹고 침묵하라.",
            "[미셸 드 몽테뉴]\n잘 먹는 기술은 결코 하찮은 기술이 아니며,\n그로 인한 기쁨은 작은 기쁨이 아니다.",
            "[쥘 르나르]\n진정으로 자유로운 사람은 변명하지 않고\n저녁식사 초대를 거절할 수 있는 사람이다.",
            "[마하트마 간디]\n세상에는 배가 너무 고파 신이 빵의 모습으로만\n나타날 수 있는 사람들이 있다.",
            "[오슨 웰즈]\n당신이 국가를 위해서 무엇을 할 수 있는지 묻지 말라.\n점심이 무엇인지 물어라."
    };
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
        Random rand = new Random();
        preText.setText(proverbs[rand.nextInt(proverbs.length)]);

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
                                                .load("https://t1.daumcdn.net/cfile/tistory/99B6454D5C79316A25")
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
                            String name;
                            try {
                                name = document.getData().get("name").toString();
                            } catch (NullPointerException e_name) {
                                name = "";
                            }
                            String tel_no;
                            try {
                                tel_no = document.getData().get("tel_no").toString();
                            } catch (NullPointerException e_tel_no) {
                                tel_no = "";
                            }
                            String type;
                            try {
                                type = document.getData().get("type").toString();
                            } catch (NullPointerException e_type) {
                                type = "";
                            }
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
            } catch (Exception e) {
            }
        }
    }

    private void LoadUnivFoodData() {
        final SharedPreferences pref = getSharedPreferences("food", MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://bablabs.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UnivFoodInterface foodInterface = retrofit.create(UnivFoodInterface.class);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        days = new String[7];
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        days[0] = simpleDateFormat.format(cal.getTime());
        for (int i = 1; i < 7; i++) {
            cal.add(Calendar.DATE, 1);
            days[i] = simpleDateFormat.format(cal.getTime());
        }
        DataInstance.getInstance().setDays(days);

        for (int i = 0; i < 7; i++) {
            DataInstance.getInstance().getCaf1()[i].clear();
            DataInstance.getInstance().getCaf2()[i].clear();
            DataInstance.getInstance().getCaf3()[i].clear();
            DataInstance.getInstance().getCaf4()[i].clear();
        }

        if (pref.getString("key", "").equals(days[0])) {
            for (int i = 0; i < 7; i++) {
                for (int j = 0; ; j++) {
                    String string = pref.getString("Caf1" + String.valueOf(i) + String.valueOf(j), null);
                    if ((string != null)) {
                        int time = Integer.parseInt(string.split("@")[0]);
                        String menu = "";
                        try {
                            menu = string.split("@")[1];
                        } catch (ArrayIndexOutOfBoundsException array_e) {}
                        DataInstance.getInstance().getCaf1()[i].add(new UnivCaffetteria(time, menu));
                    } else break;
                }
                for (int j = 0; ; j++) {
                    String string = pref.getString("Caf2" + String.valueOf(i) + String.valueOf(j), null);
                    if ((string != null)) {
                        int time = Integer.parseInt(string.split("@")[0]);
                        String menu = "";
                        try {
                            menu = string.split("@")[1];
                        } catch (ArrayIndexOutOfBoundsException array_e) {}
                        DataInstance.getInstance().getCaf2()[i].add(new UnivCaffetteria(time, menu));
                    } else break;
                }
                for (int j = 0; ; j++) {
                    String string = pref.getString("Caf3" + String.valueOf(i) + String.valueOf(j), null);
                    if ((string != null)) {
                        int time = Integer.parseInt(string.split("@")[0]);
                        String menu = "";
                        try {
                            menu = string.split("@")[1];
                        } catch (ArrayIndexOutOfBoundsException array_e) {}
                        DataInstance.getInstance().getCaf3()[i].add(new UnivCaffetteria(time, menu));
                    } else break;
                }
                for (int j = 0; ; j++) {
                    String string = pref.getString("Caf4" + String.valueOf(i) + String.valueOf(j), null);
                    if ((string != null)) {
                        int time = Integer.parseInt(string.split("@")[0]);
                        String menu = "";
                        try {
                            menu = string.split("@")[1];
                        } catch (ArrayIndexOutOfBoundsException array_e) {}
                        DataInstance.getInstance().getCaf4()[i].add(new UnivCaffetteria(time, menu));
                    } else break;
                }
            }
            checkResponse1 = true;
            checkResponse2 = true;
            checkResponse3 = true;
            checkResponse4 = true;
            return;
        }

        Log.e("데이터가 존재하지 않음", "API를 호출하여 메뉴를 추가합니다.");
        for (int i = 0; i < 7; i++) {
            final int j = i;
            Call<UnivFoodList> info1 = foodInterface.getFoodList1(days[i]); // 학생회관
            info1.enqueue(new Callback<UnivFoodList>() {
                @Override
                public void onResponse(Call<UnivFoodList> call, Response<UnivFoodList> response) {
                    checkResponse1 = true;
                    k = 0;
                    UnivFoodList body = response.body();
                    if (body.getStore().getMenus().size() != 0) {
                        for (UnivFoodList.Store.Menu menu : body.getStore().getMenus()) {
                            int time = menu.getTime();
                            String menuObject = menu.getDescription();
                            DataInstance.getInstance().getCaf1()[j].add(new UnivCaffetteria(time, menuObject));
                            editor.putString("Caf1" + j + "" + (k++), time + "@" + menuObject);
                        }
                        editor.commit();
                    } else
                        DataInstance.getInstance().getCaf1()[j].add(new UnivCaffetteria(-1, null));
                }

                @Override
                public void onFailure(Call<UnivFoodList> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "학식 리스트를 불러오는 도중 오류가 발생했습니다.\n오류가 계속해서 발생한다면 앱 데이터 삭제후 다시 실행해주세요.", Toast.LENGTH_LONG).show();
                }
            });

            Call<UnivFoodList> info2 = foodInterface.getFoodList2(days[i]); // 남자기숙사
            info2.enqueue(new Callback<UnivFoodList>() {
                @Override
                public void onResponse(Call<UnivFoodList> call, Response<UnivFoodList> response) {
                    checkResponse2 = true;
                    k = 0;
                    UnivFoodList body = response.body();
                    if (body.getStore().getMenus().size() != 0) {
                        for (UnivFoodList.Store.Menu menu : body.getStore().getMenus()) {
                            int time = menu.getTime();
                            String menuObject = menu.getDescription();
                            DataInstance.getInstance().getCaf2()[j].add(new UnivCaffetteria(time, menuObject));
                            editor.putString("Caf2" + j + "" + (k++), time + "@" + menuObject);
                        }
                        editor.commit();
                    } else
                        DataInstance.getInstance().getCaf2()[j].add(new UnivCaffetteria(-1, null));
                }

                @Override
                public void onFailure(Call<UnivFoodList> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "학식 리스트를 불러오는 도중 오류가 발생했습니다.\n오류가 계속해서 발생한다면 앱 데이터 삭제후 다시 실행해주세요.", Toast.LENGTH_LONG).show();
                }
            });

            Call<UnivFoodList> info3 = foodInterface.getFoodList3(days[i]); // 여자기숙사
            info3.enqueue(new Callback<UnivFoodList>() {
                @Override
                public void onResponse(Call<UnivFoodList> call, Response<UnivFoodList> response) {
                    checkResponse3 = true;
                    k = 0;
                    UnivFoodList body = response.body();
                    if (body.getStore().getMenus().size() != 0) {
                        for (UnivFoodList.Store.Menu menu : body.getStore().getMenus()) {
                            int time = menu.getTime();
                            String menuObject = menu.getDescription();
                            DataInstance.getInstance().getCaf3()[j].add(new UnivCaffetteria(time, menuObject));
                            editor.putString("Caf3" + j + "" + (k++), time + "@" + menuObject);
                        }
                        editor.commit();
                    } else
                        DataInstance.getInstance().getCaf3()[j].add(new UnivCaffetteria(-1, null));
                }

                @Override
                public void onFailure(Call<UnivFoodList> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "학식 리스트를 불러오는 도중 오류가 발생했습니다.\n오류가 계속해서 발생한다면 앱 데이터 삭제후 다시 실행해주세요.", Toast.LENGTH_LONG).show();
                }
            });

            Call<UnivFoodList> info4 = foodInterface.getFoodList4(days[i]); // 교수회관
            info4.enqueue(new Callback<UnivFoodList>() {
                @Override
                public void onResponse(Call<UnivFoodList> call, Response<UnivFoodList> response) {
                    checkResponse4 = true;
                    k = 0;
                    UnivFoodList body = response.body();
                    if (body.getStore().getMenus().size() != 0) {
                        for (UnivFoodList.Store.Menu menu : body.getStore().getMenus()) {
                            int time = menu.getTime();
                            String menuObject = menu.getDescription();
                            DataInstance.getInstance().getCaf4()[j].add(new UnivCaffetteria(time, menuObject));
                            editor.putString("Caf4" + j + "" + (k++), time + "@" + menuObject);
                        }
                        editor.commit();
                    } else
                        DataInstance.getInstance().getCaf4()[j].add(new UnivCaffetteria(-1, null));
                }

                @Override
                public void onFailure(Call<UnivFoodList> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "학식 리스트를 불러오는 도중 오류가 발생했습니다.\n오류가 계속해서 발생한다면 앱 데이터 삭제후 다시 실행해주세요.", Toast.LENGTH_LONG).show();
                }
            });
            editor.putString("key", days[0]);
            editor.commit();
        }
    }
}