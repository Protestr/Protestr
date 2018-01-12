package es.dmoral.protestr.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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

public class AddedAdminAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<User> addedUsers;
    private User eventCreator;
    private OnAddedUserHandlerListener onAddedUserHandlerListener;

    public interface OnAddedUserHandlerListener {
        void onAddedUserRemoved(User user);
        void onAddedUserAdded(int pos);
    }

    public AddedAdminAdapter(User eventCreator, OnAddedUserHandlerListener onAddedUserHandlerListener) {
        addedUsers = new ArrayList<>();
        addedUsers.add(eventCreator);
        this.eventCreator = eventCreator;
        this.onAddedUserHandlerListener = onAddedUserHandlerListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AddedAdminViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.added_admin_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final User admin = addedUsers.get(position);
        Glide.with(((AddedAdminViewHolder) holder).adminImg)
                .load(admin.getProfilePicUrl())
                .into(((AddedAdminViewHolder) holder).adminImg);
        ((AddedAdminViewHolder) holder).tvAdminName.setText(admin.getUsername());
        if (admin.getId().equals(eventCreator.getId()))
            ((AddedAdminViewHolder) holder).removeAdminContainer.setVisibility(View.GONE);
        else
            ((AddedAdminViewHolder) holder).removeAdminContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return addedUsers == null ? 0 : addedUsers.size();
    }

    public ArrayList<User> getAddedAdmins() {
        return addedUsers;
    }

    public void addUser(User user) {
        addedUsers.add(user);
        notifyItemInserted(addedUsers.size() - 1);
        onAddedUserHandlerListener.onAddedUserAdded(addedUsers.size() - 1);
    }

    public void removeUser(User user) {
        final int userPos = addedUsers.indexOf(user);
        addedUsers.remove(user);
        notifyItemRemoved(userPos);
    }

    class AddedAdminViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.admin_img)
        ImageView adminImg;
        @BindView(R.id.tv_admin_name)
        TextView tvAdminName;
        @BindView(R.id.remove_admin_container)
        FrameLayout removeAdminContainer;

        public AddedAdminViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            removeAdminContainer.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            final User user = addedUsers.get(getAdapterPosition());
            onAddedUserHandlerListener.onAddedUserRemoved(user);
            removeUser(user);
        }
    }
}
