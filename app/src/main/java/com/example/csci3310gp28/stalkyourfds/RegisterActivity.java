package com.example.csci3310gp28.stalkyourfds;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void onRegisterBtnClick(View view) {
        final String username = ((TextView) findViewById(R.id.register_username_et)).getText().toString();
        String password = ((TextView) findViewById(R.id.register_password_et)).getText().toString();
        String icon = ((TextView) findViewById(R.id.register_icon_et)).getText().toString();
        String url = "http://5d8ba069.ngrok.io/users";
        JSONObject request = new JSONObject();
        try {
            request.put("data",
                    new JSONObject()
                            .put("attributes",
                                    new JSONObject()
                                            .put("full_name", username)
                                            .put("password", password)
                                            .put("icon", icon)
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
                        Context context = getApplicationContext();
                        CharSequence text = "Successfully registered!";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        // We need an Editor object to make preference changes.
                        // All objects are from android.context.Context
                        SharedPreferences settings = getSharedPreferences("user",MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("username", username);

                        // Commit the edits!
                        editor.apply();
                        Intent stalkIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(stalkIntent);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //display failed msg
                        Context context = getApplicationContext();
                        CharSequence text = "Failed to registered: " + new String(error.networkResponse.data) + "(" + error.networkResponse.statusCode + ")";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                    }
                });
        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);

    }
}
