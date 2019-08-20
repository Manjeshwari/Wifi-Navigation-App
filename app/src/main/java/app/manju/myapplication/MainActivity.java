package app.manju.myapplication;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    /*public void ClickFunction(View view) {

        EditText RoomNumber = (EditText) findViewById(R.id.RoomeditText);
        Log.i("Info", "Button pressed!");
        Log.i("Values", RoomNumber.getText().toString());
        String room = RoomNumber.getText().toString();
        //HashMap<String, String> map = new HashMap<String, String>();
        //map.put("RoomNumber", RoomNumber.getText().toString());
        Toast.makeText(this, "Saved Sucessfully", Toast.LENGTH_SHORT).show();
    }*/

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
        Guess.setOnClickListener(this);
        Scan.setOnClickListener(this);
        Build.setOnClickListener(this);

        RoomNumber.setText(" ");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.guessButton:
                Intent WifiData = new Intent(getApplicationContext(), GuessActivity.class);
                startActivity(WifiData);
                break;
            case R.id.button2:
                Intent intent = new Intent(getApplicationContext(), WifiScannerActivity.class);
                EditText RoomNumber = (EditText) findViewById(R.id.RoomeditText);
                String room = RoomNumber.getText().toString();
                intent.putExtra("RoomNumber", room);
                startActivity(intent);
                break;
            case R.id.dbButton:
                Intent DB = new Intent(MainActivity.this,DatabaseActivity.class);
                startActivity(DB);
        }
    }


}


