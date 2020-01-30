package my.app.lists;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import my.app.functions.CustomFunc1;
import my.app.functions.PublicVar;

public class UserMain extends PublicVar {
    StringBuilder sb=new StringBuilder();
    TextView welcomemsgtv, mobiletv, emailtv;
    String welcomemsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        spinner=(ProgressBar)findViewById(R.id.progressBar2);
        welcomemsgtv = (TextView)findViewById(R.id.textView1);
        mobiletv = (TextView)findViewById(R.id.textView3);
        emailtv = (TextView)findViewById(R.id.textView5);
        new LoadPage().execute("");
    }

    private class LoadPage extends AsyncTask<String, Void, String> {
        JSONObject jsonObject;
        InputStream caInput;
        @Override
        protected String doInBackground(String... params) {
            sb= CustomFunc1.getresponsebody(caInput,"https://gulunodejs.myvnc.com:4050/api/getusermain","POST",jsonObject);
            try {

                JSONObject ress = new JSONObject(sb.toString());
                //String stat = ress.getString("error");
                int arrlen =0;
                JSONArray ja = ress.getJSONArray("data");
                JSONObject joa = ja.getJSONObject(0);
                //JSONObject lastname_jo = ja.getJSONObject(1);
                //JSONObject mobile_jo = ja.getJSONObject(2);
                //JSONObject emailid_jo = ja.getJSONObject(3);
                firstname = joa.getString("firstname");
                lastname = joa.getString("lastname");
                mobile = joa.getString("mobile");
                emailid = joa.getString("emailid");
                firstname = firstname.toLowerCase();
                firstname = firstname.substring(0,1).toUpperCase() + firstname.substring(1).toLowerCase();
                lastname = lastname.toLowerCase();
                lastname = lastname.substring(0,1).toUpperCase() + lastname.substring(1).toLowerCase();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            welcomemsg = "Welcome "+firstname+" "+lastname+" , You can manage account with below options. \n Make sure your details are uptodate even though we dont have any purpose for that.";
            welcomemsgtv.setText(welcomemsg);
            mobiletv.setText(mobile);
            emailtv.setText(emailid);
            spinner.setVisibility(View.GONE);
        }

        @Override
        protected void onPreExecute() {
            jsonObject = new JSONObject();
            try {
                jsonObject.put("userid",userid);
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
