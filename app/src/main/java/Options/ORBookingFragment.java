package Options;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sastauts.R;

import java.io.BufferedReader;
import java.io.IOException;


import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import Pages.TrainsAdapter;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ORBookingFragment extends Fragment {



    private static final String API_URL = "https://irctc1.p.rapidapi.com/api/v1/searchTrain?query=190";
    private RecyclerView recyclerView;
    private TrainsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_o_r_booking, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Fetch data from API
        new FetchDataFromApiTask().execute();

        return rootView;
    }

    private class FetchDataFromApiTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {
                // Create URL object
                URL url = new URL(API_URL);

                // Create HttpURLConnection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                // Set request headers
                urlConnection.setRequestProperty("x-rapidapi-key", "f54bde0a4bmshd39e1d1110c4cd5p17ab10jsn0b920baf5821");
                urlConnection.setRequestProperty("x-rapidapi-host", "irctc1.p.rapidapi.com");

                // Connect to the server
                urlConnection.connect();

                // Read the input stream into a String
                StringBuilder response = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                return response.toString();
            } catch (IOException e) {
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
        protected void onPostExecute(String result) {
            // Handle the API response here
            if (result != null) {
                // Process the response data
                // For example, update RecyclerView with fetched data
                List<String> data = parseResponse(result); // Dummy method, implement parsing according to your API response
                adapter = new TrainsAdapter(data);
                recyclerView.setAdapter(adapter);
            } else {
                // Handle error or display message to the user
                Toast.makeText(getContext(), "Failed to fetch data from API", Toast.LENGTH_SHORT).show();
            }
        }

        private List<String> parseResponse(String response) {
            // Implement parsing of the API response here
            // This is a dummy implementation, replace it with your actual parsing logic
            List<String> data = new ArrayList<>();
            data.add(response); // Add response as a dummy data
            return data;
        }
    }
}