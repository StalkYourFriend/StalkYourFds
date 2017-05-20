package com.example.csci3310gp28.stalkyourfds;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {

    private static final String TAG = AccountFragment.class.getSimpleName();

    private String username;
    private TextView usernameTV;
    private TextView iconTV;
    private ImageView mImageView;

    private Button logoutBtn;
    private Button submitBtn;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);

        // Actions within the fragment
        logoutBtn = (Button) rootView.findViewById(R.id.ac_logout_btn);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });


        submitBtn = (Button) rootView.findViewById(R.id.ac_submit_btn);
        submitBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                long id = sharedPref.getLong("id", 1);
                String url = "http://104.198.103.187:3000/users/" + id;
                JSONObject request = new JSONObject();
                try {
                    request.put("data",
                            new JSONObject()
                                    .put("attributes",
                                            new JSONObject()
                                                    .put("icon", iconTV.getText().toString())

                                    )
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsObjRequest = new JsonObjectRequest
                        (Request.Method.PATCH, url, request, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(this.getClass().getSimpleName(), "Response: " + response.toString());
                                //display successful msg
                                Context context = getActivity().getApplicationContext();
                                CharSequence text = "Successfully updated your icon";
                                int duration = Toast.LENGTH_SHORT;
                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                                SharedPreferences settings = getActivity().getSharedPreferences("user", MODE_PRIVATE);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putString("icon", iconTV.getText().toString());
                                editor.apply();
                                drawIcon(mImageView,iconTV.getText().toString());


                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //display failed msg
                                Context context = getActivity().getApplicationContext();
                                CharSequence text = "Failed to update icon: " + new String(error.networkResponse.data) + "(" + error.networkResponse.statusCode + ")";
                                int duration = Toast.LENGTH_SHORT;
                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();



                            }
                        });
                // Access the RequestQueue through your singleton class.
                MySingleton.getInstance(getActivity()).addToRequestQueue(jsObjRequest);
            }
        });


        username = getActivity().getIntent().getExtras().getString("username");
        usernameTV = (TextView) rootView.findViewById(R.id.ac_username_tv);
        usernameTV.setText(username);

        mImageView= (ImageView) rootView.findViewById(R.id.ac_icon_iv);
        SharedPreferences settings = getActivity().getSharedPreferences("user", MODE_PRIVATE);
        String icon=settings.getString("icon",null);
        drawIcon(mImageView, icon);


        iconTV=(TextView) rootView.findViewById(R.id.ac_icon_et) ;
        iconTV.setText(icon);

        return rootView;
    }

    /**
     * Puts icon for the specific friend in the friend list, given the icon URL.
     * If the URL is not null but invalid, it will put an error icon to the ImageView.
     * @param iv the target ImageView the icon is located at
     * @param url of the icon
     */
    private void drawIcon(final ImageView iv, String url) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        Log.v(TAG, "URL: " + url);
        ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                iv.setImageBitmap(response);
            }
        }, 0, 0, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                iv.setImageResource(R.drawable.ic_error_black_24dp);
                iv.setColorFilter(Color.RED);
            }
        });
        queue.add(request);
    }

}
