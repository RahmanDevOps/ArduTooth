package com.example.ardutooth.adapters;

import static com.example.ardutooth.Utility.Utility.languageCode;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;

import com.example.ardutooth.R;
import com.example.ardutooth.Utility.Utility;
import com.example.ardutooth.databinding.ListviewItemDrawerBinding;

import java.util.ArrayList;
import java.util.List;


public class CustomAdapter extends BaseAdapter {
    private Context context;
    private int images[] = {1,2,3,4};
    private String listItemNames[];
    private List<ListViewItem> listViewItemList;

    public CustomAdapter(Context context){
        this.context = context;
        listViewItemList = new ArrayList<>();

    }

    public void setLanguageItems(){

        listItemNames = Utility.drawerItems[languageCode];
        listItemNames = Utility.drawerItems[languageCode];
        listViewItemList.clear();
        for(int i=0 ; i<images.length ;i++){
            ListViewItem listViewItem = new ListViewItem(listItemNames[i],images[i]);
            listViewItemList.add(listViewItem);
        }
    }

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PlanetViewHolder holder;

        if (convertView == null) {
            ListviewItemDrawerBinding itemBinding = DataBindingUtil.inflate( LayoutInflater.from(parent.getContext()), R.layout.listview_item_drawer, parent, false);

            holder = new PlanetViewHolder(itemBinding);
            holder.view = itemBinding.getRoot();
            holder.view.setTag(holder);
        }
        else {
            holder = (PlanetViewHolder) convertView.getTag();
        }
        holder.binding.setListItem(listViewItemList.get(position));
        return holder.view;
    }

    private static class PlanetViewHolder {
        private View view;
        private ListviewItemDrawerBinding binding;

        PlanetViewHolder(ListviewItemDrawerBinding binding) {
            this.view = binding.getRoot();
            this.binding = binding;
        }
    }

    @BindingAdapter("android:src")
    public static void setSrc(ImageView imageView , int id){
        switch (id){
            case 1:
                imageView.setImageResource(R.drawable.cloud_upload_24);
                break;
            case 2:
                imageView.setImageResource(R.drawable.cloud_upload_24);
                break;
            case 3:
                imageView.setImageResource(R.drawable.cloud_upload_24);
                break;
            case 4:
                imageView.setImageResource(R.drawable.cloud_upload_24);
                break;
        }
    }


    public void setImages(int images) {
        ListViewItem listViewItem = listViewItemList.get(3);
        listViewItem.setImage_id(images);
    }

    public void setListItemNames(String[] listItemNames) {
        for(int i=0 ; i<listViewItemList.size() ;i++){
            ListViewItem listViewItem = listViewItemList.get(i);
            listViewItem.setName(listItemNames[i]);
        }
    }
}
