package app.manju.myapplication;

import android.app.Activity;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class DatabaseActivity extends AppCompatActivity {

    List<String> rooms = new ArrayList<String>();
    HashMap<String, List<String>> roomdict = new HashMap<String, List<String>>();
    static HashMap<String, HashMap<String, List<String>>> roomdatabasedict = new HashMap<>();
    static HashMap<String, String> routerlistdict = new HashMap<>();
    static List<String> roomList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        readFile();
        roomdictionary();
        roomdatabasedict();
        routerlistdict();
        Toast.makeText(getApplicationContext(), "Database Created Sucessfully", Toast.LENGTH_SHORT).show();
    }

    public void readFile() {
        try {
            File filename = new File(Environment.getExternalStorageDirectory(), "wifiscans.txt");
            Scanner scanner = new Scanner(filename);
            int count = 1;
            for (int i = 0; i < count; i++) {
                while (scanner.hasNextLine()) {
                    rooms.add(scanner.nextLine());
                    count++;
                }

            }
            System.out.println(rooms);
            Toast.makeText(getApplicationContext(), "File read Successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "File not found", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void roomdictionary() {
        for (int i = 0; i < rooms.size() - 1; i += 2) {
            String roomnumber = rooms.get(i);
            if (roomdict.containsKey(roomnumber)) {
                // System.out.println("seen this room before");
            } else {
                // System.out.println("first time seeing this room");
                roomdict.put(roomnumber, new ArrayList<String>());
            }
            String samplelement = rooms.get(i + 1);
            roomdict.get(roomnumber).add(samplelement);
        }

        System.out.println("roomdict: " + roomdict);
    }

    /******************Complete Database********************/
    public void roomdatabasedict() {
        roomList.addAll(roomdict.keySet());
        System.out.println(roomList);

        for (int i = 0; i < roomList.size(); i++) {
            String room = roomList.get(i);

            HashMap<String, List<String>> roomrouterdict = new HashMap<>();

            if (roomdict.containsKey(room)) {
                List<String> roomreading = new ArrayList<>();// List of all the routersignal pairs for each room from the roomdict
                roomreading.addAll(roomdict.get(room));
                for (int j = 0; j < roomreading.size(); j++) {
                    List<String> reading = Arrays.asList(roomreading.get(j).split(" "));


                    for (int k = 0; k < reading.size() - 1; k += 2) {
                        String routerid = reading.get(k);

                        if (roomrouterdict.containsKey(routerid)) {

                        } else {
                            roomrouterdict.put(routerid, new ArrayList<String>());
                        }
                        String signalStrength = reading.get(k + 1);
                        roomrouterdict.get(routerid).add(signalStrength);
                    }
                }

                roomdatabasedict.put(room, roomrouterdict);
            }

        }
        //System.out.println("Complete database "+" : "+roomdatabasedict);

        /********************************
         * #go through each router and see if it needs padding
         ***********************/

        List<String> router = new ArrayList<String>(routerlistdict.keySet());
        // System.out.println(router);
        // System.out.println(roomdatabasedict.values());
        int maxsamples,howmanysamples;
        List<HashMap<String, ArrayList<String>>> routersignalHashMap = new ArrayList(roomdatabasedict.values());
        //System.out.println(routersignalHashMap);
        System.out.println("Size of unpadded RoutersignalHashMap"+routersignalHashMap.size());
        for(int i=0;i<roomList.size();i++) {
            maxsamples=0;
            //howmanysamples=0;
            List<ArrayList<String>> signaList=new ArrayList<ArrayList<String>>(routersignalHashMap.get(i).values());
            for(int j=0;j<signaList.size();j++) {

                howmanysamples=signaList.get(j).size();
                if(howmanysamples>maxsamples) {
                    maxsamples=howmanysamples;
                }

            }
            System.out.println("maxsamples for room "+roomList.get(i)+"is "+maxsamples);

            for(int k=0;k<signaList.size();k++) {
                howmanysamples=signaList.get(k).size();
                if(howmanysamples<maxsamples) {
                    int pad=maxsamples-howmanysamples;
                    //System.out.println(pad);
                    String padList = "";
                    //padList+=padList;
                    //signaList.get(k).add(padList);

                    HashMap<String, ArrayList<String>> individualroomdict= new HashMap<>(routersignalHashMap.get(i));
                    //System.out.println(individualroomdict);

                    for(int m=0;m<pad;m++) {

                        padList="-100";
                        signaList.get(k).add(padList);
                    }


                }
            }
        }
/********************************PADDED DICTIONARY***********************/

        //HashMap<String, HashMap<String, List<String>>> paddeddictionary=new HashMap<String, HashMap<String, List<String>>>();
        List<HashMap<String, ArrayList<String>>> Allroomdict= new ArrayList(roomdatabasedict.values());

        for(
                int i = 0;i<roomList.size();i++)
        {
            HashMap<String, List<String>> individualroomdict = new HashMap<String, List<String>>( Allroomdict.get(i));
            // System.out.println(individualroomdict);

            for (int j = 0; j < router.size(); j++) {
                if (!individualroomdict.containsKey(router.get(j))) {
                    // System.out.println(router.get(j));
                    individualroomdict.put(router.get(j), new ArrayList<String>());
                    String padString = "-100";
                    individualroomdict.get(router.get(j)).add("-100");
                    // System.out.println(individualroomdict.get(router.get(j)));
                    for (int k = 0; k > routerlistdict.size(); k++) {
                        if (roomdatabasedict.containsKey(roomList.get(i))) {
                            System.out.println("roomlist: " + roomList.get(i));
                            if ( Allroomdict.get(i).containsKey(router.get(j))) {
                            }
                        }
                    }

                }
            }
            // System.out.println(individualroomdict);
            roomdatabasedict.put(roomList.get(i), individualroomdict);
        }
//System.out.println(paddeddictionary);
       // System.out.println("New: "+ roomdatabasedict);
        // /******************************** AVERAGED DATABASE ***********************/

        HashMap<String, HashMap<String, ArrayList<String>>> averageddatabase = new HashMap<String, HashMap<String, ArrayList<String>>>();
        List<HashMap<String, ArrayList<String>>> avgroomdict = new ArrayList(roomdatabasedict.values());
        double sum = 0;
        int counter = 0;
        double average;
        for(int i = 0;i<roomList.size();i++)
        {
            HashMap<String, ArrayList<String>> avgHashMap = new HashMap(avgroomdict.get(i));
            ArrayList list = new ArrayList(avgHashMap.values());

            average = 0;
            for (int j = 0; j < list.size(); j++) {
                List<String> sampleList2 = new ArrayList();
                sampleList2.addAll((Collection<? extends String>) list.get(j));
                Collections.sort(sampleList2);
                // System.out.println("This is "+sampleList2);
                // System.out.println(Collections.sort(sampleList2));
                sum = 0;
                int count = 0;

                for (int k = 0; k < sampleList2.size(); k++) {
                    // System.out.println(sampleList2.get(k));
                    sum = sum + Integer.parseInt(sampleList2.get(k));
                    // System.out.println(sum);
                    count += 1;
                    // System.out.println(count);
                }
                counter = count;
                // System.out.println(counter);
                average =(sum / count);
                // System.out.println(average);
                // System.out.println(count);
                if (counter > 1) {
                    String averageString = Double.toString(average);
                    List<String> averageList = new ArrayList<>();
                    averageList.add(averageString);
                    // System.out.println(avgroomdict.get(i).containsValue(sampleList.get(i)));
                    for (Entry<String, ArrayList<String>> entry : (avgroomdict.get(i)).entrySet()) {
                        if (entry.getValue().equals(list.get(j))) {
                            // System.out.println(entry.getKey());

                            avgroomdict.get(i).replace(entry.getKey(), new ArrayList<String>());
                            avgroomdict.get(i).put(entry.getKey(), (ArrayList<String>) averageList);
                        }

                    }
                }

                averageddatabase.put(roomList.get(i), avgroomdict.get(i));

            }
            //System.out.println(roomList.get(i)+ avgroomdict.get(i));
        }

       // System.out.println("Complete Database after Average: " + roomdatabasedict);
    }

    public void routerlistdict(){

        List<String> roomdatabasekeys = new ArrayList<String>(roomdatabasedict.keySet());
        // System.out.println(roomdatabasekeys);

        for (int i = 0; i < roomdatabasekeys.size(); i++) {
            List<HashMap<String, ArrayList<String>>> routersList = new ArrayList(roomdatabasedict.values());
            // System.out.println(routersList);

            for (HashMap<String, ArrayList<String>> map : routersList) {
                for (Map.Entry<String, ArrayList<String>> entrySet : map.entrySet()) {
                    for (int j = 0; j < routersList.size(); j++) {

                        if (map.entrySet().contains(entrySet.getKey())) {

                        } else {
                            routerlistdict.put(entrySet.getKey(), "2");
                        }
                        routerlistdict.put(entrySet.getKey(), "1");
                    }
                }

            }
        }
        System.out.println("Routerlistdict: "+ routerlistdict);
    }


    //----------------------make a dictionary of routers present-----------------------




}

