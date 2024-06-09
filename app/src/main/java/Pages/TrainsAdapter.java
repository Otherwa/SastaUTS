package Pages;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class TrainsAdapter extends RecyclerView.Adapter<TrainsAdapter.ViewHolder> {
    private List<Train> dataList;

    public TrainsAdapter(List<Train> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_layout, parent, false);
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
        Button addToDatabaseBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            trainNameTextView = itemView.findViewById(R.id.trainNameTextView);
            trainNumberTextView = itemView.findViewById(R.id.trainNumberTextView);
            fromTextView = itemView.findViewById(R.id.fromTextView);
            toTextView = itemView.findViewById(R.id.toTextView);
            arriveTimeTextView = itemView.findViewById(R.id.arriveTimeTextView);
            departTimeTextView = itemView.findViewById(R.id.departTimeTextView);
            addToDatabaseBtn = itemView.findViewById(R.id.addToDatabaseBtn);
        }

        public void bind(Train train) {
            trainNameTextView.setText(train.getTrainName());
            trainNumberTextView.setText(String.valueOf(train.getTrainNumber()));
            fromTextView.setText(train.getTrainFrom());
            toTextView.setText(train.getTrainTo());
            arriveTimeTextView.setText(train.getArriveTime());
            departTimeTextView.setText(train.getDepartTime());

            // Button click listener

            addToDatabaseBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the selected train
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Train selectedTrain = dataList.get(position);
                        // Pass the selected train to the addToDatabase method in the fragment
                        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference();
                        DbHelper dbHelper = new DbHelper(userReference, itemView.getContext());
                        dbHelper.addTrain(selectedTrain,selectedTrain.getTrainNumber());
                        Toast.makeText(itemView.getContext(), selectedTrain.toString(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(itemView.getContext(), "Train added to database", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
