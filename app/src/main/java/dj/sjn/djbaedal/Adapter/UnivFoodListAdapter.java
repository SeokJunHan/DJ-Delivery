package dj.sjn.djbaedal.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import dj.sjn.djbaedal.DataClass.UnivCaffetteria;
import dj.sjn.djbaedal.DataClass.UnivFoodList;
import dj.sjn.djbaedal.DataClass.list_item;
import dj.sjn.djbaedal.R;

public class UnivFoodListAdapter extends BaseAdapter {

    Context context;
    ArrayList<UnivCaffetteria> univFoodArrayList;
    ViewHolder viewholder;

    class ViewHolder {
        LinearLayout food_exist;
        TextView time;
        TextView menu;
        TextView food_null;
    }

    public UnivFoodListAdapter(Context context, ArrayList<UnivCaffetteria> univFoodArrayList) {
        this.context = context;
        this.univFoodArrayList = univFoodArrayList;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public int getCount() {
        return univFoodArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return univFoodArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.univfood_item, null);

            viewholder = new ViewHolder();
            viewholder.time = convertView.findViewById(R.id.food_time);
            viewholder.menu = convertView.findViewById(R.id.food_menu);
            viewholder.food_exist = convertView.findViewById(R.id.foodExist_layout);
            viewholder.food_null = convertView.findViewById(R.id.food_null);

            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }

        String time_text = "";
        if(univFoodArrayList.get(0) != null) {
            switch (univFoodArrayList.get(position).getTime()) {
                case 0:
                    time_text = "아침";
                    viewholder.time.setBackgroundColor(Color.parseColor("#ffff00"));
                    viewholder.time.setTextColor(Color.parseColor("#757575"));
                    break;
                case 1:
                    time_text = "점심";
                    viewholder.time.setBackgroundColor(Color.parseColor("#ffa726"));
                    viewholder.time.setTextColor(Color.parseColor("#FFFFFF"));
                    break;
                case 2:
                    time_text = "저녁";
                    viewholder.time.setBackgroundColor(Color.parseColor("#ff5050"));
                    viewholder.time.setTextColor(Color.parseColor("#FFFFFF"));
                    break;
                case 3:
                    time_text = "종일";
                    viewholder.time.setBackgroundColor(Color.parseColor("#c6ff00"));
                    viewholder.time.setTextColor(Color.parseColor("#FFFFFF"));
                    break;
                case 4:
                    time_text = "기타";
                    viewholder.time.setBackgroundColor(Color.parseColor("#82b1ff"));
                    viewholder.time.setTextColor(Color.parseColor("#FFFFFF"));
                    break;
            }

            if (univFoodArrayList.get(position).getMenu() != null) {
                viewholder.menu.setText(univFoodArrayList.get(position).getMenu().replace(" ", " | "));
                viewholder.time.setText(time_text);
                viewholder.food_null.setVisibility(View.GONE);
            }
        }

        else if (univFoodArrayList.get(0) == null) {
            viewholder.food_exist.setVisibility(View.GONE);
        }

        return convertView;
    }
}