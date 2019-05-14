package app.manju.myapplication;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

public class wifiNetworks extends Activity {

    List<ScanResult> wifiNetworks;
    TextView tv;
    StringBuilder sb = new StringBuilder();
    Switch aSwitch;
    WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_networks);
        tv = (TextView) findViewById(R.id.tv);
        getwifiNetworkList();
    }

    private void getwifiNetworkList() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        aSwitch = (Switch) findViewById(R.id.switch1);
        //register the switch for even handling
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //to switch on WiFi
                if(isChecked && !wifiManager.isWifiEnabled()){
                    wifiManager.setWifiEnabled(true);
                }
                //
                if(isChecked && wifiManager.isWifiEnabled()){
                    wifiManager.setWifiEnabled(false);
                }

            }
        });
         wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                wifiNetworks = wifiManager.getScanResults();
                sb.append("\n Number of wifi connections:" + " " + wifiNetworks.size() + "\n\n");
                for (int i = 0; i < wifiNetworks.size(); i++) {
                    sb.append(new Integer(i + 1).toString() + ". ");
                    sb.append((wifiNetworks.get(i)).toString());
                    sb.append("\n\n");
                }

                tv.setText(sb);
            }

        }, filter);
        wifiManager.startScan();
    }


    /*public void Back(View view) {
Intent intent = new Intent(this, MainActivity.class);
startActivity(intent);
    }*/
}
