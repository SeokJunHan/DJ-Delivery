package dj.sjn.djbaedal.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import dj.sjn.djbaedal.DataClass.list_item;
import dj.sjn.djbaedal.R;

public class SearchAdapter extends BaseAdapter {

    private Context context;
    private List<list_item> list;
    private LayoutInflater inflater;
    private ViewHolder viewHolder;

    public SearchAdapter(List<list_item> list, Context context) {
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.search_item, null);

            viewHolder = new ViewHolder();
            viewHolder.label = convertView.findViewById(R.id.label);
            viewHolder.label2 = convertView.findViewById(R.id.label2);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.label.setText(list.get(position).getName());
        viewHolder.label2.setText(list.get(position).getType());

        return convertView;
    }

    class ViewHolder {
        public TextView label, label2;
    }
}
