package com.example.databasedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    // define all the elements on the GUI
    Button btn_add, btn_viewAll;
    EditText et_name, et_age;
    Switch sw_activeCustomer;
    ListView lv_customerList;

    ArrayAdapter customerArrayAdapter;
    DataBaseHelper dataBaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataBaseHelper = new DataBaseHelper(MainActivity.this);

        //ShowCustomersOnListView(dataBaseHelper);

        // we have to give value to these elements
        btn_add = findViewById(R.id.btn_add); // R comes from resource file
        btn_viewAll = findViewById(R.id.btn_viewAll);
        et_name = findViewById(R.id.et_name);
        et_age = findViewById(R.id.et_age);
        sw_activeCustomer = findViewById(R.id.sw_active);
        lv_customerList = findViewById(R.id.lv_customerList);

        // button listeners for the add and view all buttons
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CustomerModel customerModel;

                try {
                    customerModel = new CustomerModel(1, et_name.getText().toString(), Integer.parseInt(et_age.getText().toString()), sw_activeCustomer.isChecked());
                    Toast.makeText(MainActivity.this, customerModel.toString(), Toast.LENGTH_SHORT).show();
                }
                catch(Exception ex) {
                    Toast.makeText(MainActivity.this, "An error has occured!", Toast.LENGTH_SHORT).show();
                    customerModel  = new CustomerModel(-1, "error", 0, false);
                }

                DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);
                boolean success = dataBaseHelper.addOne(customerModel);

                Toast.makeText(MainActivity.this, "Success: " + success, Toast.LENGTH_SHORT).show();

                // we have to update the ArrayAdapter, because we have to update the list with all the customers after one new customer was added to the database
                ShowCustomersOnListView(dataBaseHelper);

            }
        });

        btn_viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);

                //Toast.makeText(MainActivity.this, customerList.toString(), Toast.LENGTH_SHORT).show();
                ShowCustomersOnListView(dataBaseHelper);

            }
        });

        lv_customerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // position is the item which was clicked
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                CustomerModel customerModel = (CustomerModel) parent.getItemAtPosition(position);
                dataBaseHelper.deleteOne(customerModel);
                ShowCustomersOnListView(dataBaseHelper);

                Toast.makeText(MainActivity.this, "Deleted: " + customerModel.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void ShowCustomersOnListView(DataBaseHelper dataBaseHelper2) {
        customerArrayAdapter = new ArrayAdapter<CustomerModel>(MainActivity.this, android.R.layout.simple_expandable_list_item_1, dataBaseHelper2.selectAll());
        lv_customerList.setAdapter(customerArrayAdapter);
    }
}