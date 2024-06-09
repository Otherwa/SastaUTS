package Options;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sastauts.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Models.Train;
import Pages.SavedTrainsAdapter;
import Utils.DbHelper;

public class SavedTrainsFragment extends Fragment {
    private SavedTrainsAdapter adapter;
    private List<Train> trainList;
    RecyclerView recyclerView;
    DbHelper dbHelper;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved_trains, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewSavedTrains);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        trainList = new ArrayList<>();
        adapter = new SavedTrainsAdapter(trainList);
        recyclerView.setAdapter(adapter);


        // Initialize DbHelper
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference();
        dbHelper = new DbHelper(userReference, getContext());

        fetchTrainsFromDb();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchTrainsFromDb();  // Fetch trains from the database when the fragment is resumed
    }

    private void fetchTrainsFromDb() {
        dbHelper.getAllTrains(new DbHelper.DbHelperCallback() {
            @Override
            public void onSuccess(List<Train> trainList) {
                if (adapter == null) {
                    adapter = new SavedTrainsAdapter(trainList);
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter.updateData(trainList);
                }
            }

            @Override
            public void onFailure(String error) {
                Log.e("ORBookingFragment", "Failed to fetch data: " + error);
                Toast.makeText(getContext(), "Failed to fetch data from database", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
