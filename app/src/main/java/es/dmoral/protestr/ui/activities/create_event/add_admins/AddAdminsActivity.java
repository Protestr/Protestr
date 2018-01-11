package es.dmoral.protestr.ui.activities.create_event.add_admins;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import butterknife.BindView;
import es.dmoral.protestr.R;
import es.dmoral.protestr.data.models.dao.User;
import es.dmoral.protestr.ui.activities.BaseActivity;

public class AddAdminsActivity extends BaseActivity implements AddAdminsView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private AddAdminsPresenter addAdminsPresenter;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_add_admins);
        addAdminsPresenter = new AddAdminsPresenterImpl(this);
    }

    @Override
    protected void setupViews() {
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
    }

    @Override
    protected void setListeners() {

    }

    @Override
    public void addFilteredUsers(final ArrayList<User> filteredUsers) {

    }

    @Override
    public void addUser() {

    }

    @Override
    public void removeUser() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        addAdminsPresenter.onDestroy();
    }
}
