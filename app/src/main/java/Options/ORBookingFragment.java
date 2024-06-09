package Options;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.sastauts.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;


import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import Models.Train;
import Pages.TrainsAdapter;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ORBookingFragment extends Fragment {

    private static final String BASE_URL = "https://trains.p.rapidapi.com/";
    private RecyclerView recyclerView;
    private SearchView searchView;
    private TrainsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_o_r_booking, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchView = rootView.findViewById(R.id.searchView);


        // Setup search functionality
        setupSearchView();
        // Fetch data from API
        fetchDataFromApi("Rajdhani");
        return rootView;
    }

    private class FetchDataFromApiTask extends AsyncTask<String, Void, List<Train>> {

        @Override
        protected List<Train> doInBackground(String... queries) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {
                // Construct URL
                URL url = new URL(BASE_URL);

                // Create JSON request body
                JSONObject jsonRequest = new JSONObject();
                jsonRequest.put("search", queries[0]);
                String requestBody = jsonRequest.toString();

                // Create HttpURLConnection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("x-rapidapi-key", "f54bde0a4bmshd39e1d1110c4cd5p17ab10jsn0b920baf5821");
                urlConnection.setRequestProperty("x-rapidapi-host", "trains.p.rapidapi.com");
                urlConnection.setDoOutput(true);

                // Write the request body
                try (OutputStream os = urlConnection.getOutputStream()) {
                    byte[] input = requestBody.getBytes("UTF-8");
                    os.write(input, 0, input.length);
                }

                // Connect to the server
                urlConnection.connect();

                // Read the input stream into a String
                StringBuilder response = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                // Parse response
                return parseResponse(response.toString());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return null;
            } finally {
                // Close the HttpURLConnection and BufferedReader
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(List<Train> result) {
            if (result != null && !result.isEmpty()) {
                Log.d("FetchDataFromApiTask", "Data fetched successfully");
                // Store the original data
                if (adapter == null) {
                    adapter = new TrainsAdapter(result);
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter.updateData(result);
                }
            } else {
                Log.e("FetchDataFromApiTask", "Failed to fetch data");
                Toast.makeText(getContext(), "Failed to fetch data from API", Toast.LENGTH_SHORT).show();
            }
        }

        private List<Train> parseResponse(String response) {
            List<Train> trainList = new ArrayList<>();
            try {
                JSONArray dataArray = new JSONArray(response);
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject jsonObject = dataArray.getJSONObject(i);
                    Train train = new Train();
                    train.setTrainNumber(jsonObject.getString("train_num"));
                    train.setTrainName(jsonObject.getString("name"));
                    train.setTrainFrom(jsonObject.getString("train_from"));
                    train.setTrainTo(jsonObject.getString("train_to"));

                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    train.setArriveTime(dataObject.getString("arriveTime"));
                    train.setDepartTime(dataObject.getString("departTime"));

                    JSONArray classesArray = dataObject.getJSONArray("classes");
                    List<String> classes = new ArrayList<>();
                    for (int j = 0; j < classesArray.length(); j++) {
                        classes.add(classesArray.getString(j));
                    }
                    train.setClasses(classes);

                    JSONObject daysObject = dataObject.getJSONObject("days");
                    Map<String, Integer> days = new HashMap<>();
                    Iterator<String> keys = daysObject.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        days.put(key, daysObject.getInt(key));
                    }
                    train.setDays(days);

                    trainList.add(train);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return trainList;
        }
    }


    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("SearchView", "Query submitted: " + query);
                fetchDataFromApi(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("SearchView", "Query text changed: " + newText);
                return false;
            }
        });
    }

    private void fetchDataFromApi(String query) {
        // Fetch data from API with the provided query
        new FetchDataFromApiTask().execute(query);
    }

}