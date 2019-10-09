package app.manju.myapplication;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.manju.myapplication.R;


public class WifiData extends Activity {


    Button WifiButton;
    TextView txtInfo;
    Button Buildbutton;
    //ListView mylistView;
    //WifiManager wifi;
    //int size = 0;
    //List<ScanResult> results;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_data);

        WifiButton = (Button) findViewById(R.id.WifiButton);
        txtInfo = (TextView) findViewById(R.id.txtInfo);
        Buildbutton = (Button) findViewById(R.id.Buildbutton);
        //mylistView = (ListView) findViewById(R.id.mylistView);





        
        WifiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    int ip = wifiInfo.getIpAddress();
                    String macAddress = wifiInfo.getMacAddress();
                    String bssid = wifiInfo.getBSSID();
                    int rssi = wifiInfo.getRssi();
                    int linkspeed = wifiInfo.getLinkSpeed();
                    String ssid = wifiInfo.getSSID();
                    int networkId = wifiInfo.getNetworkId();
                    String ipAddress = Formatter.formatIpAddress(ip);
                    String info = "ipAddress: " + ipAddress +
                            "\n" + "MacAdress: " + macAddress +
                            "\n" + "BSSID:  " + bssid +
                            "\n" + "SSID :" + ssid +
                            "\n" + "RSSI :" + rssi +
                            "\n" + "LinkSpeed :" + linkspeed +
                            "\n" + "NetworkId: " + networkId;

                    txtInfo.setText(info);

                /*HashMap<String, Integer> map= new HashMap<>();
                map.put("RSSI 1",wifiInfo.getRssi());
                map.put("RSSI 2",wifiInfo.getRssi());
                map.put("RSSI 3",wifiInfo.getRssi());
                map.put("RSSI 4",wifiInfo.getRssi());
                    map.remove("RSSI 1");
                    map.remove("RSSI 2");
                    map.remove("RSSI 3");
                    map.remove("RSSI 4");*/


                    ArrayList<String>  BSSID = new ArrayList<>();

                    BSSID.add(wifiInfo.getBSSID());

                    ArrayList<Integer>  RSSID = new ArrayList<>();

                    RSSID.add(wifiInfo.getRssi());

                    Map<String, List> mapofrouters = new HashMap<String, List>();

                    mapofrouters.put("routers", BSSID);
                    mapofrouters.put("SignalStrength", RSSID);

                    Log.d("SignalStrength", "get" + mapofrouters.get(RSSID));
                    Log.d("RouterID", "get" + mapofrouters.get(BSSID));

            }
        });

        Buildbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename = "fileone.txt";
                FileOutputStream fos;
                try{
                    HashMap newmap = new HashMap();

                    // populate hash map
                    newmap.put(1, "First");
                    newmap.put(2, "Android ");
                    newmap.put(3, "App");

                    fos = openFileOutput(filename, Context.MODE_PRIVATE);
                    ObjectOutputStream out = new ObjectOutputStream(fos);
                    File fileone = new File("fileone.txt");
                    //FileOutputStream fos=new FileOutputStream(fileone);
                    //PrintWriter pw=new PrintWriter(fos);

                    /*for(Map.Entry<String,List> entry:newmap.entrySet()){
                        out.writeBytes(entrySet.getKey());
                        newmap = (HashMap) entrySet.getValue();
                        out.writeObject(newmap);
                        //pw.println(m.getKey()+"="+m.getValue());
                    }*/

                    out.flush();
                    out.close();
                    fos.close();
                }catch(Exception e){}

            }
        });
    }

}