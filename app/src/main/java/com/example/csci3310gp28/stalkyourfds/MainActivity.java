package com.example.csci3310gp28.stalkyourfds;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;
import java.util.Vector;

public class MainActivity extends FragmentActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.stalk, menu);
        // Set menu item to have white color
        setMenuItemColor(menu, R.id.menu_add, Color.WHITE);
        setMenuItemColor(menu, R.id.menu_logout, Color.WHITE);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_add:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Set color for the specified menu item.
     * @param menu is the menu the item is located at
     * @param res is the resource ID of the menu item
     * @param color is the color of the item
     */
    private void setMenuItemColor(Menu menu, int res, int color) {
        Drawable icon = menu.findItem(res).getIcon();
        if(icon != null) {
            icon.mutate();
            icon.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
    }
}
