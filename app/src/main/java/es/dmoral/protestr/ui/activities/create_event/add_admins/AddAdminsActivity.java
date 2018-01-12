package es.dmoral.protestr.ui.activities.create_event.add_admins;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;

import butterknife.BindView;
import es.dmoral.protestr.R;
import es.dmoral.protestr.data.models.dao.User;
import es.dmoral.protestr.ui.activities.BaseActivity;
import es.dmoral.protestr.ui.adapters.AddedAdminAdapter;
import es.dmoral.protestr.ui.adapters.FilteredAdminAdapter;
import es.dmoral.protestr.ui.custom.MarginItemDecorator;
import es.dmoral.protestr.utils.Constants;
import es.dmoral.protestr.utils.KeyboardUtils;
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
    @BindView(R.id.filter_admin_progress)
    ProgressBar filterAdminProgress;

    private AddAdminsPresenter addAdminsPresenter;

    private String lastTypedUsername = "";
    private long lastTypeTimestamp = 0;
    private Handler typeHandler = new Handler();

    private static final long TEXT_DELAY = 500;
    private static final long TEXT_DELAY_THRESHOLD = 250;

    private static final String ADDED_USERS_SAVE_STATE = "ADDED_USERS_SAVE_STATE";

    private Runnable typeRunnable = new Runnable() {
        @Override
        public void run() {
            if (System.currentTimeMillis() > lastTypeTimestamp + TEXT_DELAY_THRESHOLD) {
                if (lastTypedUsername.length() == 0) {
                    ((FilteredAdminAdapter) filteredAdminsRecyclerView.getAdapter()).clear();
                } else {
                    addAdminsPresenter.filterUsers(lastTypedUsername);
                    filterAdminProgress.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_add_admins);
        addAdminsPresenter = new AddAdminsPresenterImpl(this);
        restoreStates(savedInstanceState);
    }

    @Override
    protected void setupViews() {
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        addedAdminsRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        filteredAdminsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        filteredAdminsRecyclerView.addItemDecoration(new MarginItemDecorator(this,
                16 + 16 + 56));
        filteredAdminsRecyclerView.setAdapter(new FilteredAdminAdapter(this));
    }

    @Override
    protected void setListeners() {
        etFilterAdmins.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // unused
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                typeHandler.removeCallbacks(typeRunnable);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                lastTypeTimestamp = System.currentTimeMillis();
                lastTypedUsername = editable.toString();
                typeHandler.postDelayed(typeRunnable, TEXT_DELAY);
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
    public void restoreStates(Bundle savedInstanceState) {
        final ArrayList<User> addedUsers;
        if (savedInstanceState != null) {
            addedUsers = savedInstanceState.getParcelableArrayList(ADDED_USERS_SAVE_STATE);
        } else if (getIntent().hasExtra(Constants.ADDED_ADMINS_EXTRA)) {
            addedUsers = getIntent().getParcelableArrayListExtra(Constants.ADDED_ADMINS_EXTRA);
        } else { // Just in case...
            addedUsers = new ArrayList<>();
            addedUsers.add(PreferencesUtils.getLoggedUser(this));
        }
        addedAdminsRecyclerView.setAdapter(new AddedAdminAdapter(addedUsers, PreferencesUtils.getLoggedUser(this),
                this));
    }

    @Override
    public void addFilteredUsers(final ArrayList<User> filteredUsers) {
        filterAdminProgress.setVisibility(View.GONE);
        ((FilteredAdminAdapter) filteredAdminsRecyclerView.getAdapter()).swap(filteredUsers);
    }

    @Override
    public ArrayList<User> getAddedAdmins() {
        return ((AddedAdminAdapter) addedAdminsRecyclerView.getAdapter()).getAddedAdmins();
    }

    @Override
    public void backToEventCreation() {
        final Intent returnIntent = new Intent();
        returnIntent.putParcelableArrayListExtra(Constants.ADDED_ADMINS_EXTRA, getAddedAdmins());
        setResult(RESULT_OK, returnIntent);
        onBackPressed();
    }

    @Override
    public void onFilteredAdminClicked(User user) {
        ((AddedAdminAdapter) addedAdminsRecyclerView.getAdapter()).addUser(user);
    }

    @Override
    public void onAddedUserRemoved(User user) {
        if (etFilterAdmins.getText().toString().length() == 0) {
            ((FilteredAdminAdapter) filteredAdminsRecyclerView.getAdapter()).clear();
        } else {
            addAdminsPresenter.filterUsers(etFilterAdmins.getText().toString());
        }
    }

    @Override
    public void onAddedUserAdded(int pos) {
        addedAdminsRecyclerView.smoothScrollToPosition(pos);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(ADDED_USERS_SAVE_STATE, getAddedAdmins());
    }

    @Override
    public void onBackPressed() {
        if (etFilterAdmins.hasFocus())
            KeyboardUtils.closeKeyboard(etFilterAdmins);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        addAdminsPresenter.onDestroy();
    }
}
