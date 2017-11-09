package com.example.cohen.tree_plotter130;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;




public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String[] navDrawerItemTitles;
    private DrawerLayout drawerLayout;
    private ListView listView;
    Toolbar toolbar;
    public static final String btnTag = "btn";
    public static final String TAG_ADD = "Add";
    private CharSequence drawerTitle;
    private CharSequence title;
    public Animation show_fab;
    public Animation show_fab2;
    android.support.v7.app.ActionBarDrawerToggle drawerToggle;

    private static final String TAG_ACTION_BUTTON = "Action Menu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.Orange));
        title = drawerTitle = getTitle();
        navDrawerItemTitles = getResources().getStringArray(R.array.nav_d_items);
        drawerLayout = (DrawerLayout) findViewById(R.id.mainDrawer);
        listView = (ListView) findViewById(R.id.left_drawer);
        Button button = (Button) findViewById(R.id.btn);
        button.setTag(btnTag);
        button.setOnClickListener(this);

        setupToolbar();

        // ToolBox Image
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.hammers_green);
        // Settings
        ImageView SettingsImagView = new ImageView(this);
        SettingsImagView.setImageResource(R.drawable.settings_green);
        // Add Button
        ImageView AddButton = new ImageView(this);
        AddButton.setImageResource(R.drawable.plus_green);
        // Search Button
        ImageView SearchImage = new ImageView(this);
        SearchImage.setImageResource(R.drawable.searches);


        DrawerObjectDefiner[] drawerObjectDefiners = new DrawerObjectDefiner[5];

        drawerObjectDefiners[0] = new DrawerObjectDefiner(R.drawable.house, "Home");
        drawerObjectDefiners[1] = new DrawerObjectDefiner(R.drawable.import_export_green, "Import/Export");
        drawerObjectDefiners[2] = new DrawerObjectDefiner(R.drawable.photo_green, "Pictures");
        drawerObjectDefiners[3] = new DrawerObjectDefiner(R.drawable.settings_icon, "Settings");
        drawerObjectDefiners[4] = new DrawerObjectDefiner(R.drawable.qustion_mark_green, "Help");

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        DrawerCustomAdapter adapter = new DrawerCustomAdapter(this, R.layout.list_view_item_row, drawerObjectDefiners);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new DrawerItemClickListener());
        drawerLayout = (DrawerLayout) findViewById(R.id.mainDrawer);
        drawerLayout.addDrawerListener(drawerToggle);
        setupDrawerToggle();


    }


    @Override
    public void onClick(View view) {
        if (view.getTag().equals(btnTag)) {
            Intent intent = new Intent(MainActivity.this, AddNewPlotMap.class);
            startActivity(intent);
        }
    }






    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            selectItem(position);

        }
    }



    private void selectItem(int position) {

        switch (position) {
            case 0:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case 1:
                Intent intent1 = new Intent(this, ImportExport.class);
                startActivity(intent1);
                break;
            case 2:
                Intent intent2 = new Intent(this, Pictures.class);
                startActivity(intent2);
                break;
            case 3:
                Intent intent3 = new Intent(this, Settings.class);
                startActivity(intent3);
                break;
            case 4:
                Intent intent4 = new Intent(this, Help.class);
                startActivity(intent4);
                break;

            default:
                break;
        }
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    void setupToolbar(){
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    void setupDrawerToggle() {
        drawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerToggle.syncState();

    }

}
