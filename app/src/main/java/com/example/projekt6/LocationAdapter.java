package com.example.projekt6;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {
    private List<DBLocation> locations;
    private Context context;

    public LocationAdapter(List<DBLocation> locations, Context context) {
        this.locations = locations;
        this.context = context;
    }


    @NonNull
    @Override
    public LocationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View locationView = inflater.inflate(R.layout.location_item_layout, parent, false);
        return new ViewHolder(locationView);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationAdapter.ViewHolder holder, int position) {
        DBLocation location = locations.get(position);
        TextView txtName = holder.getTxtName();
        Button btnShowLocation = holder.getBtnShowLocation();

        txtName.setText(location.getName());
        btnShowLocation.setOnClickListener(view -> {
            Intent intent = new Intent(context, LocationInfoActivity.class);
            intent.putExtra("name", txtName.getText());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtName;
        private final Button btnShowLocation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtLocationName);
            btnShowLocation = itemView.findViewById(R.id.btnShowLocation);
        }

        public TextView getTxtName() {
            return txtName;
        }

        public Button getBtnShowLocation() {
            return btnShowLocation;
        }
    }
}
