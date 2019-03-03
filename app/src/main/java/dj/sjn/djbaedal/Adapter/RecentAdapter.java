package dj.sjn.djbaedal.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import dj.sjn.djbaedal.DataClass.DataInstance;
import dj.sjn.djbaedal.DataClass.list_item;
import dj.sjn.djbaedal.MainActivity;
import dj.sjn.djbaedal.R;

public class RecentAdapter extends BaseAdapter {

    Context context;
    ArrayList<list_item> list_itemArrayList;
    ViewHolder viewholder;

    class ViewHolder {
        ImageView imageView, deleteButton;
        TextView name;
    }

    public RecentAdapter(Context context, ArrayList<list_item> list_itemArrayList) {
        this.context = context;
        this.list_itemArrayList = list_itemArrayList;
    }

    @Override
    public int getCount() {
        return list_itemArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return list_itemArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //캐시된 뷰가 없을경우 새로 생성 후 뷰홀더 생성
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.recentitem, null);

            viewholder = new ViewHolder();
            viewholder.imageView = convertView.findViewById(R.id.recent_image);
            viewholder.name = convertView.findViewById(R.id.recent_name);
            viewholder.deleteButton = convertView.findViewById(R.id.delete_button);

            convertView.setTag(viewholder);
        }
        else {
            viewholder = (ViewHolder) convertView.getTag();
        }

        Glide.with(context)
                .load(list_itemArrayList.get(position).getThumbnail())
                .thumbnail(Glide.with(context)
                        .load("https://t1.daumcdn.net/cfile/tistory/99B6454D5C79316A25")
                        .apply(new RequestOptions().override(100, 100).circleCrop().diskCacheStrategy(DiskCacheStrategy.ALL).priority(Priority.HIGH)))
                .apply(new RequestOptions().override(100, 100).circleCrop().diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(viewholder.imageView);
        viewholder.name.setText(list_itemArrayList.get(position).getName());
        final String itemName = list_itemArrayList.get(position).getName();
        viewholder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteButtonClicked(itemName);
            }
        });
        return convertView;
    }

    public void onDeleteButtonClicked(String name) {
        DataInstance.getInstance().getLinkedHashMap().remove(name);
        ((MainActivity)MainActivity.mContext).deleteRecentList();
    }

    public void notifyChanged() {
        notifyDataSetChanged();
    }
}