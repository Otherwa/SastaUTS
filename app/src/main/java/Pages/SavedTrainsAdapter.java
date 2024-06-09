package Pages;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sastauts.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import Models.Train;
import Utils.DbHelper;

public class SavedTrainsAdapter extends RecyclerView.Adapter<SavedTrainsAdapter.ViewHolder> {
    private List<Train> dataList;

    public SavedTrainsAdapter(List<Train> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.train_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Train train = dataList.get(position);
        holder.bind(train);
    }

    public List<Train> getDataList() {
        return dataList;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void updateData(List<Train> newData) {
        dataList = newData;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView trainNameTextView;
        TextView trainNumberTextView;
        TextView fromTextView;
        TextView toTextView;
        TextView arriveTimeTextView;
        TextView departTimeTextView;
        Button editButton, deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            trainNameTextView = itemView.findViewById(R.id.trainNameTextView);
            trainNumberTextView = itemView.findViewById(R.id.trainNumberTextView);
            fromTextView = itemView.findViewById(R.id.trainFromTextView);
            toTextView = itemView.findViewById(R.id.trainToTextView);
            arriveTimeTextView = itemView.findViewById(R.id.arriveTimeTextView);
            departTimeTextView = itemView.findViewById(R.id.departTimeTextView);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }

        public void bind(Train train) {
            trainNameTextView.setText(train.getTrainName());
            trainNumberTextView.setText(String.valueOf(train.getTrainNumber()));
            fromTextView.setText(train.getTrainFrom());
            toTextView.setText(train.getTrainTo());
            arriveTimeTextView.setText(train.getArriveTime());
            departTimeTextView.setText(train.getDepartTime());

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the selected train
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Train selectedTrain = dataList.get(position);

                        // Inflate the dialog with the custom layout
                        LayoutInflater inflater = LayoutInflater.from(itemView.getContext());
                        View dialogView = inflater.inflate(R.layout.dialog_edit_train, null);

                        // Pre-fill the fields with the train's current data
                        EditText editTrainName = dialogView.findViewById(R.id.editTrainName);
                        EditText editTrainNumber = dialogView.findViewById(R.id.editTrainNumber);
                        EditText editTrainFrom = dialogView.findViewById(R.id.editTrainFrom);
                        EditText editTrainTo = dialogView.findViewById(R.id.editTrainTo);
                        EditText editArriveTime = dialogView.findViewById(R.id.editArriveTime);
                        EditText editDepartTime = dialogView.findViewById(R.id.editDepartTime);

                        editTrainName.setText(selectedTrain.getTrainName());
                        editTrainNumber.setText(String.valueOf(selectedTrain.getTrainNumber()));
                        editTrainFrom.setText(selectedTrain.getTrainFrom());
                        editTrainTo.setText(selectedTrain.getTrainTo());
                        editArriveTime.setText(selectedTrain.getArriveTime());
                        editDepartTime.setText(selectedTrain.getDepartTime());

                        // Show the dialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                        builder.setView(dialogView);
                        builder.setTitle("Edit Train Details");
                        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Update the train details
                                selectedTrain.setTrainName(editTrainName.getText().toString());
                                selectedTrain.setTrainNumber(String.valueOf(Integer.parseInt(editTrainNumber.getText().toString())));
                                selectedTrain.setTrainFrom(editTrainFrom.getText().toString());
                                selectedTrain.setTrainTo(editTrainTo.getText().toString());
                                selectedTrain.setArriveTime(editArriveTime.getText().toString());
                                selectedTrain.setDepartTime(editDepartTime.getText().toString());

                                // Update the train in Firebase
                                DatabaseReference userReference = FirebaseDatabase.getInstance().getReference();
                                DbHelper dbHelper = new DbHelper(userReference, itemView.getContext());
                                dbHelper.updateTrain(selectedTrain, selectedTrain.getTrainNumber());

                                // Notify the adapter of the changes
                                notifyItemChanged(position);
                                Toast.makeText(itemView.getContext(), "Train updated: " + selectedTrain.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        builder.setNegativeButton("Cancel", null);
                        builder.show();
                    }
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Show a dialog box to confirm deletion
                    AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                    builder.setTitle("Confirm Deletion");
                    builder.setMessage("Are you sure you want to delete this train?");
                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Delete the train
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                Train selectedTrain = dataList.get(position);
                                // Pass the selected train to the deleteFromDatabase method in the fragment
                                DatabaseReference userReference = FirebaseDatabase.getInstance().getReference();
                                DbHelper dbHelper = new DbHelper(userReference, itemView.getContext());
                                dbHelper.deleteTrain(selectedTrain, new DbHelper.OnDeleteListener() {
                                    @Override
                                    public void onDeleteSuccess() {
                                        // Remove item from dataList and notify adapter
                                        dataList.remove(position);
                                        notifyItemRemoved(position);
                                        Toast.makeText(itemView.getContext(), "Train deleted from database: " + selectedTrain.toString(), Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onDeleteFailure(Exception e) {
                                        Toast.makeText(itemView.getContext(), "Failed to delete train: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", null);
                    builder.show();
                }
            });
        }

    }
}

