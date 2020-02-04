package my.app.lists;


import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import my.app.functions.CustomFunc1;
import my.app.functions.PublicVar;

public class SelectedUsersList extends PublicVar {
    ListView listView1;
    FloatingActionButton fab;
    ArrayAdapter<String> adapter;
    StringBuilder sb = new StringBuilder();
    JSONArray ja;
    String[] itemTextArr;
    EditText userinput_et;
    String userinput;
    String secret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_users_list);
        listView1 = (ListView) findViewById(R.id.userlist);
        Button button4 = (Button) findViewById(R.id.button4);
        fab = (FloatingActionButton)findViewById(R.id.floatingActionButton);
        userinput_et = (EditText)findViewById(R.id.editText4);
        spinner=(ProgressBar)findViewById(R.id.progressBar3);
        spinner.setVisibility(View.GONE);
        new UserToDos().execute("");

        userinput_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                userinput_et.setText("");
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //-------------------building dialog box-------------------------
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                secret = "yes";
                                new Addtasktouser().execute("");
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                secret = "no";
                                new Addtasktouser().execute("");
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Do you want to add this as secret?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                //--------------------------------------------------------
                //new Addtasktouser().execute("");
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UserToDos().execute("");
            }
        });
    }



    private class UserToDos extends AsyncTask<String, Void, String> {
        InputStream caInput;
        JSONObject jsonObject;
        @Override
        protected String doInBackground(String... params) {
            sb= CustomFunc1.getresponsebody(caInput,"https://gulunodejs.myvnc.com:4050/api/getusertodos","POST",jsonObject);
            try {
                JSONObject ress = new JSONObject(sb.toString());
                ja = ress.getJSONArray("data");
                int listlen;
                listlen=ja.length();
                itemTextArr=new String[listlen];
                for(int i=0;i<listlen;i++) {
                    JSONObject object3 = ja.getJSONObject(i);
                    itemTextArr[i] = object3.getString("todomessage");
                    //itemTextArr[i]=sqlresp;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.simple_list_font, itemTextArr);

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            listView1.setAdapter(adapter);
            spinner.setVisibility(View.GONE);
        }

        @Override
        protected void onPreExecute() {
            spinner.setVisibility(View.VISIBLE);
            jsonObject = new JSONObject();
            try {
                jsonObject.put("guestusername",guestusername);
                jsonObject.put("username",myusername);
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

    private class Addtasktouser extends AsyncTask<String, Void, String> {
        InputStream caInput;
        JSONObject jsonObject;
        @Override
        protected String doInBackground(String... params) {
            sb= CustomFunc1.getresponsebody(caInput,"https://gulunodejs.myvnc.com:4050/api/addtolist","POST",jsonObject);
            try {
                JSONObject ress = new JSONObject(sb.toString());
                ja = ress.getJSONArray("data");
                JSONObject object3 = ja.getJSONObject(0);
                // String  object3.getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.simple_list_font, itemTextArr);




            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            new UserToDos().execute("");
            spinner.setVisibility(View.GONE);
        }

        @Override
        protected void onPreExecute() {
            jsonObject = new JSONObject();
            spinner.setVisibility(View.VISIBLE);
            userinput = userinput_et.getText().toString();
            try {
                jsonObject.put("username",guestusername);
                jsonObject.put("userinput",userinput);
                jsonObject.put("secret",secret);
                jsonObject.put("updatedby",myusername);
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
