package com.example.csci3310gp28.stalkyourfds.presence;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.csci3310gp28.stalkyourfds.R;

public class PresenceTabContentFragment extends Fragment {
    private PresenceListAdapter prAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chatroom, container, false);
        ListView listView = (ListView) view.findViewById(R.id.message_list);
        listView.setAdapter(prAdapter);

        return view;
    }

    public void setAdapter(PresenceListAdapter prAdapter) {
        this.prAdapter = prAdapter;
    }
}
