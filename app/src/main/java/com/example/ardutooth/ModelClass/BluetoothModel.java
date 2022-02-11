package com.example.ardutooth.ModelClass;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Objects;

public class BluetoothModel {
    int bluetoothImage;
    String bluetoothName;
    String ipAddress;

    public BluetoothModel() {

    }

    public BluetoothModel(int bluetoothImage, String bluetoothName, String ipAddress) {
        this.bluetoothImage = bluetoothImage;
        this.bluetoothName = bluetoothName;
        this.ipAddress = ipAddress;
    }

    public int getBluetoothImage() {
        return bluetoothImage;
    }

    public String getBluetoothName() {
        return bluetoothName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setBluetoothImage(int bluetoothImage) {
        this.bluetoothImage = bluetoothImage;
    }

    public void setBluetoothName(String bluetoothName) {
        this.bluetoothName = bluetoothName;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BluetoothModel that = (BluetoothModel) o;
        return bluetoothImage == that.bluetoothImage && bluetoothName.equals(that.bluetoothName) && ipAddress.equals(that.ipAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bluetoothImage, bluetoothName, ipAddress);
    }

    public static DiffUtil.ItemCallback<BluetoothModel> itemCallBack = new DiffUtil.ItemCallback<BluetoothModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull BluetoothModel oldItem, @NonNull BluetoothModel newItem) {
            return oldItem.getIpAddress().equals(newItem.getIpAddress());
        }

        @Override
        public boolean areContentsTheSame(@NonNull BluetoothModel oldItem, @NonNull BluetoothModel newItem) {
            return oldItem.equals(newItem);
        }
    };
}