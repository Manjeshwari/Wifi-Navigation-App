package app.manju.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.net.wifi.ScanResult;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class WifiScannerActivity extends AppCompatActivity {

    private WifiManager wifiManager;
    private ListView listView;
    private Button Print;
    TextView RoomNumber;
    String room;
    private int size = 0;
    private List<ScanResult> results;
    private static ArrayList<String> arrayList = new ArrayList<>();
    private ArrayList<String> routerinfo = new ArrayList<String>();
    private ArrayAdapter adapter;
    String result = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_scanner);
        Print= (Button) findViewById(R.id.ScanButton);
        RoomNumber = (TextView) findViewById(R.id.RoomeditText);
        room = getIntent().getExtras().getString("RoomNumber");
        RoomNumber.setText(room);


        Print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            System.out.println(routerinfo.size());

                for(String r: routerinfo){
                    Log.d("RouterID", "get" + result);
                    System.out.println(r);
                }

            }
        });

        listView= findViewById(R.id.wifilist);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(!wifiManager.isWifiEnabled()){
            Toast.makeText(this,"WiFi is disabled....Need to enable it", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(adapter);

            scanWifi();
    }

    private void scanWifi(){
        //arrayList.clear();
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
         wifiManager.startScan();
         Toast.makeText(this,"Scanning WiFi ....", Toast.LENGTH_SHORT).show();
    }




        BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                results = wifiManager.getScanResults();
                unregisterReceiver(this);


                    result = "";
                    //result+=arrayList.size()+":  ";
                    //result+=arrayList.size()+" ";
                    result += room + "\n";

                    for (ScanResult scanResult : results) {

                        //arrayList.add(scanResult.SSID);

                        result += scanResult.BSSID;
                        result += " ";
                        result += scanResult.level;
                        result += " ";
                        adapter.notifyDataSetChanged();
                    }
                    result += "\n";


                    routerinfo.add(result);

                    Log.d("RouterID", "get" + result);
                    arrayList.add(result);

                    //File text = result.getText().toString();
                    File wifiscans;
                    PrintWriter writer = null;

                    try {
                        wifiscans = new File(Environment.getExternalStorageDirectory(), "wifiscans.txt");
                        FileOutputStream os = new FileOutputStream(wifiscans, true);
                        //FileOutputStream fos = openFileOutput("wifiscans.txt", getApplicationContext().MODE_PRIVATE|getApplicationContext().MODE_APPEND);
                        os.write(result.getBytes(), 0, result.length());
                        os.close();
                        //writer=new PrintWriter(os);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "File not found", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        // log exception
                    }

                    //writer.print(result);
                    //writer.flush();
                    //writer.close();
                    //msg.setText("Data Saved Successfully");


                }

        };

}
