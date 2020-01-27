package app.manju.myapplication;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText RoomNumber = (EditText) findViewById(R.id.RoomeditText);
        Log.i("Info", "Button pressed!");
        Log.i("Values", RoomNumber.getText().toString());


        Button Guess = (Button) findViewById(R.id.guessButton);
        Button Scan = (Button) findViewById(R.id.button2);
        Button Build = (Button) findViewById(R.id.dbButton);
        Button Map = (Button) findViewById(R.id.mapbutton);
        Guess.setOnClickListener(this);
        Scan.setOnClickListener(this);
        Build.setOnClickListener(this);
        Map.setOnClickListener(this);
        RoomNumber.setText("");
        String room = RoomNumber.getText().toString();


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.guessButton:
                Intent WifiData = new Intent(getApplicationContext(), GuessActivity.class);
                startActivity(WifiData);
                break;
            case R.id.button2:

                EditText RoomNumber = (EditText) findViewById(R.id.RoomeditText);

                String room = RoomNumber.getText().toString();
                if(room.length()==0)
                {
                    RoomNumber.setError("Please enter the Room Number");

                    }else {
                    Intent intent = new Intent(getApplicationContext(), WifiScannerActivity.class);
                    intent.putExtra("RoomNumber", room);
                    for (int i = 0; i < 25; i++) {
                        startActivity(intent);
                    }
                }

                break;
            case R.id.dbButton:
                Intent DB = new Intent(MainActivity.this,DatabaseActivity.class);
                startActivity(DB);
                break;
            case R.id.mapbutton:

                String closestRoom = GuessActivity.ClosestRoom;
                Intent mapIntent = new Intent(MainActivity.this,MapActivity.class);
                mapIntent.putExtra("Source",closestRoom);
                System.out.println(closestRoom);
                startActivity(mapIntent);
                //break;
        }
    }


}


