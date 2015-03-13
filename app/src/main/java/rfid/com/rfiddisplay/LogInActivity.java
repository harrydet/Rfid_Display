package rfid.com.rfiddisplay;

import android.app.ActionBar;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.ParseInstallation;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class LogInActivity extends Activity implements OnTaskCompleted, View.OnClickListener {

    private Button signInButton;
    private EditText enterTicketEditText;
    private String dbUrl = "http://178.62.34.201/phpDatabase/db_logInUserApp.php";
    private JSONParser asyncRequest;
    private String passportNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Add a progress bar
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.activity_log_in);

        enterTicketEditText = (EditText)findViewById(R.id.ticket_number_edittext);
        signInButton = (Button)findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.log_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskCompleted(JSONObject jObj) {
        setProgressBarIndeterminateVisibility(false);
        //Toast.makeText(getApplicationContext(), "Result: " + jObj.toString(), Toast.LENGTH_LONG).show();
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("auth_token", "authenticated");
        editor.commit();

        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("passportNo", passportNumber);
        installation.saveInBackground();

        Intent newActivity = new Intent(this, LandingActivity.class);
        this.startActivity(newActivity);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case  R.id.sign_in_button:
                setProgressBarIndeterminateVisibility(true);
                passportNumber = enterTicketEditText.getText().toString();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("tag", "log_in"));
                params.add(new BasicNameValuePair("ticket_no", passportNumber));
                params.add(new BasicNameValuePair("pin", "5"));
                asyncRequest = new JSONParser(this, dbUrl);
                asyncRequest.execute(params);
        }
    }
}