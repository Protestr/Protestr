package org.protestr.app.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vanniktech.emoji.EmojiTextView;

import org.protestr.app.R;
import org.protestr.app.data.models.dao.EventUpdate;
import org.protestr.app.utils.FormatUtils;
import org.protestr.app.utils.TimeUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class LiveUpdateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<EventUpdate> eventUpdates;

    public LiveUpdateAdapter(ArrayList<EventUpdate> eventUpdates){
        this.eventUpdates = eventUpdates;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventUpdateViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_update_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final EventUpdate eventUpdate = eventUpdates.get(position);
        Glide.with(((EventUpdateViewHolder) holder).profileImage)
                .load(eventUpdate.getProfilePicUrl())
                .into(((EventUpdateViewHolder) holder).profileImage);
        ((EventUpdateViewHolder) holder).username.setText(eventUpdate.getUsername());
        ((EventUpdateViewHolder) holder).eventUpdate.setText(eventUpdate.getMessage());
        ((EventUpdateViewHolder) holder).timestamp.setText(FormatUtils.formatEventUpdate(eventUpdate.getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return eventUpdates == null ? 0 : eventUpdates.size();
    }

    public void addItem(EventUpdate eventUpdate) {
        eventUpdates.add(eventUpdate);
        notifyItemInserted(eventUpdates.size() - 1);
    }

    static class EventUpdateViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.profile_pic_image)
        CircleImageView profileImage;
        @BindView(R.id.username)
        TextView username;
        @BindView(R.id.event_update)
        EmojiTextView eventUpdate;
        @BindView(R.id.timestamp)
        TextView timestamp;

        EventUpdateViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
