package com.example.ardutooth.adapters;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ardutooth.ModelClass.BluetoothModel;
import com.example.ardutooth.Utility.BluetoothItemListener;
import com.example.ardutooth.databinding.BluetoothItemContainerBinding;


public class PairedBluetoothAdapter extends ListAdapter<BluetoothModel, PairedBluetoothAdapter.PairedBTViewHolder> {

    private final BluetoothItemListener bluetoothItemListener;

    public PairedBluetoothAdapter(@NonNull DiffUtil.ItemCallback<BluetoothModel> diffCallback, BluetoothItemListener bluetoothItemListener) {
        super(diffCallback);
        this.bluetoothItemListener = bluetoothItemListener;
    }

    @NonNull
    @Override
    public PairedBTViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PairedBTViewHolder(BluetoothItemContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull PairedBTViewHolder holder, int position) {
        BluetoothModel bluetooth = getItem(position);
        holder.setPairedBluetooth(bluetooth);
        holder.binding.bluetoothItemRoot.setOnClickListener(view -> bluetoothItemListener.onPairedItemClicked(position));
    }


    static class PairedBTViewHolder extends RecyclerView.ViewHolder {
        private final BluetoothItemContainerBinding binding;

        public PairedBTViewHolder(@NonNull BluetoothItemContainerBinding bluetoothItemContainerBinding) {
            super(bluetoothItemContainerBinding.getRoot());
            binding = bluetoothItemContainerBinding;
        }

        void setPairedBluetooth(BluetoothModel bluetoothModel) {
            binding.typeBtImage.setImageResource(bluetoothModel.getBluetoothImage());
            binding.tvBtDeviceName.setText(bluetoothModel.getBluetoothName());
            binding.tvIpAddress.setText(bluetoothModel.getIpAddress());
        }
    }
}
