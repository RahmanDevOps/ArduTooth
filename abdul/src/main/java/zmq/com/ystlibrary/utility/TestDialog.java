package zmq.com.ystlibrary.utility;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import zmq.com.ystlibrary.BR;

public class TestDialog extends BaseObservable {
    @Bindable
    public boolean getDialogVisibility() {
        return dialogVisibility;
    }

    public void setDialogVisibility(boolean dialogVisibility) {
        this.dialogVisibility = dialogVisibility;
        notifyPropertyChanged(BR.dialogVisibility);
    }

    public boolean dialogVisibility;
}
