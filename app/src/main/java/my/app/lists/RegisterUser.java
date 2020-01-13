package my.app.lists;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import my.app.functions.CustomFunc1;
import my.app.functions.PublicVar;

public class RegisterUser extends PublicVar {
    String username, pass, confpass, fname, lname, mobile, email;
    Button register;
    JSONObject jsonObject;
    String endpoint = "https://gulunodejs.myvnc.com:4050/api/user1";
    StringBuilder sb=new StringBuilder();
    String typemethod ="POST";
    TextView errormessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        spinner=(ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);
        errormessage = (TextView)findViewById(R.id.textView13);
        username = ((TextView)findViewById(R.id.textView7)).getText().toString();
        pass = ((TextView)findViewById(R.id.textView8)).getText().toString();
        confpass = ((TextView)findViewById(R.id.textView9)).getText().toString();
        fname = ((TextView)findViewById(R.id.textView10)).getText().toString();
        lname = ((TextView)findViewById(R.id.textView11)).getText().toString();
        mobile = ((TextView)findViewById(R.id.textView12)).getText().toString();
        email = ((TextView)findViewById(R.id.textView13)).getText().toString();
        register = (Button)findViewById(R.id.button);
        jsonObject = new JSONObject();
        try {
            jsonObject.put("username",username);
            jsonObject.put("pass",pass);
            jsonObject.put("fname",fname);
            jsonObject.put("lname",lname);
            jsonObject.put("mobile",mobile);
            jsonObject.put("email",email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Register().execute("");
            }
        });
    }
    private class Register extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            sb= CustomFunc1.getresponsebody(caInput,endpoint,typemethod,jsonObject);
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            errormessage.setText(sb);
            spinner.setVisibility(View.GONE);
        }
        @Override
        protected void onPreExecute() {
            spinner.setVisibility(View.VISIBLE);
            try {
                caInput = getAssets().open("server.cert");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
