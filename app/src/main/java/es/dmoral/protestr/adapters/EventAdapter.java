package es.dmoral.protestr.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.protestr.R;
import es.dmoral.protestr.api.models.Event;

/**
 * Created by grender on 15/02/17.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private final ArrayList<Event> events;
    private Context context;
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyy");

    public EventAdapter(ArrayList<Event> events) {
        this.events = events;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        return new EventAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.event_cardview, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Event event = events.get(position);
        Glide.with(context)
                .load(event.getImage_url())
                .into(holder.imgEventImage);
        holder.tvEventTitle.setText(event.getTitle());
        holder.tvEventFrom.setText(simpleDateFormat.format(event.getFrom()));
        holder.tvEventRating.setText(String.valueOf(event.getRating()));
        holder.tvEventLocation.setText(event.getLocation_name());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.event_image) ImageView imgEventImage;
        @BindView(R.id.event_from) TextView tvEventFrom;
        @BindView(R.id.event_title) TextView tvEventTitle;
        @BindView(R.id.event_rating) TextView tvEventRating;
        @BindView(R.id.event_location) TextView tvEventLocation;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
