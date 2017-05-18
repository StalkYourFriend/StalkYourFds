package com.example.csci3310gp28.stalkyourfds;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    final int[] ICONS = new int[] {
            R.drawable.ic_people_black_24dp,
            R.drawable.ic_chat_black_24dp,
            R.drawable.ic_person_black_24dp
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Fragment> fragments = new Vector<>();
        fragments.add(Fragment.instantiate(this, FdListFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, ChatroomFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, AccountFragment.class.getName()));

        CustomPagerAdapter pageAdapter = new CustomPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(pageAdapter);

        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        for (int i=0; i<tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            Drawable icon = getResources().getDrawable(ICONS[i],getTheme());
            if (icon != null) {
                icon.mutate();
                icon.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.primary_light), PorterDuff.Mode.SRC_ATOP);
            }
        }

//        viewPager = (ViewPager) findViewById(R.id.viewPager);
//        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
//
//        SampleFragmentPagerAdapter pagerAdapter =
//                new SampleFragmentPagerAdapter(getSupportFragmentManager(), this);
//        viewPager.setAdapter(pagerAdapter);
//
//        tabLayout.setupWithViewPager(viewPager);
//
//        for (int i = 0; i < tabLayout.getTabCount(); i++) {
//            TabLayout.Tab tab = tabLayout.getTabAt(i);
//            if (tab != null) {
//                tab.setCustomView(pagerAdapter.getTabView(i));
//            }
//        }
//
//        viewPager.setCurrentItem(1);
    }
}
