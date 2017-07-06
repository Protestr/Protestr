package es.dmoral.protestr.ui.activities.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.protestr.R;
import es.dmoral.protestr.ui.activities.BaseActivity;
import es.dmoral.protestr.ui.custom.SimplifiedTextWatcher;
import es.dmoral.protestr.ui.activities.main.MainActivity;
import es.dmoral.protestr.ui.activities.signup.SignUpActivity;
import es.dmoral.protestr.utils.Sha256Utils;
import es.dmoral.toasty.Toasty;

public class LoginActivity extends BaseActivity implements LoginView {

    @BindView(R.id.input_layout_email) TextInputLayout txInEmail;
    @BindView(R.id.et_email) EditText etEmail;
    @BindView(R.id.input_layout_password) TextInputLayout txInPassword;
    @BindView(R.id.et_password) EditText etPassword;
    @BindView(R.id.button_login) Button btnLogin;
    @BindView(R.id.button_sign_up) Button btnSignUp;
    @BindView(R.id.login_logo) TextView txLoginLogo;

    private LoginPresenter loginPresenter;

    private MaterialDialog progressDialog;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_login);
        loginPresenter = new LoginPresenterImpl(this);
    }

    @Override
    protected void setupViews() {
        txLoginLogo.setTypeface(Typeface.createFromAsset(getAssets(), "PermanentMarker.ttf"));
        etEmail.setError(null);
        etPassword.setError(null);
    }

    @Override
    protected void setListeners() {
        etPassword.addTextChangedListener(new SimplifiedTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etPassword.getError() != null)
                    txInPassword.setPasswordVisibilityToggleEnabled(true);
            }
        });
    }

    @Override
    public void showProgress() {
        progressDialog = new MaterialDialog.Builder(this)
                .content(R.string.processing_text)
                .progress(true, 0)
                .cancelable(false)
                .show();
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    public void loginError(final String message) {
        Toasty.error(this, message).show();
    }

    @Override
    public void loginSuccess() {
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
        finish();
    }

    @Override
    public void connectionError() {
        Toasty.error(this, getString(R.string.connection_error)).show();
    }

    @OnClick(R.id.button_login)
    @Override
    public void loginAction() {
        etEmail.setError(null);
        etPassword.setError(null);

        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();

        boolean cancel = false;
        View focusViewOnError = null;

        if (TextUtils.isEmpty(password)) {
            txInPassword.setPasswordVisibilityToggleEnabled(false);
            etPassword.setError(getString(R.string.password_empty_error));
            focusViewOnError = etPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError(getString(R.string.email_not_valid_error));
            focusViewOnError = etEmail;
            cancel = true;
        }

        if (cancel)
            focusViewOnError.requestFocus();
        else {
            loginPresenter.attemptLogin(email.trim(), Sha256Utils.digest(password));
        }
    }

    @OnClick(R.id.button_sign_up)
    @Override
    public void signUpAction() {
        startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginPresenter.onDestroy();
        progressDialog = null;
    }
}