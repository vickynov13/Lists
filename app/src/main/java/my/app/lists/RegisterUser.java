package my.app.lists;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    String endpoint = "https://gulunodejs.myvnc.com:4050/api/register";
    StringBuilder sb=new StringBuilder();
    String typemethod ="PUT";
    TextView errormessage;
    String sqlerror;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        spinner=(ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);
        errormessage = (TextView)findViewById(R.id.textView13);
        //errormessage.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        register = (Button)findViewById(R.id.button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Register().execute("");
            }
        });
    }
    private class Register extends AsyncTask<String, Void, String> {
        Boolean validationres = false;
        String errormsg=null;
        @Override
        protected String doInBackground(String... params) {
            if(username.isEmpty()||pass.isEmpty()||confpass.isEmpty()||fname.isEmpty()||lname.isEmpty()||mobile.isEmpty()||email.isEmpty()){
                errormsg  = "All Fields are Mandatory";
            }else if(!pass.equals(confpass)){
                errormsg = "Passwords Does not match";
            }else if(!(mobile.length() ==10)){
                errormsg = "Mobile number should have 10 digits";
            }else if((!email.contains("@"))||(!email.contains("."))){
                errormsg = "Enter Valid email";
            }else {
                validationres = true;
                sb= CustomFunc1.getresponsebody(caInput,endpoint,typemethod,jsonObject);
                try {
                    JSONObject ress = new JSONObject(sb.toString());
                    sqlerror = ress.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {

            if(!validationres){
                errormessage.setText(errormsg);
                errormessage.setTextColor(getResources().getColor(R.color.errorColor));
            }else{
                if(sqlerror.contains("username_UNIQUE")){
                    errormsg = "Username already registered";
                    errormessage.setTextColor(getResources().getColor(R.color.errorColor));
                }else if(sqlerror.contains("mobile_UNIQUE")){
                    errormsg = "Mobile No.already registered";
                    errormessage.setTextColor(getResources().getColor(R.color.errorColor));
                }else if(sqlerror.contains("emailid_UNIQUE")){
                    errormsg = "Email Already Registered";
                    errormessage.setTextColor(getResources().getColor(R.color.errorColor));
                }else if(sqlerror.contains("serverdown")){
                    errormsg = "Server Down";
                    errormessage.setTextColor(getResources().getColor(R.color.errorColor));
                }else{
                    errormsg = sqlerror;
                    errormessage.setTextColor(getResources().getColor(R.color.passcolor));
                }
                errormessage.setText(errormsg);
            }
            spinner.setVisibility(View.GONE);
        }
        @Override
        protected void onPreExecute() {
            spinner.setVisibility(View.VISIBLE);
            username = ((EditText)findViewById(R.id.editText7)).getText().toString();
            pass = ((EditText)findViewById(R.id.editText8)).getText().toString();
            confpass = ((EditText)findViewById(R.id.editText9)).getText().toString();
            fname = ((EditText)findViewById(R.id.editText10)).getText().toString();
            lname = ((EditText)findViewById(R.id.editText11)).getText().toString();
            mobile = ((EditText)findViewById(R.id.editText12)).getText().toString();
            email = ((EditText)findViewById(R.id.editText13)).getText().toString();
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
