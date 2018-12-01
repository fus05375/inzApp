package com.androiddeft.jsonparsing;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.androiddeft.jsonparsing.adapter.EmployeeAdapter;
import com.androiddeft.jsonparsing.beans.EmployeeDetails;
import com.androiddeft.jsonparsing.utils.BottomNavigationViewHelper;
import com.androiddeft.jsonparsing.utils.HttpJsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListActivity extends AppCompatActivity {
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "myData";
    private static final String KEY_TITLE = "title";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_PRICE = "price";
    private static final String KEY_LINK = "link";
    private static final String KEY_SOURCE = "source";
    private int page =1;

    private ProgressDialog pDialog;
    private int success;
    private EmployeeAdapter adapter;
    private String searchTag;
    private String category;
    Map<String,String> categoryMap;
    private ImageButton previousBtn;
    private ImageButton nextBtn;

    private Context context;

    private String url = "http://192.168.0.234:3000/search/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_main);




        nextBtn = (ImageButton) findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                page++;

                Toast msg = Toast.makeText(getBaseContext(), String.valueOf(page),
                        Toast.LENGTH_LONG);
                msg.show();

                new FetchEmployeeDetails().execute();


            }

        });

        previousBtn = (ImageButton) findViewById(R.id.previousBtn);
        previousBtn.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {


                if(page>=2) {
                    page--;

                    Toast msg = Toast.makeText(getBaseContext(), String.valueOf(page),
                            Toast.LENGTH_LONG);
                    msg.show();

                    new FetchEmployeeDetails().execute();
                }else{

                    Toast msg = Toast.makeText(getBaseContext(), "Jesteś na pierwszej stronie!",
                            Toast.LENGTH_LONG);
                    msg.show();

                }


            }

        });






        // receive the arguments from the previous Activity
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }

        // assign the values to string-arguments
        searchTag= extras.getString("searchTag");

        category= extras.getString("category");

        Toast msg = Toast.makeText(getBaseContext(), category,
                Toast.LENGTH_LONG);
        msg.show();

        categoryMap = new HashMap<>();
        categoryMap.put("Wszystko","all");
        categoryMap.put("Moda","moda");
        categoryMap.put("Nieruchomosci","nieruchomosci");
        categoryMap.put("Dom i ogrod","dom-i-ogrod");
        categoryMap.put("Muzyka","muzyka-i-rozrywka");
        categoryMap.put("Motoryzacja","motoryzacja");
        categoryMap.put("Oferty pracy","oferty-pracy");
        categoryMap.put("Dla dziecka","dla-dziecka");
        categoryMap.put("Elektronika","elektronika");
        categoryMap.put("Uslugi","uslugi");
        categoryMap.put("Zwierzaki","zwierzaki");
        categoryMap.put("Sportowe","sportowe");





        //Call the AsyncTask
        new FetchEmployeeDetails().execute();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_arrow:
                        Intent intent0 = new Intent(ListActivity.this, MainActivity.class);
                        startActivity(intent0);
                        break;


                    case R.id.ic_android:
                        Intent intent1 = new Intent(ListActivity.this, ListActivity.class);
                        startActivity(intent1);
                        break;


                    case R.id.ic_center_focus:
                        Intent intent3 = new Intent(ListActivity.this, ListActivity.class);
                        startActivity(intent3);
                        break;


                }


                return false;
            }
        });

    }

    private class FetchEmployeeDetails extends AsyncTask<String, String, String> {
        JSONObject response;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(ListActivity.this);
            pDialog.setMessage("Ładowanie.. Proszę czekać...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser jsonParser = new HttpJsonParser();
            response = jsonParser.makeHttpRequest(url +searchTag +"/"+ categoryMap.get(category)+"/"+page ,"GET",null);
            try {
                success = response.getInt(KEY_SUCCESS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {


            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {

                    ListView listView =(ListView)findViewById(R.id.employeeList);
                    if (success == 1) {

                        try {
                            JSONArray employeeArray =  response.getJSONArray(KEY_DATA);
                            List<EmployeeDetails> employeeList = new ArrayList<>();
                            //Populate the EmployeeDetails list from response
                            for (int i = 0; i<employeeArray.length();i++){
                                EmployeeDetails employeeDetails = new EmployeeDetails();
                                JSONObject employeeObj = employeeArray.getJSONObject(i);
                                employeeDetails.setTitle(employeeObj.getString(KEY_TITLE));
                                if (employeeObj.has(KEY_IMAGE)) {
                                    employeeDetails.setImage(employeeObj.getString(KEY_IMAGE));
                                }
                                employeeDetails.setPrice(employeeObj.getString(KEY_PRICE));
                                employeeDetails.setLink(employeeObj.getString(KEY_LINK));
                                employeeDetails.setSource(employeeObj.getString(KEY_SOURCE));


                                employeeList.add(employeeDetails);
                            }
                            //Create an adapter with the EmployeeDetails List and set it to the LstView
                            adapter = new EmployeeAdapter(employeeList,getApplicationContext());
                            listView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(ListActivity.this,
                                "Some error occurred while loading data",
                                Toast.LENGTH_LONG).show();

                    }
                }
            });
        }

    }

}
