package zmq.com.ystlibrary.download;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import zmq.com.ystlibrary.R;
import zmq.com.ystlibrary.canvas.DownloadResourceActivity;
import zmq.com.ystlibrary.databinding.StoryBinding;
import zmq.com.ystlibrary.model.StoryListJson;
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    private List<StoryListJson.Data> postList;
    private LayoutInflater layoutInflater;
    private PostsAdapterListener listener;
    private DownloadResourceActivity.MyClickHandler myClickHandler;
    private Context context;
    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final StoryBinding binding;

        public MyViewHolder(final StoryBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
            this.binding.setHandlers(myClickHandler);
        }
    }


    public PostAdapter(Context context,List<StoryListJson.Data> postList, PostsAdapterListener listener) {
        this.context=context;
        this.postList = postList;
        this.listener = listener;
        myClickHandler=new DownloadResourceActivity.MyClickHandler(context);
    }

    public void setPostList(List<StoryListJson.Data> postList) {
        this.postList = postList;
    }

    public List<StoryListJson.Data> getPostList() {
        return postList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        StoryBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.story_item, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.binding.setData(postList.get(position));
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public interface PostsAdapterListener {
        void onPostClicked(StoryListJson.Data post);
    }
}
