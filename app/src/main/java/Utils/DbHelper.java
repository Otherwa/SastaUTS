package Utils;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Models.Train;

public class DbHelper {

    private DatabaseReference userReference;
    private Context context;

    public DbHelper(Context context) {
        this.context = context;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Get the current user's email
            String userEmail = currentUser.getEmail();
            // Initialize the database reference to point to the user's node
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://sastauts-default-rtdb.asia-southeast1.firebasedatabase.app/");
            assert userEmail != null;
            userReference = firebaseDatabase.getReference("users").child(encodeUserEmail(userEmail));
        } else {
            // Handle the case where currentUser is null (user not logged in)
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private String encodeUserEmail(String email) {
        // Firebase does not allow certain characters in keys, so encode the email
        return email.replace(".", ",");
    }

    public void addTrain(Train train) {
        if (userReference != null) {
            // Push the train object to the "trainSet" node under the user's node
            userReference.child("trainSet").push().setValue(train, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    if (error != null) {
                        Toast.makeText(context, "Failed to add train: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Train added to Firebase", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public List<Train> getAllTrains() {
        final List<Train> trainList = new ArrayList<>();
        if (userReference != null) {
            userReference.child("trainSet").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Train train = snapshot.getValue(Train.class);
                        if (train != null) {
                            trainList.add(train);
                        }
                    }
                    // Notify any observers of the data change
                    // (You may want to implement a callback mechanism here)
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(context, "Failed to retrieve trains: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        return trainList;
    }
}
