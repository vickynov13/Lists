package my.app.lists;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import my.app.functions.CustomFunc1;
import my.app.functions.PublicVar;

public class UserRequests extends PublicVar {
    ListView reqlistView;
    JSONArray ja;
    StringBuilder sb = new StringBuilder();
    String[] itemTextArr;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_requests);
        reqlistView = (ListView) findViewById(R.id.userreqlist);
        spinner=(ProgressBar)findViewById(R.id.progressBar3);
        spinner.setVisibility(View.GONE);
        new GetRequests().execute("");

        reqlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String item = Integer.toString(position);
                try {
                    JSONObject object3 = ja.getJSONObject(position);
                    guestusername = object3.getString("username");
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //secret = "yes";
                                    new UpdateRequest().execute("");
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //secret = "no";
                                    //new SelectedUsersList.Addtasktouser().execute("");
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(UserRequests.this);
                    builder.setMessage("Do you want to allow this user to View and add items to your list?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                } catch (JSONException e) {
                    //e.printStackTrace();
                    String error = e.toString();
                    Toast.makeText(getBaseContext(), error, Toast.LENGTH_LONG).show();
                }
            }
        });


    }
    private class GetRequests extends AsyncTask<String, Void, String> {
        InputStream caInput;
        JSONObject jsonObject;
        @Override
        protected String doInBackground(String... params) {
            sb= CustomFunc1.getresponsebody(caInput,"https://gulunodejs.myvnc.com:4050/api/getuserrequests","POST",jsonObject);
            try {
                JSONObject ress = new JSONObject(sb.toString());
                ja = ress.getJSONArray("data");
                int listlen;
                listlen=ja.length();
                itemTextArr=new String[listlen];
                for(int i=0;i<listlen;i++) {
                    JSONObject object3 = ja.getJSONObject(i);
                    itemTextArr[i] = object3.getString("name");
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
            reqlistView.setAdapter(adapter);
            spinner.setVisibility(View.GONE);
        }

        @Override
        protected void onPreExecute() {
            spinner.setVisibility(View.VISIBLE);
            jsonObject = new JSONObject();
            try {
                jsonObject.put("myname",myusername);
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

    private class UpdateRequest extends AsyncTask<String, Void, String> {
        InputStream caInput;
        JSONObject jsonObject;
        String insertresp;
        @Override
        protected String doInBackground(String... params) {
            sb= CustomFunc1.getresponsebody(caInput,"https://gulunodejs.myvnc.com:4050/api/grantaccess","POST",jsonObject);
            try {
                JSONObject ress = new JSONObject(sb.toString());
                insertresp = ress.getString("error");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if(insertresp.equalsIgnoreCase("false")) {
                Toast.makeText(getBaseContext(), "Access Granted", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getBaseContext(), "Could not Approve", Toast.LENGTH_LONG).show();
            }
            new GetRequests().execute("");
            spinner.setVisibility(View.GONE);
        }

        @Override
        protected void onPreExecute() {
            spinner.setVisibility(View.VISIBLE);
            jsonObject = new JSONObject();
            try {
                jsonObject.put("myname",myusername);
                jsonObject.put("guestname",guestusername);
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
