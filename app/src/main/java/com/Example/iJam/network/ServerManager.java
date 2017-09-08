package com.Example.iJam.network;

/**
 * Created by Mostafa on 7/5/2015.
 */
public class ServerManager {
    private static String serverURL = null;
    private static String serverStatus = null;

    public static void setServerStatus(String status){
        serverStatus = status;
    }

    public static void setServerURL (String url){
        serverURL = "http://" + url + "/JamhubBackEnd";
    }

    public static String getServerURL() {
        //return "http://10.40.35.117/JamhubBackEnd";
//        return "http://192.168.1.7/JamhubBackEnd";
       //return "http://192.168.1.73/JamHubBackEnd";
       //return "http://10.40.34.9/JamHubBackEnd";
        return serverURL;
    }
}
