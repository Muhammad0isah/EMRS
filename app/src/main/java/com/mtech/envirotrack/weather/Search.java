package com.mtech.envirotrack.weather;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.mtech.envirotrack.MainActivity;
import com.mtech.envirotrack.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Search extends Fragment implements SearchAdapter.OnItemClickListener {

    private static final String API_KEY = "YOUR_API_KEY";

    private RecyclerView searchRV;
    private SearchAdapter searchAdapter;

    private Toolbar backToolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        ((MainActivity) getActivity()).findViewById(R.id.toolbar).setVisibility(View.GONE);






        searchRV = view.findViewById(R.id.searchRV);
        searchAdapter = new SearchAdapter(new ArrayList<>(), this);
        searchRV.setAdapter(searchAdapter);

        backToolbar = view.findViewById(R.id.searchToolbar);

        MaterialCardView searchCardView = view.findViewById(R.id.search);
        EditText searchEditText = view.findViewById(R.id.searchEditText);

        searchCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = searchEditText.getText().toString();
                new GetLocationDataTask().execute(searchQuery);
            }
        });

        backToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

        return view;
    }

    private class GetLocationDataTask extends AsyncTask<String, Void, List<String>> {

        @Override
        protected List<String> doInBackground(String... params) {
            String query = params[0];
            String requestUrl = "https://api.opencagedata.com/geocode/v1/json?q=" + query + "&key=" + API_KEY;

            try {
                URL url = new URL(requestUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }

                in.close();
                connection.disconnect();

                // Parse the JSON response
                JSONObject jsonObject = new JSONObject(content.toString());
                JSONArray results = jsonObject.getJSONArray("results");

                List<String> cityNames = new ArrayList<>();
                for (int i = 0; i < results.length(); i++) {
                    JSONObject result = results.getJSONObject(i);
                    String cityName = result.getString("formatted");
                    cityNames.add(cityName);
                }

                return cityNames;
            } catch (Exception e) {
                Log.e("SearchFragment", "Error getting location data", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<String> cityNames) {
            if (cityNames != null) {
                // Update the RecyclerView adapter with the city names
                searchAdapter.updateData(cityNames);
            }
        }
    }

    @Override
    public void onItemClick(String item) {
        // Create a new bundle to store the selected city name
        Bundle bundle = new Bundle();
        bundle.putString("cityName", item);

        // Create a new Home and set the arguments
        Home home = new Home();
        home.setArguments(bundle);

        // Navigate to the Home
        getParentFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, home)
                .addToBackStack(null)
                .commit();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Show MainActivity's toolbar
        ((MainActivity) getActivity()).findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
    }
}