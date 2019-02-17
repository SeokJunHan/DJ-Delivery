package dj.sjn.djbaedal.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import dj.sjn.djbaedal.DataClass.list_item;
import dj.sjn.djbaedal.R;

public class ListAdapter extends BaseAdapter {

    Context context;
    ArrayList<list_item> list_itemArrayList;
    ViewHolder viewholder;

    class ViewHolder {
        ImageView imageView;
        TextView name;
        TextView tel_no;
    }

    public ListAdapter(Context context, ArrayList<list_item> list_itemARrayList) {
        this.context = context;
        this.list_itemArrayList = list_itemARrayList;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item, null);

            viewholder = new ViewHolder();
            viewholder.imageView = convertView.findViewById(R.id.image);
            viewholder.name = convertView.findViewById(R.id.name);
            viewholder.tel_no = convertView.findViewById(R.id.tel_no);

            //View에 object를 넣고 가져올 수 있게 해줌
            convertView.setTag(viewholder);
        }
        //캐시된 뷰가 있을 경우 저장된 뷰 홀더 사용
        else {
            viewholder = (ViewHolder) convertView.getTag();
        }

        Glide.with(context)
                .load(list_itemArrayList.get(position).getImage()[0])
                .thumbnail(Glide.with(context)
                        .load("https://t1.daumcdn.net/cfile/tistory/995168475C4DA4702A")
                        .apply(new RequestOptions().override(250, 250).circleCrop().diskCacheStrategy(DiskCacheStrategy.ALL).priority(Priority.HIGH)))
                .apply(new RequestOptions().override(250, 250).circleCrop().diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(viewholder.imageView);
        viewholder.name.setText(list_itemArrayList.get(position).getName());
        viewholder.tel_no.setText(list_itemArrayList.get(position).getTel_no());

        return convertView;
    }

    public void notifyChanged() {
        notifyDataSetChanged();
    }
}