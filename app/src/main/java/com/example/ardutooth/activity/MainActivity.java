package com.example.ardutooth.activity;

import static android.content.ContentValues.TAG;

import static com.example.ardutooth.Utility.Utility.message_read;
import static com.example.ardutooth.Utility.Utility.message_state_changed;
import static com.example.ardutooth.Utility.Utility.message_write;
import static com.example.ardutooth.Utility.Utility.state_connected;
import static com.example.ardutooth.Utility.Utility.state_connecting;
import static com.example.ardutooth.Utility.Utility.state_connection_failed;
import static com.example.ardutooth.Utility.Utility.state_listening;
import static com.example.ardutooth.Utility.Utility.state_none;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSpecifier;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ardutooth.ModelClass.BluetoothModel;
import com.example.ardutooth.R;
import com.example.ardutooth.Utility.BluetoothItemListener;
import com.example.ardutooth.Utility.BluetoothUtils;
import com.example.ardutooth.adapters.CustomAdapter;
import com.example.ardutooth.adapters.PairedBluetoothAdapter;
import com.example.ardutooth.databinding.BluetoothBottomSheetLayoutBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;


import java.security.Permissions;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, BluetoothItemListener {
    private CustomAdapter listAdapter;
    private ListView drawerListView;
    private TextView header_name, header_email;
    private WifiManager mainWifiObj;
    private WifiScanReceiver wifiReciever;
    private ListView list;
    private String wifis[];
    private EditText pass;
    private MenuItem enableBluetooth, disableBluetooth;
    private BottomSheetDialog bluetoothBottomSheet;
    private List<BluetoothModel> pairedBluetoothList;
    private BluetoothBottomSheetLayoutBinding bottomSheetBinding;
    private PairedBluetoothAdapter pairedAdapter;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice[] pairedBTDevice;
    private BluetoothUtils bluetoothUtils = null;
    private boolean isEnabled = false;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        pairedBluetoothList = new ArrayList<>();
        bluetoothUtils = new BluetoothUtils(MainActivity.this,handler);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Main Menu");
        setSupportActionBar(toolbar);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        header_name = (TextView) findViewById(R.id.header_name);
        header_name.setText("WiFi Router Controller");
        header_email = (TextView) findViewById(R.id.header_email);
        header_email.setText("rahman" + "@gmail.com");
        drawerListView = (ListView) findViewById(R.id.lv_drawer);
        listAdapter = new CustomAdapter(this);
        listAdapter.setLanguageItems();
        drawerListView.setAdapter(listAdapter);
        drawerListView.setOnItemClickListener(this);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                System.out.println("onDrawerSlide");
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                System.out.println("onDrawerClosed");
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                System.out.println("onDrawerStateChanged");
                listAdapter.notifyDataSetChanged();
            }
        });

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setColorSchemeColors(R.color.darkfirozi);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new WifiScanReceiver();
                pullToRefresh.setRefreshing(false);
            }
        });

        String[] PERMS_INITIAL = {Manifest.permission.ACCESS_FINE_LOCATION};
        ActivityCompat.requestPermissions(this, PERMS_INITIAL, 127);
        list = findViewById(R.id.list);
        mainWifiObj = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        wifiReciever = new WifiScanReceiver();
        mainWifiObj.startScan();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ssid = ((TextView) view).getText().toString();
                connectToWifi(ssid);
                Toast.makeText(MainActivity.this, "Wifi SSID : " + ssid, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        enableBluetooth = menu.findItem(R.id.item_enable);
        disableBluetooth = menu.findItem(R.id.item_disable);
        return super.onPrepareOptionsMenu(menu);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(msg -> {
        switch (msg.what) {
            case message_state_changed:
                switch (msg.arg1) {
                    case state_none:
                        //showToast("not connected");
                        break;
                    case state_listening:

                        break;
                    case state_connecting:

                        break;
                    case state_connected:
                        //showToast("Connected");
                        Toast.makeText(MainActivity.this,"connected",Toast.LENGTH_SHORT).show();
                        bluetoothBottomSheet.dismiss();
                        break;
                    case state_connection_failed:

                        bluetoothBottomSheet.dismiss();
                        break;
                    default:
                        System.out.println("nothing found");
                }
                break;
            case message_read:
                byte[] buffer = (byte[]) msg.obj;
                String readMessage = new String(buffer, 0, msg.arg1);
                //binding.tvTokenId.setText(readTokenId);
                System.out.println("Token Id ==== " + readMessage);
                break;
            case message_write:
                byte[] buffer1 = (byte[]) msg.obj;
                String writeMessage = new String(buffer1);
                System.out.println("write message ====== " + writeMessage);
                break;
            default:
                System.out.println("nothing found");
        }
        return false;
    });


    @RequiresApi(api = Build.VERSION_CODES.S)
    @SuppressLint({"MissingPermission", "NonConstantResourceId"})
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_bluetooth:
                if (!bluetoothAdapter.isEnabled()) {
                    enableBluetooth.setVisible(true);
                    disableBluetooth.setVisible(false);
                } else {
                    enableBluetooth.setVisible(false);
                    disableBluetooth.setVisible(true);
                }
                break;
            case R.id.item_enable:
                if (!bluetoothAdapter.isEnabled()) {
                    setEnableBluetooth();
                }
                break;
            case R.id.item_disable:
                if (bluetoothAdapter.isEnabled()) {
                    bluetoothAdapter.disable();
                }
                break;
            case R.id.item_scan:
                showBluetoothBottomSheet();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private void showBluetoothBottomSheet() {
        bluetoothBottomSheet = new BottomSheetDialog(MainActivity.this);
        bottomSheetBinding = BluetoothBottomSheetLayoutBinding.inflate(
                LayoutInflater.from(MainActivity.this),
                null,
                false
        );

        bluetoothBottomSheet.setContentView(bottomSheetBinding.getRoot());
        bluetoothBottomSheet.setCancelable(false);
        bluetoothBottomSheet.setCanceledOnTouchOutside(true);

        bottomSheetBinding.backArrow.setOnClickListener(view -> bluetoothBottomSheet.dismiss());
        getPairedDevices();
        pairedAdapter = new PairedBluetoothAdapter(BluetoothModel.itemCallBack, MainActivity.this);
        bottomSheetBinding.rvPairedDevice.setAdapter(pairedAdapter);
        pairedAdapter.submitList(pairedBluetoothList);

        //scanDevices();

        setupFullHeight(bluetoothBottomSheet);

        bluetoothBottomSheet.show();
    }

    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        assert bottomSheet != null;
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) MainActivity.this).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.S)
    private void getPairedDevices() {
        pairedBluetoothList.clear();
        if (bluetoothAdapter.isEnabled()) {
            @SuppressLint("MissingPermission")
            Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
            pairedBTDevice = new BluetoothDevice[devices.size()];
            if (devices.size() > 0) {
                int image = R.drawable.cloud_upload_24;
                int index = 0;
                for (BluetoothDevice device : devices) {
                    pairedBTDevice[index] = device;
                    BluetoothClass bluetoothClass = device.getBluetoothClass();
                    if (bluetoothClass.getDeviceClass() == BluetoothClass.Device.PHONE_SMART) {

                    } else if (bluetoothClass.getDeviceClass() == BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET) {

                    } else if (bluetoothClass.getDeviceClass() == BluetoothClass.Device.AUDIO_VIDEO_HEADPHONES) {

                    } else if (bluetoothClass.getDeviceClass() == BluetoothClass.Device.AUDIO_VIDEO_LOUDSPEAKER) {

                    } else {

                    }
                    index++;
                    pairedBluetoothList.add(new BluetoothModel(image, device.getName(), device.getAddress()));
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void setEnableBluetooth() {
        if (bluetoothAdapter == null) {
            Toast.makeText(MainActivity.this,"Bluetooth is not supported in the device",Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            isEnabled = true;
            onEnableBluetoothLauncher.launch(intent);
        }

        if (bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(intent);
        }
    }

    ActivityResultLauncher<Intent> onEnableBluetoothLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (isEnabled) {
                        Toast.makeText(MainActivity.this,"Enabled",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this,"permission denied",Toast.LENGTH_SHORT).show();
                }

            });


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    protected void onPause() {
        unregisterReceiver(wifiReciever);
        super.onPause();
    }

    protected void onResume() {
        registerReceiver(wifiReciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }

    @Override
    public void onPairedItemClicked(int position) {
        bluetoothUtils.connect(pairedBTDevice[position]);
    }

    @Override
    public void onAvailableItemClicked(int position) {

    }

    public class WifiScanReceiver extends BroadcastReceiver {
        @SuppressLint("UseValueOf")
        public void onReceive(Context c, Intent intent) {
            List<ScanResult> wifiScanList = mainWifiObj.getScanResults();
            wifis = new String[wifiScanList.size()];

            for (int i = 0; i < wifiScanList.size(); i++) {
                wifis[i] = ((wifiScanList.get(i)).toString());
                System.out.println("==================Rahman wifis================" + wifis[i]);
            }
            String filtered[] = new String[wifiScanList.size()];
            int counter = 0;
            for (String eachWifi : wifis) {
                String[] temp = eachWifi.split(",");
                filtered[counter] = temp[0].substring(5).trim();
                counter++;
            }
            list.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, R.id.label, filtered));
        }
    }


    public boolean connectToWifi(String password, String ssid) {
        boolean connected = false;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            try {
                Log.e(TAG, "connection wifi pre Q");
                WifiConfiguration wifiConfig = new WifiConfiguration();
                wifiConfig.SSID = "\"" + ssid + "\"";
                wifiConfig.preSharedKey = "\"" + password + "\"";
                int netId = mainWifiObj.addNetwork(wifiConfig);
                mainWifiObj.disconnect();
                mainWifiObj.enableNetwork(netId, true);
                mainWifiObj.reconnect();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.e(TAG, "connection wifi  Q");

            WifiNetworkSpecifier wifiNetworkSpecifier = new WifiNetworkSpecifier.Builder().setSsid(ssid).setWpa2Passphrase(password).build();
            NetworkRequest networkRequest = new NetworkRequest.Builder().addTransportType(NetworkCapabilities.TRANSPORT_WIFI).setNetworkSpecifier(wifiNetworkSpecifier).build();
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();

            ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(Network network) {
                    super.onAvailable(network);
                    connectivityManager.bindProcessToNetwork(network);
                    Log.e(TAG, "onAvailable");
                }

                @Override
                public void onLosing(@NonNull Network network, int maxMsToLive) {
                    super.onLosing(network, maxMsToLive);
                    Log.e(TAG, "onLosing");
                }

                @Override
                public void onLost(Network network) {
                    super.onLost(network);
                    Log.e(TAG, "losing active connection");
                }

                @Override
                public void onUnavailable() {
                    super.onUnavailable();
                    Log.e(TAG, "onUnavailable");
                }
            };
            connectivityManager.requestNetwork(networkRequest, networkCallback);
        }
        return connected;
    }

    public boolean isNetworkAvailable(Context context) {
        boolean isOnline = false;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                NetworkCapabilities capabilities = manager.getNetworkCapabilities(manager.getActiveNetwork());
                isOnline = capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
            } else {
                NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
                isOnline = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isOnline;
    }

    private void connectToWifi(final String wifiSSID) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.connect);
        dialog.setTitle("Connect to Network");
        TextView textSSID = (TextView) dialog.findViewById(R.id.textSSID1);

        Button dialogButton = (Button) dialog.findViewById(R.id.okButton);
        pass = (EditText) dialog.findViewById(R.id.textPassword);
        textSSID.setText(wifiSSID);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String checkPassword = pass.getText().toString();
                connectToWifi(checkPassword, wifiSSID);
                System.out.println("===========checkPassword=============" + checkPassword);
                boolean isConnectionSuccessful = mainWifiObj.reconnect();
                if (isConnectionSuccessful) {
                    Toast.makeText(MainActivity.this, "Successfully Connected", Toast.LENGTH_SHORT).show();
                } else {
                    // Toast.makeText(MainActivity.this, "invalid credential", Toast.LENGTH_SHORT).show();

                }

                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
