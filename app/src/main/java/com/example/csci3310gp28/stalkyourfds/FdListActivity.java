package com.example.csci3310gp28.stalkyourfds;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class FdListActivity extends AppCompatActivity {

    private static final String TAG = FdListActivity.class.getSimpleName();
    private FdListAdapter mFdListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_fd_list);

        // Set up friends ListView
        ListView fdsListView = (ListView) findViewById(R.id.friend_listview_activity);

        // TEMP dummy friend list
        ArrayList<Friend> fds = new ArrayList<>();
        fds.add(new Friend("Aaron", "", "SHB 123"));
        fds.add(new Friend("Energy", null, "SHB 123"));
        fds.add(new Friend("Kalok", null, "SHB 123"));
        fds.add(new Friend("No", null, "SHB 123"));
        fds.add(new Friend("Joker", null, "SHB 924"));
        fds.add(new Friend("Sam", null, "SHB 924"));
        fds.add(new Friend("DR S.H.OR", "http://appsrv.cse.cuhk.edu.hk/~shor/index_files/image003.jpg", "SHB 127"));

        // Set up FdListAdapter on the friends ListView
        mFdListAdapter = new FdListAdapter(this, fds);
        fdsListView.setAdapter(mFdListAdapter);
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
            case R.id.menu_update:
                return true;
            case R.id.menu_logout:
                // TODO implement logout function
                // Below are just temporary codes
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                finish();
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
