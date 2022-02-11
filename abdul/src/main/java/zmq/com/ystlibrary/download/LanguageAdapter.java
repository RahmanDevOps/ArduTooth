package zmq.com.ystlibrary.download;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import zmq.com.ystlibrary.R;
import zmq.com.ystlibrary.databinding.LanguageBinding;
import zmq.com.ystlibrary.model.StoryListJson;
import zmq.com.ystlibrary.utility.TestDialog;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.MyViewHolder> {

    private List<StoryListJson.Language> languageList;
    private LayoutInflater layoutInflater;
    private PostsAdapterListener listener;
    private List<TestDialog> testDialogs;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final LanguageBinding binding;

        public MyViewHolder(final LanguageBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }


    public LanguageAdapter(List<StoryListJson.Language> languages, PostsAdapterListener listener) {
        this.languageList = languages;
        this.listener = listener;
        testDialogs = new ArrayList<>();
        for (int i = 0; i < languageList.size(); i++) {
            TestDialog testDialog=new TestDialog();
            if(i==0){
                testDialog.setDialogVisibility(true);

            }
            testDialogs.add(testDialog);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        LanguageBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.language_item, parent, false);
        return new MyViewHolder(binding);
    }

    boolean oneTym=true;
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.binding.setLanguage(languageList.get(position));
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    for (TestDialog testDialog : testDialogs) {
                        testDialog.setDialogVisibility(false);
                    }

                    TestDialog testDialog = testDialogs.get(position);
                    testDialog.setDialogVisibility(true);
                    holder.binding.setDialogVal(testDialog);
                    listener.onPostClicked(languageList.get(position),position);
                }
            }
        });
        if(oneTym){
            oneTym=false;
            TestDialog testDialog = testDialogs.get(position);
            holder.binding.setDialogVal(testDialog);
        }
    }

    @Override
    public int getItemCount() {
        return languageList.size();
    }

    public interface PostsAdapterListener {
        void onPostClicked(StoryListJson.Language language,int lng_code);
    }
}
