package zmq.com.ystlibrary.utility;

import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import zmq.com.ystlibrary.R;
import zmq.com.ystlibrary.model.StoryListJson;

public class BindingUtil {

    @BindingAdapter("imageUrl")
    public static void loadImage(final ImageView view, final StoryListJson.Data data) {
        Glide.with(view.getContext())
                .load(data.icon_url)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }

    @BindingAdapter("android:visibility")
    public static void setVisibility(View view, boolean value) {
        view.setVisibility(value ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("android:rating")
    public static void setRating(RatingBar view, StoryListJson.Data data) {
        float a = Float.parseFloat(data.view);
        float b = Float.parseFloat(data.rating);
        float rating = (float) (b / a);
        if (view.getRating() != rating) {
            view.setRating(rating);
        }
    }

    @BindingAdapter("android:background")
    public static void setBackground(TextView textView, boolean value){
       textView.setBackgroundResource(!value?R.drawable.view_rounded_corners :R.drawable.view_rounded_fill_corners);
    }
}
