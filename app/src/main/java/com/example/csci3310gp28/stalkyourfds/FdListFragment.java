package com.example.csci3310gp28.stalkyourfds;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FdListFragment} factory method to
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

        // TODO replace TEMP dummy friend list with the actual one
        fds = new ArrayList<>();
//        fds.add(new Friend("Aaron", null, "SHB 123"));
//        fds.add(new Friend("Energy", null, "SHB 123"));
//        fds.add(new Friend("Kalok", null, "SHB 123"));
//        fds.add(new Friend("No", null, "SHB 123"));
//        fds.add(new Friend("Joker", null, "SHB 924"));
//        fds.add(new Friend("Sam", null, "SHB 924"));
//        fds.add(new Friend("DR S.H.OR", "http://appsrv.cse.cuhk.edu.hk/~shor/index_files/image003.jpg", "SHB 127"));
        /*
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        */

//        final String username = ((TextView) findViewById(R.id.register_username_et)).getText().toString();
//        String password = ((TextView) findViewById(R.id.register_password_et)).getText().toString();
//        String icon = ((TextView) findViewById(R.id.register_icon_et)).getText().toString();
        updateFriendList();


    }

    private void updateFriendList() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);

        long id = sharedPref.getLong("id", 1);
        String url = "http://5d8ba069.ngrok.io/users/" + id + "/friends";
        JSONObject request = new JSONObject();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, request, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(this.getClass().getSimpleName(), "Response: " + response.toString());
                        //display successful msg
                        Context context = getActivity().getApplicationContext();
                        CharSequence text = "Successfully loaded friends!";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        fds.clear();
                        try {
                            for (int i = 0; i < response.getJSONArray("data").length(); i++) {
                                String name = response.getJSONArray("data").getJSONObject(i).getJSONObject("attributes").getString("full-name");
                                String icon = response.getJSONArray("data").getJSONObject(i).getJSONObject("attributes").getString("icon");
                                String location = response.getJSONArray("data").getJSONObject(i).getJSONObject("attributes").getString("location");
                                fds.add(new Friend(name, icon.equals("null") ? null : icon, location));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        ((FdListAdapter) mFdListView.getAdapter()).notifyDataSetChanged();
                        mFdListView.invalidateViews();
                        mFdListView.refreshDrawableState();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //display failed msg
                        Context context = getActivity().getApplicationContext();
                        CharSequence text = "Failed to load friend list: " + new String(error.networkResponse.data) + "(" + error.networkResponse.statusCode + ")";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                    }
                });
        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(getActivity()).addToRequestQueue(jsObjRequest);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_fd_list, container, false);

        // Set up FdListAdapter on the friends ListView
        mFdListView = (ListView) rootView.findViewById(R.id.friend_listview);
        if (mFdListView != null) {
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
        setMenuItemColor(menu, R.id.menu_chat, Color.WHITE);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                //Toast.makeText(getActivity(), "Add friend", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder addFdDialog = buildAddFdDialog();
                addFdDialog.show();
                return true;
            case R.id.menu_chat:
                if(Constants.location.equals("SHB924") || Constants.location.equals("SHB123")) {
                    Intent chatIntent = new Intent(getActivity(), ChatActivity.class);
                    chatIntent.putExtra("username", getActivity().getIntent().getExtras().getString("username"));
                    startActivity(chatIntent);
                }else{
                    Toast.makeText(getActivity(),"Your are outside the lab",Toast.LENGTH_SHORT).show();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Set color for the specified menu item.
     *
     * @param menu  is the menu the item is located at
     * @param res   is the resource ID of the menu item
     * @param color is the color of the item
     */
    private void setMenuItemColor(Menu menu, int res, int color) {
        Drawable icon = menu.findItem(res).getIcon();
        if (icon != null) {
            icon.mutate();
            icon.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
    }

    /**
     * Construct the dialog for adding friends.
     *
     * @return dialog with username input and buttons
     */
    private AlertDialog.Builder buildAddFdDialog() {
        // Set title and message text
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Add a friend");

        // Set up input field (with left and right margins)
        final EditText addFdEditText = new EditText(getActivity());
        addFdEditText.setHint("Friend's username");
        addFdEditText.setSingleLine();
        FrameLayout container = new FrameLayout(getActivity());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.activity_dialog_margin);
        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.activity_dialog_margin);
        params.topMargin = getResources().getDimensionPixelSize(R.dimen.activity_dialog_margin);
        addFdEditText.setLayoutParams(params);
        container.addView(addFdEditText);
        dialog.setView(container);

        // Add submit and cancel buttons
        dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO: Add friend by the given username, store the result in {@link resultText}
                // Possible results: Successfully added, already added, failed to add
                String resultText = "Adding " + addFdEditText.getText().toString();
                Toast.makeText(getActivity(), resultText, Toast.LENGTH_LONG).show();
                SharedPreferences sharedPref = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);

                long id = sharedPref.getLong("id", 1);
                String url = "http://5d8ba069.ngrok.io/users/" + id + "/addfriendbyname";
                JSONObject request = new JSONObject();
                try {
                    request.put("data",
                            new JSONObject()
                                    .put("attributes",
                                            new JSONObject()
                                                    .put("full-name", addFdEditText.getText().toString())

                                    )
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsObjRequest = new JsonObjectRequest
                        (Request.Method.POST, url, request, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(this.getClass().getSimpleName(), "Response: " + response.toString());
                                //display successful msg
                                Context context = getActivity().getApplicationContext();
                                CharSequence text = "Successfully added " + addFdEditText.getText().toString() + " as friends.";
                                int duration = Toast.LENGTH_SHORT;
                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                                updateFriendList();

                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //display failed msg
                                Context context = getActivity().getApplicationContext();
                                CharSequence text = "Failed to add friends: " + new String(error.networkResponse.data) + "(" + error.networkResponse.statusCode + ")";
                                int duration = Toast.LENGTH_SHORT;
                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                                updateFriendList();


                            }
                        });
                // Access the RequestQueue through your singleton class.
                MySingleton.getInstance(getActivity()).addToRequestQueue(jsObjRequest);
            }
        });
        dialog.setNegativeButton("Cancel", null);

        // Return the dialog
        return dialog;
    }
}
