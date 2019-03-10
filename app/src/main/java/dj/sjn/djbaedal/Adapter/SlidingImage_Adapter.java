package dj.sjn.djbaedal.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import dj.sjn.djbaedal.DataClass.TouchImageView;
import dj.sjn.djbaedal.R;

public class SlidingImage_Adapter extends PagerAdapter {

    private String[] urls;
    private LayoutInflater inflater;
    private Context context;
    TouchImageView imageView;

    public SlidingImage_Adapter(Context context, String[] urls) {
        this.context = context;
        this.urls = urls;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(container);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public int getCount() {
        return urls[1] == null? 1 : (urls[2] == null? 2 : 3);
    }

    @NonNull
    @Override
    //create every slide of the view pager
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, container, false);

        assert imageLayout != null;
        imageView = imageLayout.findViewById(R.id.sliding_image);

        if(urls[position] != null) {

            Glide.with(context)
                    .load(urls[position])
                    .thumbnail(Glide.with(context)
                            .load("https://t1.daumcdn.net/cfile/tistory/99B6454D5C79316A25")
                            .apply(new RequestOptions().circleCrop().diskCacheStrategy(DiskCacheStrategy.ALL).priority(Priority.HIGH)))
                    .into(imageView);

            container.addView(imageLayout, 0);
        }

        return imageLayout;
    }




}
