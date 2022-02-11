package com.example.ardutooth.Utility;

import java.util.UUID;

public class Utility
{
    public static int languageCode = 1;
    public static final String[][] drawerItems = {{"Download Resource file", "Language", "Upload Data", "Logout"},{"Wifi List", "Ping", "Settings", "Logout"}};
    public static final int request_check_code = 108;


    public static final int state_listening = 201;
    public static final int state_connecting = 202;
    public static final int state_connected = 203;
    public static final int state_connection_failed = 204;
    public static final int state_none = 205;

    public static final int message_state_changed = 101;
    public static final int message_read = 102;
    public static final int message_write = 103;
    // UUID service
    public static final UUID btModuleUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

}
