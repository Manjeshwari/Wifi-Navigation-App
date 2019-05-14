package app.manju.myapplication;

import android.app.Activity;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class WifiData extends Activity {


    Button WifiButton;
    TextView txtInfo;
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
                        "\n" + "MacAdress: " + macAddress+
                        "\n" + "BSSID:  " + bssid+
                        "\n" + "SSID :" + ssid +
                        "\n" + "RSSI :" + rssi +
                        "\n" + "LinkSpeed :" + linkspeed +
                        "\n" + "NetworkId: " + networkId;

                txtInfo.setText(info);


                //myarray<> Results = wifi.getScanResults();

                //ArrayAdapter myAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,Results);
               // mylistView.setAdapter(myAdapter);


                //Log.d("New Result");
               // WifiData myData = new WifiData();
                //myData.add(Results);

            }
        });

    }

}