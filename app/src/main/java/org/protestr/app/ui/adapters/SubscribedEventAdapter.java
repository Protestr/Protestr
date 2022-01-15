package org.protestr.app.ui.adapters;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import org.protestr.app.R;
import org.protestr.app.data.models.dao.Event;
import org.protestr.app.utils.FormatUtils;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * Created by someone on 19/04/18.
 */

public class SubscribedEventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Event> events;

    private OnSubscribedEventClickedListener onSubscribedEventClickedListener;

    public interface OnSubscribedEventClickedListener {
        void onEventClicked(Event event);
    }

    public SubscribedEventAdapter(OnSubscribedEventClickedListener onSubscribedEventClickedListener) {
        this.onSubscribedEventClickedListener = onSubscribedEventClickedListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SubscribedEventViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.subscribed_event_cardview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Event event = events.get(position);
        Glide.with(((SubscribedEventViewHolder) holder).imgEventImage)
                .load(event.getImageUrl())
                .into(((SubscribedEventViewHolder) holder).imgEventImage);
        ((SubscribedEventViewHolder) holder).tvEventTitle.setText(event.getTitle());
        ((SubscribedEventViewHolder) holder).tvEventDate.setText(FormatUtils.formatDateByDefaultLocale(event.getFromDate()) + " - " +
                FormatUtils.formatHours(event.getFromDate()));

    }

    @Override
    public int getItemCount() {
        return events != null ? events.size() : 0;
    }

    public void addItems(ArrayList<Event> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        events.remove(position);
        notifyItemRemoved(position);
    }

    public void removeItem(Event event) {
        removeItem(events.indexOf(event));
    }

    public void updateItem(Event event) {
        final int pos = events.indexOf(event);
        events.set(pos, event);
        notifyItemChanged(pos);
    }

    public void clearAll() {
        events.clear();
        notifyDataSetChanged();
    }

    class SubscribedEventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.subscribed_event_image)
        ImageView imgEventImage;
        @BindView(R.id.subscribed_event_date)
        TextView tvEventDate;
        @BindView(R.id.subscribed_event_title)
        TextView tvEventTitle;

        SubscribedEventViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onSubscribedEventClickedListener.onEventClicked(events.get(getAdapterPosition()));
        }
    }

}
