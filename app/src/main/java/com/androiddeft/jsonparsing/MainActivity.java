package com.androiddeft.jsonparsing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private EditText input;
    private Button search;
    private Context context;
    Spinner spinner;
    Intent myIntent;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this.getApplicationContext();
        myIntent = new Intent(context, ListActivity.class);


        input = (EditText) findViewById(R.id.et_search);



        spinner = (Spinner) findViewById(R.id.spinner);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);



        search = (Button) findViewById(R.id.btn_search);
        search.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {



                myIntent.putExtra("searchTag", input.getText().toString());
                myIntent.putExtra("category", spinner.getSelectedItem().toString());


                startActivity(myIntent);

            }

        });


    }


}
