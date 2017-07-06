package es.dmoral.protestr.ui.activities;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import butterknife.ButterKnife;
import es.dmoral.protestr.R;
import es.dmoral.protestr.ui.activities.login.LoginActivity;
import es.dmoral.protestr.ui.activities.main.MainActivity;

/**
 * Created by grender on 13/02/17.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected void onCreate(@Nullable Bundle savedInstanceState, @LayoutRes int contentViewId) {
        super.onCreate(savedInstanceState);
        setContentView(contentViewId);
        ButterKnife.bind(this);
        setupViews();
        setListeners();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!(this instanceof MainActivity) &&
                !(this instanceof LoginActivity))
            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }

    protected abstract void setupViews();
    protected abstract void setListeners();
}
