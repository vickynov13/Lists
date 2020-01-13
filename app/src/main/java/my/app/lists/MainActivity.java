package my.app.lists;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import my.app.functions.PublicVar;

public class MainActivity extends PublicVar {
    TextView register, forgotpass;
    Button login;
    Intent registerreq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerreq = new Intent(this,RegisterUser.class);
        spinner=(ProgressBar)findViewById(R.id.progressBar2);
        spinner.setVisibility(View.GONE);
        register = (TextView)findViewById(R.id.textView16);
        forgotpass = (TextView)findViewById(R.id.textView17);
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
        @Override
        protected String doInBackground(String... params) {
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            startActivity(registerreq);
            spinner.setVisibility(View.GONE);
        }
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
