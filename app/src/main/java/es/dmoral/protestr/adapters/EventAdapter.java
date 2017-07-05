package es.dmoral.protestr.adapters;

import android.content.Context;
import android.os.Handler;
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
import es.dmoral.protestr.R;
import es.dmoral.protestr.models.models.Event;
import es.dmoral.protestr.utils.FormatUtils;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * Created by grender on 15/02/17.
 */

public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ArrayList<Event> events;
    private Context context;

    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_EVENT = 1;

    public EventAdapter(ArrayList<Event> events) {
        this.events = events;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        if (viewType == VIEW_TYPE_EVENT)
            return new EventViewHolder(LayoutInflater.from(context).inflate(R.layout.event_cardview, parent, false));
        else if (viewType == VIEW_TYPE_LOADING)
            return new LoadingViewHolder(LayoutInflater.from(context).inflate(R.layout.loading_item, parent, false));
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof EventViewHolder) {
            final Event event = events.get(position);
            Glide.with(context)
                    .load(event.getImageUrl())
                    .into(((EventViewHolder) holder).imgEventImage);
            ((EventViewHolder) holder).tvEventTitle.setText(event.getTitle());
            ((EventViewHolder) holder).tvEventFrom.setText(FormatUtils.formatDateByDefaultLocale(event.getFromDate()));
            ((EventViewHolder) holder).tvEventParticipants.setText(String.valueOf(event.getParticipants()));
            ((EventViewHolder) holder).tvEventLocation.setText(event.getLocationName());
        } else if (holder instanceof LoadingViewHolder) {
            ((LoadingViewHolder) holder).progressBar.setIndeterminate(true);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return events.get(position) != null ?  VIEW_TYPE_EVENT : VIEW_TYPE_LOADING;
    }

    @Override
    public int getItemCount() {
        return events != null ? events.size() : 0;
    }

    public void showProgress() {
        events.add(null);
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    notifyItemInserted(getItemCount() - 1);
                } catch (java.lang.IndexOutOfBoundsException ignored) {}
            }
        };
        handler.post(runnable);

    }

    public void removeItem(int position) {
        events.remove(position);
        notifyItemRemoved(position);
    }

    public void removeProgress() {
        if (events != null && getItemCount() > 0 && events.get(events.size() - 1) == null)
            removeItem(events.size() - 1);
    }

    public void addNewItems(ArrayList<Event> newEvents) {
        int countBeforeAdding = events.size();
        events.addAll(newEvents);
            notifyItemRangeInserted(countBeforeAdding, newEvents.size());
    }

    public void clearAll() {
        events.clear();
        notifyDataSetChanged();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.event_image) ImageView imgEventImage;
        @BindView(R.id.event_from) TextView tvEventFrom;
        @BindView(R.id.event_title) TextView tvEventTitle;
        @BindView(R.id.event_participants) TextView tvEventParticipants;
        @BindView(R.id.event_location) TextView tvEventLocation;

        EventViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View view) {

        }
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.progress_bar) MaterialProgressBar progressBar;

        LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
