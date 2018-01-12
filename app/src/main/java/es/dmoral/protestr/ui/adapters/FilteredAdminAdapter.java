package es.dmoral.protestr.ui.adapters;

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
import es.dmoral.protestr.data.models.dao.User;

/**
 * Created by grender on 12/01/18.
 */

public class FilteredAdminAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<User> filteredUsers;
    private OnFilteredAdminClickedListener onFilteredAdminClickedListener;

    public interface OnFilteredAdminClickedListener {
        void onFilteredAdminClicked(User user);
    }

    public FilteredAdminAdapter(OnFilteredAdminClickedListener onFilteredAdminClickedListener) {
        filteredUsers = new ArrayList<>();
        this.onFilteredAdminClickedListener = onFilteredAdminClickedListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FilteredAdminViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.filtered_admin_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final User admin = filteredUsers.get(position);
        Glide.with(((FilteredAdminViewHolder) holder).ivFilteredAdminAvatar)
                .load(admin.getProfilePicUrl())
                .into(((FilteredAdminViewHolder) holder).ivFilteredAdminAvatar);
        ((FilteredAdminViewHolder) holder).tvFilteredAdminName.setText(admin.getUsername());
    }

    @Override
    public int getItemCount() {
        return filteredUsers == null ? 0 : filteredUsers.size();
    }

    public void swap(ArrayList<User> filteredUsers) {
        this.filteredUsers = filteredUsers;
        notifyDataSetChanged();
    }

    public void clear() {
        filteredUsers.clear();
        notifyDataSetChanged();
    }

    class FilteredAdminViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_filtered_admin)
        ImageView ivFilteredAdminAvatar;
        @BindView(R.id.tv_filtered_admin_name)
        TextView tvFilteredAdminName;

        FilteredAdminViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            final int adapterPosition = getAdapterPosition();
            onFilteredAdminClickedListener.onFilteredAdminClicked(filteredUsers.get(adapterPosition));
            filteredUsers.remove(adapterPosition);
            notifyItemRemoved(adapterPosition);
        }
    }
}
