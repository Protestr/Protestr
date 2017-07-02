package es.dmoral.protestr.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import es.dmoral.prefs.Prefs;
import es.dmoral.protestr.login.LoginActivity;
import es.dmoral.protestr.main.MainActivity;
import es.dmoral.protestr.utils.Constants;

public class SplashActivity extends AppCompatActivity implements SplashView {

    private SplashPresenter splashPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.splashPresenter = new SplashPresenterImpl(this);
        if (Prefs.with(this).readBoolean(Constants.PREFERENCES_LOGGED_IN, false)) {
            splashPresenter.confirmLoginAttempt(Prefs.with(this).read(Constants.PREFERENCES_EMAIL), Prefs.with(this).read(Constants.PREFERENCES_PASSWORD));
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
