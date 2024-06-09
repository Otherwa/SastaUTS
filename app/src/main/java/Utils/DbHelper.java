package Utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

    public DbHelper(DatabaseReference userReference, Context context) {
        this.context = context;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Get the current user's email
            String userEmail = currentUser.getEmail();
            // Initialize the database reference to point to the user's node
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://sastauts-default-rtdb.asia-southeast1.firebasedatabase.app/");
            assert userEmail != null;
            this.userReference = firebaseDatabase.getReference("users").child(encodeUserEmail(userEmail));
        } else {
            // Handle the case where currentUser is null (user not logged in)
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private String encodeUserEmail(String email) {
        // Firebase does not allow certain characters in keys, so encode the email
        return email.replace(".", ",");
    }

    public void addTrain(Train train, String trainId) {
        if (userReference != null) {
            // Push the train object to the "trainSet" node under the user's node
            userReference.child("trainSet").child(trainId).setValue(train, new DatabaseReference.CompletionListener() {
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

    public void getAllTrains(final DbHelperCallback callback) {
        if (userReference != null) {
            userReference.child("trainSet").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Train> trainList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Train train = snapshot.getValue(Train.class);
                        if (train != null) {
                            trainList.add(train);
                        }
                    }
                    callback.onSuccess(trainList);
                    Log.d("TrainsGET", trainList.toString());
                    Toast.makeText(context, "Successfully retrieved trains", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    callback.onFailure(error.getMessage());
                    Toast.makeText(context, "Failed to retrieve trains: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            callback.onFailure("User reference is null");
            Toast.makeText(context, "User reference is null", Toast.LENGTH_SHORT).show();
        }
    }

    public interface DbHelperCallback {
        void onSuccess(List<Train> trainList);
        void onFailure(String error);
    }


    public void deleteTrain(Train train, final OnDeleteListener listener) {
        // Retrieve the train ID
        String trainId = String.valueOf(train.getTrainNumber());

        // Remove the train node using the train ID
        DatabaseReference trainRef = userReference.child("trainSet").child(trainId);
        trainRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("DbHelper", "Train deleted successfully: " + train.toString());
                if (listener != null) {
                    listener.onDeleteSuccess();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("DbHelper", "Failed to delete train: " + e.getMessage());
                if (listener != null) {
                    listener.onDeleteFailure(e);
                }
            }
        });
    }

    public interface OnDeleteListener {
        void onDeleteSuccess();
        void onDeleteFailure(Exception e);
    }

    public void updateTrain(Train train, String trainNumber) {
        if (userReference != null) {
            userReference.child("trainSet").child(trainNumber).setValue(train, (error, ref) -> {
                if (error != null) {
                    Toast.makeText(context, "Failed to update train: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Train updated in Firebase", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
