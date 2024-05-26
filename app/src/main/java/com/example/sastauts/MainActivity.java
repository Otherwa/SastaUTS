package com.example.sastauts;

import static Utils.Utils.showAlertDialog;
import static asseter.StatusBarUtils.setStatusBarColorAndIcons;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


import Pages.PageAdapter;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbarMain;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_options,menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        int About;

        int id = item.getItemId();

        About = R.id.about;

        if(id == About) {
            showAlertDialog("ABOUT",this);
        }else{
            showAlertDialog("FAQ",this);
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        toolbarMain = findViewById(R.id.toolbar);

        setSupportActionBar(toolbarMain);

        TabLayout tabLayout = findViewById(R.id.tabs);
        ViewPager2 viewPager = findViewById(R.id.view_pager);

        //set notibar color
        setStatusBarColorAndIcons(getWindow(),MainActivity.this);

        // frag pages adapter
        PageAdapter adapter = new PageAdapter(MainActivity.this);

        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(adapter.getTabTitle(position))
        ).attach();

    }

}
