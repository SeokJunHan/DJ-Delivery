package dj.sjn.djbaedal.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import dj.sjn.djbaedal.DataClass.DataInstance;
import dj.sjn.djbaedal.DataClass.review_item;
import dj.sjn.djbaedal.R;

public class ReviewAdapter extends BaseAdapter {

    Context context;
    ArrayList<review_item> review_items;
    ViewHolder viewHolder;
    String name;

    class ViewHolder {
        TextView review_id;
        TextView review_timeStamp;
        TextView review_content;
        RatingBar review_rates;
    }

    public ReviewAdapter(Context context, ArrayList<review_item> review_items, String name) {
        this.context = context;
        this.review_items = review_items;
        this.name = name;
    }

    @Override
    public int getCount() {
        return review_items.size();
    }

    @Override
    public Object getItem(int position) {
        return review_items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.review_item, null);

            viewHolder = new ViewHolder();
            viewHolder.review_id = convertView.findViewById(R.id.review_id);
            viewHolder.review_timeStamp = convertView.findViewById(R.id.review_timestamp);
            viewHolder.review_content = convertView.findViewById(R.id.review_content);
            viewHolder.review_rates = convertView.findViewById(R.id.review_rates);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.review_id.setText(review_items.get(position).getUser_nickname());
        viewHolder.review_timeStamp.setText(review_items.get(position).getTimeStamp());
        viewHolder.review_content.setText(review_items.get(position).getContents());
        viewHolder.review_rates.setRating(review_items.get(position).getRate());

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!= null && mAuth.getCurrentUser().getUid().equals(review_items.get(position).getUser_id())) {
            viewHolder.review_id.setTextColor(Color.BLUE);
        }
        else {
            viewHolder.review_id.setTextColor(Color.BLACK);
        }

        return convertView;
    }
}
