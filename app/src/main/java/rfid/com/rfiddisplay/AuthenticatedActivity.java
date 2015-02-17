package rfid.com.rfiddisplay;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class AuthenticatedActivity extends Activity {

    HashMap<String, String> serialCache;
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_authenticated);
        String json = getIntent().getStringExtra("com.parse.Data");
        Toast.makeText(getApplicationContext(), json, Toast.LENGTH_SHORT).show();
        TextView topBox = (TextView)findViewById(R.id.top_box);
        topBox.setText(json);

        serialCache = new HashMap<String, String>();
        serialCache.put("335391", "Gate 1");
        serialCache.put("335392", "Gate 2");
        serialCache.put("335393", "Gate 3");

        try {
            if(json != null){
                jsonObject = new JSONObject(json);
                topBox.setText(jsonObject.get("payload").toString());
            }
        } catch (JSONException e) {
            Log.e("JSON ERROR", "Error converting String to JSON Object.");
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.authenticated, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.action_logout:
                SharedPreferences settings = PreferenceManager
                        .getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("auth_token", "NOT_LOGGED_IN");
                editor.commit();
                Intent switchActivity = new Intent(this, SplashScreen.class);
                this.startActivity(switchActivity);
        }
        return super.onOptionsItemSelected(item);
    }
}
