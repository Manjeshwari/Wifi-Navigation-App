package app.manju.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import app.manju.myapplication.DatabaseActivity;

public class GuessActivity<pubilc> extends AppCompatActivity {
    private WifiManager GuesswifiManager;
    String room;
    private int size = 0;
    private List<ScanResult> results;
    private static ArrayList<String> arrayList = new ArrayList<>();
    private ArrayList<String> routerinfo = new ArrayList<String>();
    private ArrayAdapter adapter;
    String result = "";

    HashMap<String, String> sampleHashMap = new HashMap<String, String>();

    List<String> sampleList = new ArrayList<String>();
    List<String> linesplit = new ArrayList<>();
   // List<Double> roomdistance = new ArrayList<>();
    //HashMap<String, Double> roomdistanceHashMap = new HashMap<>();
    //List<Double> roomdistancevalues = new ArrayList<>(roomdistanceHashMap.values());
    HashMap<String,String> samplerouterlistdict=new HashMap<>(DatabaseActivity.routerlistdict);
    //List<HashMap<String, ArrayList<String>>> DBroomdict = new ArrayList(DatabaseActivity.roomdatabasedict.values());
    int routerNotinSample = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess);
     TextView Guess=(TextView) findViewById(R.id.GuesstextView);
        GuesswifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(!GuesswifiManager.isWifiEnabled()) {
            Toast.makeText(this, "WiFi is disabled....Need to enable it", Toast.LENGTH_LONG).show();
            GuesswifiManager.setWifiEnabled(true);
        }

        scanWifi();

    }

    private void scanWifi(){
        //arrayList.clear();
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        GuesswifiManager.startScan();
        Toast.makeText(this,"Scanning WiFi ....", Toast.LENGTH_SHORT).show();

    }

    BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            results= GuesswifiManager.getScanResults();
            System.out.println(results);
            unregisterReceiver(this);

            result="";
            //result+=arrayList.size()+":  ";
            //result+=arrayList.size()+" ";
            //result+=room+"\n";

            for (ScanResult scanResult : results){

                //arrayList.add(scanResult.SSID);

                result += scanResult.BSSID;
                result  += " ";
                result += scanResult.level;
                result  += " ";
                //adapter.notifyDataSetChanged();
            }
            result+="\n";

            routerinfo.add(result);

            Log.d("RouterID", "get" + result);
            arrayList.add(result);
            //File text = result.getText().toString();
            File Guess;
            PrintWriter writer=null;

            try {
                Guess = new File(Environment.getExternalStorageDirectory(),"guessScan.txt" );
                FileOutputStream os = new FileOutputStream(Guess,true);
                //FileOutputStream fos = openFileOutput("wifiscans.txt", getApplicationContext().MODE_PRIVATE|getApplicationContext().MODE_APPEND);
                os.write(result.getBytes(),0,result.length());
                os.close();
                Toast.makeText(getApplicationContext(), "Guess scanned!!",Toast.LENGTH_SHORT).show();
                //writer=new PrintWriter(os);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "File not found",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                // log exception
            }

            readGuessFile();
            samplerouterlist();
            roomdistance();
            clearguessfile();
            //findroom();
        }
    };
