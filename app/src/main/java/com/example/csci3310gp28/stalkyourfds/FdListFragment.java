package com.example.csci3310gp28.stalkyourfds;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FdListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FdListFragment extends Fragment {

    private static final String TAG = FdListFragment.class.getSimpleName();
    private FdListAdapter mFdListAdapter;
    private ListView mFdListView;

    ArrayList<Friend> fds;

    /*
    // Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    */


    public FdListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FdListFragment.
     */
    /*
    // Rename and change types and number of parameters
    public static FdListFragment newInstance(String param1, String param2) {
        FdListFragment fragment = new FdListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    */

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        /*
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        */

        // TEMP dummy friend list
        fds = new ArrayList<>();
        fds.add(new Friend("Aaron", "", "SHB 123"));
        fds.add(new Friend("Energy", null, "SHB 123"));
        fds.add(new Friend("Kalok", null, "SHB 123"));
        fds.add(new Friend("No", null, "SHB 123"));
        fds.add(new Friend("Joker", null, "SHB 924"));
        fds.add(new Friend("Sam", null, "SHB 924"));
        fds.add(new Friend("DR S.H.OR", "http://appsrv.cse.cuhk.edu.hk/~shor/index_files/image003.jpg", "SHB 127"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_fd_list, container, false);

        // Set up FdListAdapter on the friends ListView
        mFdListView = (ListView) rootView.findViewById(R.id.friend_listview);
        if(mFdListView != null) {
            mFdListAdapter = new FdListAdapter(getActivity(), fds);
            mFdListView.setAdapter(mFdListAdapter);
        } else {
            Log.e(TAG, "Error: FdListView not found");
        }

        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        // Set menu item to have white color
        setMenuItemColor(menu, R.id.menu_add, Color.WHITE);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_add:
                Toast.makeText(getActivity(), "Add friend", Toast.LENGTH_SHORT).show();
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
