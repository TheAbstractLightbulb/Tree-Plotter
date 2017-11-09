package com.example.cohen.tree_plotter130;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by cohen on 14/09/2017.
 */

public class AddNewPlotMap extends Activity {
    public EditText name;
    public EditText owner;
    public EditText mapType;
    public EditText address;
    public EditText date;
    public String nameVal;
    public String ownerVal;
    public String mapTypeVal;
    public String addressVal;
    public String dateVal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_plot_map);

        name = findViewById(R.id.NameBox);
        owner = findViewById(R.id.Owner);
        mapType = findViewById(R.id.MapType);
        address = findViewById(R.id.Address);
        date = findViewById(R.id.Date);

        nameVal = name.getText().toString();
        ownerVal = owner.getText().toString();
        mapTypeVal = mapType.getText().toString();
        addressVal = address.getText().toString();
        dateVal = date.getText().toString();

    }

    public void toMap(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("name", nameVal);
        intent.putExtra("owner", ownerVal);
        intent.putExtra("mapType", mapTypeVal);
        intent.putExtra("address", addressVal);
        intent.putExtra("date", dateVal);
        startActivity(intent);
    }
}
