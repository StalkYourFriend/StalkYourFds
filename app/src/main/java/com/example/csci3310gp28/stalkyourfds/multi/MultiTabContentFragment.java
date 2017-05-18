package com.example.csci3310gp28.stalkyourfds.multi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.csci3310gp28.stalkyourfds.R;

public class MultiTabContentFragment extends Fragment {
    private MultiListAdapter mlAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chatroom, container, false);
        ListView listView = (ListView) view.findViewById(R.id.message_list);
        listView.setAdapter(mlAdapter);

        return view;
    }

    public void setAdapter(MultiListAdapter mlAdapter) {
        this.mlAdapter = mlAdapter;
    }
}
