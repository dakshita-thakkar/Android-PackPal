package com.example.packpal.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.packpal.Constants.Constants;
import com.example.packpal.Database.RoomDB;
import com.example.packpal.Models.Items;
import com.example.packpal.R;

import java.util.List;

public class CheckListAdapter extends RecyclerView.Adapter<CheckListAdapter.CheckListViewHolder> {

    private Context context;
    private List<Items> itemsList;
    private RoomDB database;
    private String show;

    public CheckListAdapter(Context context, List<Items> itemsList, RoomDB database, String show) {
        this.context = context;
        this.itemsList = itemsList;
        this.database = database;
        this.show = show;
    }

    @NonNull
    @Override
    public CheckListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.check_list_item, parent, false);
        return new CheckListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckListViewHolder holder, int position) {
        Items currentItem = itemsList.get(position);
        holder.checkBox.setText(currentItem.getItemname());
        holder.checkBox.setChecked(currentItem.getChecked());

        if (Constants.FALSE_STRING.equals(show)) {
            holder.btnDelete.setVisibility(View.GONE);
            holder.layout.setBackgroundResource(R.drawable.border_one);
        } else {
            if (currentItem.getChecked()) {
                holder.layout.setBackgroundColor(Color.parseColor("#8e546f"));
            } else {
                holder.layout.setBackgroundResource(R.drawable.border_one);
            }
        }

        holder.checkBox.setOnClickListener(v -> {
            boolean isChecked = holder.checkBox.isChecked();
            database.mainDao().checkUncheck(currentItem.getID(), isChecked);

            if (Constants.FALSE_STRING.equals(show)) {
                itemsList = database.mainDao().getAllSelected(true);
                notifyDataSetChanged();
            } else {
                currentItem.setChecked(isChecked);
                notifyDataSetChanged();
                String toastMessage = isChecked ? "(Packed)" : "(Un-Packed)";
                Toast.makeText(context, holder.checkBox.getText() + " " + toastMessage, Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnDelete.setOnClickListener(v -> new AlertDialog.Builder(context)
                .setTitle("Delete (" + currentItem.getItemname() + ")")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    database.mainDao().delete(currentItem);
                    itemsList.remove(currentItem);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", (dialog, which) -> Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show())
                .setIcon(R.drawable.baseline_delete_forever_24)
                .show());
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public static class CheckListViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;
        CheckBox checkBox;
        Button btnDelete;

        public CheckListViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.linearLayout);
            checkBox = itemView.findViewById(R.id.checkbox);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
