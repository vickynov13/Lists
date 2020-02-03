package my.app.lists;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import my.app.functions.CustomFunc1;
import my.app.functions.PublicVar;
import my.app.listpack.ListViewItemCheckboxBaseAdapter;
import my.app.listpack.ListViewItemDTO;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ToDolistMain extends PublicVar {
    ListView listViewWithCheckbox;
    List<ListViewItemDTO> initItemList;
    ListViewItemCheckboxBaseAdapter listViewDataAdapter;
    StringBuilder sb = new StringBuilder();
    String[] itemTextArr;
    String sqlresp =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist);
        new Getlistdata().execute("");

        listViewWithCheckbox = (ListView)findViewById(R.id.list_view_with_checkbox);

        listViewWithCheckbox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long l) {
                Object itemObject = adapterView.getAdapter().getItem(itemIndex);
                ListViewItemDTO itemDto = (ListViewItemDTO)itemObject;
                CheckBox itemCheckbox = (CheckBox) view.findViewById(R.id.list_view_item_checkbox);
                if(itemDto.isChecked())
                {
                    itemCheckbox.setChecked(false);
                    itemDto.setChecked(false);
                }else
                {
                    itemCheckbox.setChecked(true);
                    itemDto.setChecked(true);
                }
            }
        });


        Button selectAllButton = (Button)findViewById(R.id.list_select_all);
        selectAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size = initItemList.size();
                for(int i=0;i<size;i++)
                {
                    ListViewItemDTO dto = initItemList.get(i);
                    dto.setChecked(true);
                }
                listViewDataAdapter.notifyDataSetChanged();
            }
        });

        Button selectNoneButton = (Button)findViewById(R.id.list_select_none);
        selectNoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size = initItemList.size();
                for(int i=0;i<size;i++)
                {
                    ListViewItemDTO dto = initItemList.get(i);
                    dto.setChecked(false);
                }

                listViewDataAdapter.notifyDataSetChanged();
            }
        });

        Button selectReverseButton = (Button)findViewById(R.id.list_select_reverse);
        selectReverseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size = initItemList.size();
                for(int i=0;i<size;i++)
                {
                    ListViewItemDTO dto = initItemList.get(i);

                    if(dto.isChecked())
                    {
                        dto.setChecked(false);
                    }else {
                        dto.setChecked(true);
                    }
                }

                listViewDataAdapter.notifyDataSetChanged();
            }
        });

        Button selectRemoveButton = (Button)findViewById(R.id.list_remove_selected_rows);
        selectRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog alertDialog = new AlertDialog.Builder(ToDolistMain.this).create();
                alertDialog.setMessage("Are you sure to remove selected listview items?");

                alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        int size = initItemList.size();
                        for(int i=0;i<size;i++)
                        {
                            ListViewItemDTO dto = initItemList.get(i);

                            if(dto.isChecked())
                            {
                                initItemList.remove(i);
                                i--;
                                size = initItemList.size();
                            }
                        }

                        listViewDataAdapter.notifyDataSetChanged();
                    }
                });

                alertDialog.show();
            }
        });

    }

    private class Getlistdata extends AsyncTask<String, Void, String> {
        InputStream caInput;
        JSONObject jsonObject;
        @Override
        protected String doInBackground(String... params) {

            sb= CustomFunc1.getresponsebody(caInput,"https://gulunodejs.myvnc.com:4050/api/getusertodos","POST",jsonObject);
            try {
                JSONObject ress = new JSONObject(sb.toString());
                JSONArray ja = ress.getJSONArray("data");
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
            initItemList = this.getInitViewItemDtoList();
            listViewDataAdapter = new ListViewItemCheckboxBaseAdapter(getApplicationContext(), initItemList);

            listViewDataAdapter.notifyDataSetChanged();

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            listViewWithCheckbox.setAdapter(listViewDataAdapter);
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

        private List<ListViewItemDTO> getInitViewItemDtoList()
        {
            //String itemTextArr[] = {"s~Android", "n~iOS", "s~Java", "JavaScript", "JDBC", "JSP", "Linux", "Python", "Servlet", "Windows", "s~Android", "n~iOS", "s~Java", "JavaScript", "JDBC", "JSP", "Linux", "Python", "Servlet", "Windows" };

            List<ListViewItemDTO> ret = new ArrayList<ListViewItemDTO>();

            int length = itemTextArr.length;

            for(int i=0;i<length;i++)
            {
                String itemText = itemTextArr[i];

                ListViewItemDTO dto = new ListViewItemDTO();
                dto.setChecked(false);
                dto.setItemText(itemText);

                ret.add(dto);
            }

            return ret;
        }
    }







    // Return an initialize list of ListViewItemDTO.

}