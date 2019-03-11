package dj.sjn.djbaedal;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.viewpagerindicator.CirclePageIndicator;

import org.w3c.dom.Document;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import dj.sjn.djbaedal.Adapter.ReviewAdapter;
import dj.sjn.djbaedal.Adapter.SlidingImage_Adapter;
import dj.sjn.djbaedal.DataClass.CheckNetwork;
import dj.sjn.djbaedal.DataClass.DataInstance;
import dj.sjn.djbaedal.DataClass.MyListView;
import dj.sjn.djbaedal.DataClass.list_item;
import dj.sjn.djbaedal.DataClass.review_item;

public class Main3Activity extends AppCompatActivity {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    TextView telText, title, extra, timeText;
    LinearLayout time_layout, writeReview_layout;
    String img_reg, img_reg2, img_reg3, name, tel_no, type, extra_text, thumbnail, time;
    Snackbar snackbar;
    ArrayList<review_item> review_list;
    Map<String, Object> map2;
    ReviewAdapter reviewAdapter;
    ImageView bookMark;
    AlertDialog alertDialog;
    Toolbar toolbar;
    ViewPager viewPager;
    Boolean check = false;
    Boolean checkReviewed = false;
    int currentPage = 0;
    int NUM_PAGES = 0;
    int rate = 0;
    int reviewCount = 0;
    int count, max;
    String[] urls;

    FirebaseFirestore db;
    RatingBar writeReview_rates;
    EditText writeReview_name;
    EditText writeReview_content;
    Button writeReview_button;
    MyListView review_listView;
    RatingBar average_rates;
    TextView average_text;
    View main3_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        telText = findViewById(R.id.singleTel);
        toolbar = findViewById(R.id.toolbar3);
        title = findViewById(R.id.toolbar3_title);
        bookMark = findViewById(R.id.bookmark);
        extra = findViewById(R.id.extra_text);
        timeText = findViewById(R.id.time);
        time_layout = findViewById(R.id.time_layout);

        writeReview_layout = findViewById(R.id.writeReview_layout);
        writeReview_rates = findViewById(R.id.writeReview_rates);
        writeReview_name = findViewById(R.id.writeReview_name);
        writeReview_content = findViewById(R.id.writeReview_content);
        writeReview_button = findViewById(R.id.writeReview_button);
        review_listView = findViewById(R.id.review_listView);
        average_rates = findViewById(R.id.average_rates);
        average_text = findViewById(R.id.average_text);
        main3_layout = findViewById(R.id.main3_layout);

        pref = getSharedPreferences("bookmark", MODE_PRIVATE);
        editor = pref.edit();
        review_list = new ArrayList<>();

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
            img_reg = getIntent().getExtras().getString("img_reg");
            img_reg2 = getIntent().getExtras().getString("img_reg2");
            img_reg3 = getIntent().getExtras().getString("img_reg3");
            urls = new String[]{img_reg, img_reg2, img_reg3};
            name = getIntent.getExtras().getString("name");
            tel_no = getIntent.getExtras().getString("tel_no");
            type = getIntent.getExtras().getString("type");
            extra_text = getIntent.getExtras().getString("extra_text");
            thumbnail = getIntent.getExtras().getString("thumbnail");
            time = getIntent.getExtras().getString("time");

            initViewpager();

            telText.setText(tel_no);
            if (extra_text != null)
                extra.setText(extra_text.replace("\\n", System.getProperty("line.separator")));
            else
                extra.setVisibility(View.GONE);
            if (time != null)
                timeText.setText(time.replace("\\n", System.getProperty("line.separator")));
            else
                time_layout.setVisibility(View.GONE);

            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            title.setText(name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (DataInstance.getInstance().getLinkedHashMap2().get(name) != null)
                bookMark.setImageResource(R.drawable.goldbook);
            bookMark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //북마크에 추가
                    if (DataInstance.getInstance().getLinkedHashMap2().get(name) == null) {
                        editor.putString(name, name + "@" + tel_no + "@" + type + "@" + extra_text + "@" + thumbnail + "@" + time + "@" + img_reg + "@" + img_reg2 + "@" + img_reg3);
                        editor.commit();
                        Toast.makeText(getApplicationContext(), "북마크에 추가되었습니다!", Toast.LENGTH_SHORT).show();
                        DataInstance.getInstance().getLinkedHashMap2().put(name, new list_item(new String[]{img_reg, img_reg2, img_reg3}, name, tel_no, type, extra_text, thumbnail, time));
                        bookMark.setImageResource(R.drawable.goldbook);
                        if (((BookmarkActivity) BookmarkActivity.mContext) != null)
                            ((BookmarkActivity) BookmarkActivity.mContext).addList(new list_item(new String[]{img_reg, img_reg2, img_reg3}, name, tel_no, type, extra_text, thumbnail, time));
                    }
                    //북마크에서 삭제
                    else {
                        editor.remove(name);
                        editor.commit();
                        Toast.makeText(getApplicationContext(), "북마크에서 삭제되었습니다!", Toast.LENGTH_SHORT).show();
                        DataInstance.getInstance().getLinkedHashMap2().remove(name);
                        bookMark.setImageResource(R.drawable.whitebook);
                        if (((BookmarkActivity) BookmarkActivity.mContext) != null)
                            ((BookmarkActivity) BookmarkActivity.mContext).deleteList(name);
                    }
                }
            });

            setReview();
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm.isActive())
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    private long timestampToLong(review_item review) {
        return Long.parseLong(review.getTimeStamp().replace("-", "").replace(":", "").replace(" ", ""));
    }

    private ArrayList<review_item> bubbleSort(ArrayList<review_item> review_items) {

        review_item temp;

        for (int i = 0; i < review_items.size(); i++) {
            for (int j = 0; j < review_items.size() - i - 1; j++) {
                if (timestampToLong(review_items.get(j)) > timestampToLong(review_items.get(j + 1))) {
                    temp = review_items.get(j);
                    review_items.set(j, review_items.get(j + 1));
                    review_items.set(j + 1, temp);
                }
            }
        }

        return review_items;
    }

    private void calRate() {
        db.collection("review").document(name).collection("review").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                reviewCount = 0;
                rate = 0;
                for (DocumentSnapshot document : task.getResult()) {
                    int rates = Integer.parseInt(document.getData().get("rates").toString());
                    reviewCount++;
                    rate += rates;
                }

                if (reviewCount != 0) {
                    float averageRate;
                    averageRate = (float) (rate / (double) reviewCount);
                    average_rates.setRating(Float.parseFloat(String.format("%.2f", averageRate)));
                    average_text.setText(String.format("%.2f", averageRate) + " ( " + String.valueOf(reviewCount) + "개의 리뷰가 있습니다 )");
                    if (Main2Activity.mContext != null)
                        ((Main2Activity) Main2Activity.mContext).renewRate(name, "평점 : " + String.format("%.2f", (double) rate / reviewCount) + " ( " + reviewCount + "개의 리뷰가 있습니다 )");
                } else {
                    average_rates.setRating(0);
                    average_text.setText("-");
                    if (Main2Activity.mContext != null)
                        ((Main2Activity) Main2Activity.mContext).renewRate(name, "평점 : -");
                }

            }
        });
    }

    private void setReview() {
        writeReview_layout.setVisibility(View.GONE);
        db = FirebaseFirestore.getInstance();

        snackbar = Snackbar.make(main3_layout, "리뷰 정보를 받아오고있습니다...", Snackbar.LENGTH_INDEFINITE);
        snackbar.show();

        Map<String, Object> map = new HashMap<>();
        db.collection("review").document(name).set(map, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                map2 = new HashMap<>();
                db.collection("review").document(name).collection("review").document("creating").set(map2, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        db.collection("review").document(name).collection("review").document("creating").delete();
                        LoadReviews();
                    }
                });
            }
        });
    }

    private void LoadReviews() {
        db.collection("review").document(name).collection("review").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot document : task.getResult()) {

                    String name = document.getData().get("name").toString();
                    String id = document.getData().get("id").toString();
                    int rates = Integer.parseInt(document.getData().get("rates").toString());
                    String timestamp = document.getData().get("timestamp").toString();
                    String content = document.getData().get("content").toString();
                    review_list.add(new review_item(id, name, rates, content, timestamp));
                    if (id.equals(DataInstance.getInstance().getSerial())) {
                        checkReviewed = true;
                    }
                    reviewCount++;
                    rate += rates;
                }

                review_list = bubbleSort(review_list);
                snackbar.dismiss();

                if (reviewCount != 0) {
                    float averageRate;
                    averageRate = (float) (rate / (double) reviewCount);
                    average_rates.setRating(Float.parseFloat(String.format("%.2f", averageRate)));
                    average_text.setText(String.format("%.2f", averageRate) + " ( " + String.valueOf(reviewCount) + "개의 리뷰가 있습니다 )");
                }

                if (!checkReviewed)
                    writeReview_layout.setVisibility(View.VISIBLE);

                reviewAdapter = new ReviewAdapter(Main3Activity.this, review_list, name);
                review_listView.setAdapter(reviewAdapter);
                review_listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        if (review_list.get(position).getUser_id().equals(DataInstance.getInstance().getSerial())) {
                            final int temp_position = position;
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Main3Activity.this);
                            alertDialogBuilder.setTitle("알림")
                                    .setMessage("작성한 리뷰를 삭제할까요?")
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                                            try {
                                                db.collection("review").document(name).collection("review").document(DataInstance.getInstance().getSerial()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(getApplicationContext(), "리뷰를 삭제했습니다.", Toast.LENGTH_LONG).show();
                                                            review_list.remove(temp_position);
                                                            reviewAdapter.notifyDataSetChanged();
                                                            writeReview_content.setText("");
                                                            writeReview_rates.setRating(0);
                                                            writeReview_name.setText("");
                                                            writeReview_layout.setVisibility(View.VISIBLE);
                                                            calRate();
                                                        }
                                                    }
                                                });
                                            } catch (Exception e) {
                                                Toast.makeText(getApplicationContext(), "처리 도중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    })
                                    .setNegativeButton("취소", null);
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }
                        return true;
                    }
                });
                reviewAdapter.notifyDataSetChanged();
            }
        });

        writeReview_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DataInstance.getInstance().isBanned()) {
                    Toast.makeText(getApplicationContext(), "리뷰 작성이 제한된 사용자입니다.", Toast.LENGTH_LONG).show();
                    return;
                }
                final int rates = (int) writeReview_rates.getRating();
                final String name2 = writeReview_name.getText().toString();
                final String content = writeReview_content.getText().toString();
                if (rates == 0) {
                    Toast.makeText(getApplicationContext(), "별점을 매겨주세요!", Toast.LENGTH_LONG).show();
                    return;
                } else if (name2.trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "이름을 입력해주세요!", Toast.LENGTH_LONG).show();
                    return;
                } else if (content.trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "내용을 입력해주세요!", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    hideKeyboard();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
                    final String timestamp = format.format(cal.getTime());
                    Map<String, Object> map = new HashMap<>();
                    map.put("rates", rates);
                    map.put("name", name2);
                    map.put("content", content);
                    map.put("timestamp", timestamp);
                    map.put("id", DataInstance.getInstance().getSerial());
                    db.collection("review").document(name).collection("review").document(DataInstance.getInstance().getSerial()).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            writeReview_layout.setVisibility(View.GONE);
                            review_list.add(new review_item(DataInstance.getInstance().getSerial(), name2, rates, content, timestamp));
                            checkReviewed = false;
                            reviewAdapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(), "리뷰가 작성되었습니다.", Toast.LENGTH_LONG).show();
                            calRate();
                            Log.e("글 작성 완료", "완료");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "작성중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
                            Log.e("글 작성 실패", "실패");
                        }
                    });
                }
            }
        });
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
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }

    public static String saveBitmap(Bitmap bitmap, String folder, String name) {
        String exStorage = Environment.getExternalStorageDirectory().getAbsolutePath();
        String folderName = "/" + folder + "/";
        String fileName = name + ".jpg";
        String path = exStorage + folderName;

        File filePath;
        try {
            filePath = new File(path);
            if (!filePath.isDirectory()) {
                filePath.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(path + fileName);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();

            return path + fileName;

        } catch (Exception e) {
            return null;
        }
    }

    private void initViewpager() {
        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(new SlidingImage_Adapter(this, urls));

        CirclePageIndicator indicator = findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
        final float density = getResources().getDisplayMetrics().density;

        //Set circle indicator radius
        indicator.setRadius(5 * density);
        NUM_PAGES = urls[1] == null ? 1 : (urls[2] == null ? 2 : 3);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        });

        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_call: {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Main3Activity.this);
                alertDialogBuilder.setTitle(name)
                        .setMessage("전화를 거시겠습니까?\n전화번호 : " + tel_no)
                        .setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setNegativeButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent("android.intent.action.DIAL", Uri.parse("tel:" + tel_no.replace("-", ""))));
                            }
                        });
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;
            }
            case R.id.action_download: {
                count = 0;
                max = 0;
                max++;
                Glide.with(getApplicationContext()).asBitmap().load(img_reg)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(Main3Activity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                                }
                                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
                                Date time = new Date();
                                String path = saveBitmap(resource, "Download", name + "1_" + format.format(time));
                                check = true;
                                if (path != null)
                                    count++;
                            }
                        });
                if (img_reg2 != null) {
                    max++;
                    Glide.with(getApplicationContext()).asBitmap().load(img_reg2)
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
                                    Date time = new Date();
                                    String path = saveBitmap(resource, "Download", name + "2_" + format.format(time));
                                    if (path != null)
                                        count++;
                                }
                            });
                }
                if (img_reg3 != null) {
                    max++;
                    Glide.with(getApplicationContext()).asBitmap().load(img_reg3)
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
                                    Date time = new Date();
                                    String path = saveBitmap(resource, "Download", name + "3_" + format.format(time));
                                    if (path != null)
                                        count++;
                                }
                            });
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (count == max)
                            Toast.makeText(getApplicationContext(), count + "개 이미지가 Download 폴더에 저장되었습니다!", Toast.LENGTH_LONG).show();
                        else if (count == 0)
                            Toast.makeText(getApplicationContext(), "이미지를 저장할 수 없습니다.", Toast.LENGTH_LONG).show();
                        else if (count < max)
                            Toast.makeText(getApplicationContext(), max - count + "개 이미지 저장에 실패했습니다.", Toast.LENGTH_LONG).show();
                    }
                }, 500);
                return true;
            }
            case android.R.id.home: {
                finish();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu3, menu);
        return true;
    }
}