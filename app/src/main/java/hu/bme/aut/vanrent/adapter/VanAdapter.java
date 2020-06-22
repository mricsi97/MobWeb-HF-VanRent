package hu.bme.aut.vanrent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hu.bme.aut.vanrent.R;
import hu.bme.aut.vanrent.data.VanItem;

public class VanAdapter
        extends RecyclerView.Adapter<VanAdapter.VanViewHolder> {

    private Context context;
    private final List<VanItem> items;
    private VanItemClickListener listener;

    public VanAdapter(Context context, VanItemClickListener listener) {
        this.listener = listener;
        this.context = context;
        items = new ArrayList<>();
    }

    public void addItem(VanItem item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    public void update(List<VanItem> vanItems) {
        items.clear();
        items.addAll(vanItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_van_list, parent, false);
        return new VanViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VanViewHolder holder, int position) {
        VanItem item = items.get(position);
        holder.nameTextView.setText(item.name);
        holder.descriptionTextView.setText(item.description);
        holder.categoryTextView.setText(item.size.name());
        holder.priceTextView.setText(context.getString(R.string.currency_per_hour, item.price));
        holder.iconImageView.setImageResource(getImageResource(item.name));

        holder.item = item;
    }

    private @DrawableRes
    int getImageResource(String name) {
        @DrawableRes int ret;
        switch (name) {
            case "Fiat Ducato":
                ret = R.drawable.fiat_ducato;
                break;
            case "Ford Transit Courier":
                ret = R.drawable.ford_transit_courier;
                break;
            case "Ford Transit Custom LWB":
                ret = R.drawable.ford_transit_custom_lwb;
                break;
            case "Mercedes Benz Sprinter":
                ret = R.drawable.mercedes_benz_sprinter;
                break;
            case "Peugeot Expert":
                ret = R.drawable.peugeot_expert;
                break;
            case "Vauxhall Movan L3 H2":
                ret = R.drawable.vauxhall_movan_l3_h2;
                break;
            case "Volkswagen Caddy Maxi":
                ret = R.drawable.volkswagen_caddy_maxi;
                break;
            case "Volkswagen Transporter":
                ret = R.drawable.volkswagen_transporter;
                break;
            default:
                ret = 0;
        }
        return ret;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface VanItemClickListener{
        void onItemClicked(VanItem item);
    }

    class VanViewHolder extends RecyclerView.ViewHolder {

        ImageView iconImageView;
        TextView nameTextView;
        TextView descriptionTextView;
        TextView categoryTextView;
        TextView priceTextView;
        Button rentButton;

        VanItem item;

        VanViewHolder(View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.ivVanItemIcon);
            nameTextView = itemView.findViewById(R.id.tvVanItemName);
            descriptionTextView = itemView.findViewById(R.id.tvItemDescription);
            categoryTextView = itemView.findViewById(R.id.tvVanItemCategory);
            priceTextView = itemView.findViewById(R.id.tvVanItemPrice);
            rentButton = itemView.findViewById(R.id.btnVanItemRentButton);

            rentButton.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClicked(item);
                    }
                }
            });
        }
    }
}