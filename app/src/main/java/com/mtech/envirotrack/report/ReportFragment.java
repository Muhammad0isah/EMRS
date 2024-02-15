package com.mtech.envirotrack.report;

import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mtech.envirotrack.R;

import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportFragment extends Fragment {

    ListView listView;

    public ReportFragment() {
        // Required empty public constructor
    }

    public static ReportFragment newInstance(String param1, String param2) {
        return new ReportFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_report, container, false);
        listView = (ListView) rootView.findViewById(R.id.lv_wayPoint);
        List<Location> saved_location = ((com.mtech.envirotrack.MyApplication) requireActivity().getApplication()).getLocations();

        ArrayAdapter<Location> adapter = new ArrayAdapter<Location>(requireActivity(), android.R.layout.simple_list_item_1,saved_location);
        listView.setAdapter(adapter);
        return rootView;


    }
}