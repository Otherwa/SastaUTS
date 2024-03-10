package com.example.sastauts;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import BookingOptions.ORBookingFragment;
import Pages.PageAdapter;
import BookingOptions.TicketJourneyFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabLayout = findViewById(R.id.tabs);
        ViewPager2 viewPager = findViewById(R.id.view_pager);

        FloatingActionButton helpButton = findViewById(R.id.help);

        // frag pages adapter
        PageAdapter adapter = new PageAdapter(MainActivity.this);

        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(adapter.getTabTitle(position))
        ).attach();

        helpButton.setOnClickListener(v->{
            Toast.makeText(MainActivity.this,"HELP",Toast.LENGTH_LONG).show();
        });
    }
}
