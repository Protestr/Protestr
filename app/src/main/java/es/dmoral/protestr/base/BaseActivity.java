package es.dmoral.protestr.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

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

    protected abstract void setupViews();
    protected abstract void setListeners();
}
