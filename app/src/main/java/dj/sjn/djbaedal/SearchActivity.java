package dj.sjn.djbaedal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import dj.sjn.djbaedal.Adapter.SearchAdapter;
import dj.sjn.djbaedal.DataClass.CheckNetwork;
import dj.sjn.djbaedal.DataClass.DataInstance;
import dj.sjn.djbaedal.DataClass.MyListView;
import dj.sjn.djbaedal.DataClass.list_item;

public class SearchActivity extends AppCompatActivity {

    ArrayList<list_item> list;
    Toolbar toolbar;
    EditText editText;
    SearchAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar = findViewById(R.id.toolbarSearch);
        editText = findViewById(R.id.search_text);
        listView = findViewById(R.id.search_list);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list = new ArrayList<>();

        adapter = new SearchAdapter(list, this);
        listView.setAdapter(adapter);

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
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    search(editText.getText().toString());
                }
            });
        }
    }

    public void search(String text) {
        list.clear();

        boolean isOverlaped;
        if (!text.equals("")) {
            for (int i = 0; i < DataInstance.getInstance().getSearch_list().size(); i++) {
                isOverlaped = false;
                if (DataInstance.getInstance().getSearch_list().get(i).getName().toLowerCase().trim().contains(text.toLowerCase().trim())) {
                    for(int j=0; j<list.size(); j++) {
                        if(DataInstance.getInstance().getSearch_list().get(i).getName().trim().toLowerCase().equals(list.get(j).getName().trim().toLowerCase())) {
                            isOverlaped = true;
                            break;
                        }
                    }
                    if(!isOverlaped)
                    list.add(DataInstance.getInstance().getSearch_list().get(i));
                }
            }
        } else {
            list.clear();
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), Main3Activity.class);
                final String img_reg = list.get(position).getImage()[0];
                final String img_reg2 = list.get(position).getImage()[1];
                final String img_reg3 = list.get(position).getImage()[2];
                final String name = list.get(position).getName();
                final String tel_no = list.get(position).getTel_no();
                final String type = list.get(position).getType();
                final String extra_text = list.get(position).getExtra_text();
                final String thumbnail = list.get(position).getThumbnail();
                final String time = list.get(position).getTime();

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
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
            }
        }
        return true;
    }
}
