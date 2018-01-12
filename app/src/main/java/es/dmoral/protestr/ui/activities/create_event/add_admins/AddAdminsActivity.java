package es.dmoral.protestr.ui.activities.create_event.add_admins;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import butterknife.BindView;
import es.dmoral.protestr.R;
import es.dmoral.protestr.data.models.dao.User;
import es.dmoral.protestr.ui.activities.BaseActivity;
import es.dmoral.protestr.ui.adapters.AddedAdminAdapter;
import es.dmoral.protestr.ui.adapters.FilteredAdminAdapter;
import es.dmoral.protestr.ui.custom.MarginItemDecorator;
import es.dmoral.protestr.ui.custom.SimplifiedTextWatcher;
import es.dmoral.protestr.utils.PreferencesUtils;

public class AddAdminsActivity extends BaseActivity implements AddAdminsView,
        FilteredAdminAdapter.OnFilteredAdminClickedListener, AddedAdminAdapter.OnAddedUserHandlerListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.added_admins_list)
    RecyclerView addedAdminsRecyclerView;
    @BindView(R.id.et_filter_admins)
    AppCompatEditText etFilterAdmins;
    @BindView(R.id.filtered_admins_list)
    RecyclerView filteredAdminsRecyclerView;

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
        addedAdminsRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        addedAdminsRecyclerView.setAdapter(new AddedAdminAdapter(PreferencesUtils.getLoggedUser(this),
                this));
        filteredAdminsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        filteredAdminsRecyclerView.addItemDecoration(new MarginItemDecorator(this,
                16 + 16 + 56));
        filteredAdminsRecyclerView.setAdapter(new FilteredAdminAdapter(this));
    }

    @Override
    protected void setListeners() {
        etFilterAdmins.addTextChangedListener(new SimplifiedTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (false) {
                    ((FilteredAdminAdapter) filteredAdminsRecyclerView.getAdapter()).clear();
                } else {
                    addAdminsPresenter.filterUsers(charSequence.toString());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_admins, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_admins:
                backToEventCreation();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void addFilteredUsers(final ArrayList<User> filteredUsers) {
        ((FilteredAdminAdapter) filteredAdminsRecyclerView.getAdapter()).swap(filteredUsers);
    }

    @Override
    public ArrayList<User> getAddedAdmins() {
        return ((AddedAdminAdapter) addedAdminsRecyclerView.getAdapter()).getAddedAdmins();
    }

    @Override
    public void backToEventCreation() {
        onBackPressed();
    }

    @Override
    public void onFilteredAdminClicked(User user) {
        ((AddedAdminAdapter) addedAdminsRecyclerView.getAdapter()).addUser(user);
    }

    @Override
    public void onAddedUserRemoved(User user) {
        addAdminsPresenter.filterUsers(etFilterAdmins.getText().toString());
    }

    @Override
    public void onAddedUserAdded(int pos) {
        addedAdminsRecyclerView.smoothScrollToPosition(pos);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        addAdminsPresenter.onDestroy();
    }
}
