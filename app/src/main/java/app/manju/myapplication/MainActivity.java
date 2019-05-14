package app.manju.myapplication;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    public void ClickFunction(View view) {

        EditText RoomeditText = (EditText) findViewById(R.id.RoomeditText);
        Log.i("Info", "Button pressed!");
        Log.i("Values", RoomeditText.getText().toString());
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("RoomNumber", RoomeditText.getText().toString());
        Toast.makeText(this, "Saved Sucessfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button Guess = (Button) findViewById(R.id.guessButton);
        Button Scan = (Button) findViewById(R.id.button2);
        Guess.setOnClickListener(this);
        Scan.setOnClickListener(this);
        /*final Button Guess = (Button) findViewById(R.id.guessButton);
        Guess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent WifiData = new Intent(getApplicationContext(), WifiData.class);
                startActivity(WifiData);


            }
        });*/

    }
        @Override
        public void onClick (View v){
switch (v.getId()){
    case R.id.guessButton:
        Intent WifiData = new Intent(getApplicationContext(), WifiData.class);
        startActivity(WifiData);
        break;
    case R.id.button2:
        Intent intent = new Intent(getApplicationContext(),WifiScannerActivity.class);
        startActivity(intent);
        }
    }
}
