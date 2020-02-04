package my.app.lists;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class SearchUser extends PublicVar {
    EditText userinput_et;
    Button submitsearch;
    String userinput;
    StringBuilder sb = new StringBuilder();
    String[] itemTextArr;
    String sqlresp =null;
    ListView listView1;
    ArrayAdapter<String> adapter;
    JSONArray ja;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        userinput_et = (EditText)findViewById(R.id.editText3);
        submitsearch = (Button)findViewById(R.id.button3);
        listView1 = (ListView) findViewById(R.id.userlist);
        spinner=(ProgressBar)findViewById(R.id.progressBar3);
        spinner.setVisibility(View.GONE);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, itemTextArr);
        submitsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setVisibility(View.VISIBLE);

                new UserlistData().execute("");
            }
        });
        userinput_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                    userinput_et.setText("");

            }
        });
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String item = Integer.toString(position);
                try {
                    JSONObject object3 = ja.getJSONObject(position);
                    guestusername = object3.getString("username");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getBaseContext(), guestusername, Toast.LENGTH_LONG).show();
                Viewuserlist();
            }
        });
    }

    private void Viewuserlist() {
        Intent viewguestlist = new Intent(this,SelectedUsersList.class);
        spinner.setVisibility(View.GONE);
        //mainuser = false;
        startActivity(viewguestlist);
    }


    private class UserlistData extends AsyncTask<String, Void, String> {
        InputStream caInput;
        JSONObject jsonObject;
        @Override
        protected String doInBackground(String... params) {
            sb= CustomFunc1.getresponsebody(caInput,"https://gulunodejs.myvnc.com:4050/api/usersearchresult","POST",jsonObject);
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

            listView1.setAdapter(adapter);
            spinner.setVisibility(View.GONE);
        }

        @Override
        protected void onPreExecute() {
            jsonObject = new JSONObject();
            userinput = userinput_et.getText().toString();
            try {
                jsonObject.put("userinput",userinput);
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
