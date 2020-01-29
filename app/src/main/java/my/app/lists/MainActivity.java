package my.app.lists;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import my.app.functions.CustomFunc1;
import my.app.functions.PublicVar;

public class MainActivity extends PublicVar {
    TextView register, forgotpass;
    Button login;
    Intent registerreq;
    Intent usermain;
    String username, pass, deviceid;
    JSONObject jsonObject;
    StringBuilder sb=new StringBuilder();
    String typemethod ="POST";
    TextView errormessage;
    String sqlresp =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerreq = new Intent(this,RegisterUser.class);
        usermain = new Intent(this,UserMain.class);
        spinner=(ProgressBar)findViewById(R.id.progressBar2);
        spinner.setVisibility(View.GONE);
        register = (TextView)findViewById(R.id.textView16);
        forgotpass = (TextView)findViewById(R.id.textView17);
        errormessage = (TextView)findViewById(R.id.textView18);
        login=(Button)findViewById(R.id.button2);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setVisibility(View.VISIBLE);
                register();
            }
        });
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setVisibility(View.VISIBLE);
                forgotlogin();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setVisibility(View.VISIBLE);
                new LoginTask().execute("");
            }
        });
    }

    private void forgotlogin() {
        spinner.setVisibility(View.GONE);
    }

    private void register() {
        //Intent registerreq = new Intent(this,RegisterUser.class);
        spinner.setVisibility(View.GONE);
        startActivity(registerreq);
    }
    private class LoginTask extends AsyncTask<String, Void, String> {
        Boolean validationres = false;
        String errormsg=null;
        InputStream caInput;
        @Override
        protected String doInBackground(String... params) {
            if(username.isEmpty()||pass.isEmpty()){
                sqlresp="0";
            }else {
                validationres = true;
                sb= CustomFunc1.getresponsebody(caInput,"https://gulunodejs.myvnc.com:4050/api/login",typemethod,jsonObject);
                try {
                    JSONObject ress = new JSONObject(sb.toString());
                    String stat = ress.getString("error");
                    JSONArray ja = ress.getJSONArray("data");
                    JSONObject object3 = ja.getJSONObject(0);
                    sqlresp = object3.getString("COUNT(userid)");

                    //sqlresp = sb.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            if(sqlresp.equals("0")){
                errormsg = "Incorrect Username / Password";
                errormessage.setTextColor(getResources().getColor(R.color.errorColor));
                errormessage.setText(errormsg);
                spinner.setVisibility(View.GONE);
            }else if(sqlresp.equals("1")){
                spinner.setVisibility(View.GONE);
                finish();
                startActivity(usermain);
            }

        }
        @Override
        protected void onPreExecute() {
            username = ((EditText)findViewById(R.id.editText)).getText().toString();
            pass = ((EditText)findViewById(R.id.editText2)).getText().toString();
            deviceid=null;
            jsonObject = new JSONObject();
            try {
                jsonObject.put("username",username);
                jsonObject.put("pass",pass);
                jsonObject.put("deviceid",deviceid);
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