public void readGuessFile(){
    try {
        File samplefilename = new File(Environment.getExternalStorageDirectory(), "guessScan.txt");
        Scanner samplescanner = new Scanner(samplefilename);
        int count2 = 1;
        for(int i = 0;i<count2;i++)
        {
            while (samplescanner.hasNextLine()) {
                sampleList.add(samplescanner.nextLine());
                count2++;
            }

        }

        Toast.makeText(getApplicationContext(), "File read Successfully", Toast.LENGTH_SHORT).show();
         } catch (Exception e) {
        Toast.makeText(getApplicationContext(), "File not found", Toast.LENGTH_SHORT).show();
        e.printStackTrace();
        }
        // System.out.println(sampleList);
        for(
                int j = 0;j<sampleList.size();j++) {
            linesplit = Arrays.asList(sampleList.get(j).split(" "));
            // System.out.println(linesplit);
            for (int k = 0; k < linesplit.size() - 1; k += 2) {
                sampleHashMap.put(linesplit.get(k), linesplit.get(k + 1));
                // sampleHashMap.remove(linesplit.get(k),linesplit.get(k+1));
            }
        }
            System.out.println("Sampledict : "+sampleHashMap);
            System.out.println("Sampledict size " + sampleHashMap.size());

        }

        public void samplerouterlist()
        {
            List<String> samplerouterList = new ArrayList(sampleHashMap.keySet());
            int deletedroutercount = 0;
            // System.out.println("old sampledict size: "+samplerouterList.size());
            for(
                    int i = 0;i<samplerouterList.size();i++)
            {
                if (!samplerouterlistdict.containsKey(samplerouterList.get(i))) {
                    // System.out.println(samplerouterList.get(i));
                    sampleHashMap.remove(samplerouterList.get(i));

                    deletedroutercount++;

                }
            }
            System.out.println("New sampledict size, intersection: "+sampleHashMap.size());
            System.out.println("Deleted router count is: "+deletedroutercount);


        }

        public void roomdistance() {
            List<Double> roomdistance = new ArrayList<>();
            HashMap<String, Double> roomdistanceHashMap = new HashMap<>();
            List<HashMap<String, ArrayList<String>>> DBroomdict = new ArrayList(DatabaseActivity.roomdatabasedict.values());
            List<String> samplekey = new ArrayList(sampleHashMap.keySet());
             System.out.println("Size of Sampledict: " +sampleHashMap.size());
            double distance = 0;
            double comparevalue = 0;
            int routerNotinSample = 0;
            double samplevalue = 0;
            for (
                    int i = 0; i < DatabaseActivity.roomList.size(); i++) {

                HashMap<String, ArrayList<String>> compareHashMap = new HashMap(DBroomdict.get(i));
                //System.out.println("Comparedict size: " + compareHashMap.size());
                List<String> comparevalues = new ArrayList(DBroomdict.get(i).values());

                // System.out.println("Comparedict: "+compareHashMap);
                // System.out.println("Comparevalues: "+comparevalues);
                distance = 0;
                for (int j = 0; j < samplekey.size(); j++) {
                    List<String> samplevalues = new ArrayList(sampleHashMap.values());
                    samplevalue = Integer.parseInt(samplevalues.get(j));
                    comparevalue = 0;

                    if (compareHashMap.containsKey(samplekey.get(j))) {

                        //System.out.println(compareHashMap.get(samplekey.get(j)));
                        ArrayList<String> comparevalueList = compareHashMap.get(samplekey.get(j));
                        String comparevalueString = comparevalueList.get(0);
                        comparevalue = Double.parseDouble(comparevalueString);
                         System.out.println("Compare Value: "+comparevalue+" SampleValue: "+samplevalue);

                    } else if (!sampleHashMap.containsKey(samplerouterlistdict.get(j))) {
                        // comparevalue=-100;
                        //System.out.println(routerlistdict.get(j));
                        // compareHashMap.put(samplekey.get(j), new ArrayList<>());
                        // compareHashMap.get(samplekey.get(j)).add("-100");
                        //System.out.println("Allocate 100 for router : " + samplekey.get(j) + " which is missing in comparedict.");
                        routerNotinSample++;
                        }
                        distance += Math.pow((comparevalue - samplevalue), 2);

                    }

                    roomdistance.add(distance);
                    // Collections.sort(roomdistance);
                    //System.out.println(roomdistance);
                    roomdistanceHashMap.put(DatabaseActivity.roomList.get(i), roomdistance.get(i));
                }
                // Collections.sort(roomdistance);

                System.out.println(roomdistanceHashMap);

                List<Double> roomdistancevalues = new ArrayList<>(roomdistanceHashMap.values());
                Collections.sort(roomdistancevalues);
                System.out.println(roomdistancevalues);
                // get the key for the lowest values from roomdistanceHashMap

                for (
                        Map.Entry<String, Double> entry : roomdistanceHashMap.entrySet()) {
                    if (entry.getValue().equals(roomdistancevalues.get(0))) {
                        System.out.println("closest match is room : " + entry.getKey());
                        Toast.makeText(getApplicationContext(), "Closest room is: "+entry.getKey(),Toast.LENGTH_SHORT).show();
                    }

                }
                System.out.println(DatabaseActivity.roomList);
                System.out.println("No.of routers not in sample but present in Database: " + routerNotinSample);

            }



            public void clearguessfile () {
                try {
                    File samplefilename = new File(Environment.getExternalStorageDirectory(), "guessScan.txt");
                    samplefilename.delete();
                    Toast.makeText(getApplicationContext(), "File deleted Successfully", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "File to be deleted not found", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }



