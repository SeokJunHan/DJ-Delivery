package dj.sjn.djbaedal;

import android.Manifest;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.viewpagerindicator.CirclePageIndicator;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import dj.sjn.djbaedal.Adapter.SlidingImage_Adapter;
import dj.sjn.djbaedal.DataClass.CheckNetwork;
import dj.sjn.djbaedal.DataClass.DataInstance;
import dj.sjn.djbaedal.DataClass.list_item;

public class Main3Activity extends AppCompatActivity {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    TextView telText, title;
    String img_reg, img_reg2, img_reg3, name, tel_no;
    ImageView bookMark;
    AlertDialog alertDialog;
    Toolbar toolbar;
    ViewPager viewPager;
    int currentPage = 0;
    int NUM_PAGES = 0;
    String[] urls;

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
                                String path = saveBitmap(resource, "Download", name + "_" + format.format(time));
                                if (path != null)
                                    Toast.makeText(getApplicationContext(), "이미지가 Download 폴더에 저장되었습니다!", Toast.LENGTH_LONG).show();
                                else {
                                    Toast.makeText(getApplicationContext(), "이미지를 저장할 수 없습니다.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        telText = findViewById(R.id.singleTel);
        toolbar = findViewById(R.id.toolbar3);
        title = findViewById(R.id.toolbar3_title);
        bookMark = findViewById(R.id.bookmark);
        pref = getSharedPreferences("bookmark", MODE_PRIVATE);
        editor = pref.edit();

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

            initViewpager();
            telText.setText("전화번호 : " + tel_no);

            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            title.setText(name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if(DataInstance.getInstance().getLinkedHashMap2().get(name) != null)
                bookMark.setImageResource(R.drawable.goldbook);
            bookMark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //북마크에 없음
                    if(DataInstance.getInstance().getLinkedHashMap2().get(name) == null) {
                        editor.putString(name, name + "~" + tel_no + "~" + img_reg + "~" + img_reg2 + "~" + img_reg3);
                        editor.commit();
                        Toast.makeText(getApplicationContext(), "북마크에 추가되었습니다!", Toast.LENGTH_SHORT).show();
                        DataInstance.getInstance().getLinkedHashMap2().put(name, new list_item(new String[]{img_reg, img_reg2, img_reg3}, name, tel_no));
                        bookMark.setImageResource(R.drawable.goldbook);
                    }
                    //북마크에 있음
                    else {
                        editor.remove(name);
                        editor.commit();
                        Toast.makeText(getApplicationContext(), "북마크에서 삭제되었습니다!", Toast.LENGTH_SHORT).show();
                        DataInstance.getInstance().getLinkedHashMap2().remove(name);
                        bookMark.setImageResource(R.drawable.whitebook);

                    }
                }
            });
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
        NUM_PAGES = urls[1] == null ? 1 : (urls[2] == null ? 2: 3);

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
}