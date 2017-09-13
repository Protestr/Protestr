package es.dmoral.protestr.ui.activities.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import es.dmoral.prefs.Prefs;
import es.dmoral.protestr.ui.activities.login.LoginActivity;
import es.dmoral.protestr.ui.activities.main.MainActivity;
import es.dmoral.protestr.utils.PreferencesUtils;
import es.dmoral.protestr.utils.RotationUtils;

public class SplashActivity extends AppCompatActivity implements SplashView {

    private SplashPresenter splashPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RotationUtils.lockOrientation(this);
        this.splashPresenter = new SplashPresenterImpl(this);
        if (Prefs.with(this).readBoolean(PreferencesUtils.PREFERENCES_LOGGED_IN, false)) {
            splashPresenter.confirmLoginAttempt(Prefs.with(this).read(PreferencesUtils.PREFERENCES_EMAIL), Prefs.with(this).read(PreferencesUtils.PREFERENCES_PASSWORD));
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    @Override
    public void loginConfirmed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void loginUnconfirmed() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        splashPresenter.onDestroy();
    }
}
