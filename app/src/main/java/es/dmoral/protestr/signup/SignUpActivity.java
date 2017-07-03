package es.dmoral.protestr.signup;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import es.dmoral.protestr.R;
import es.dmoral.protestr.base.BaseActivity;
import es.dmoral.protestr.create_event.CreateEventActivity;
import es.dmoral.protestr.utils.IdenticonUtils;
import es.dmoral.protestr.utils.TimeUtils;
import es.dmoral.toasty.Toasty;

public class SignUpActivity extends BaseActivity implements SignUpView {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.profile_image) ImageView profileImage;
    @BindView(R.id.et_email) EditText etEmail;
    @BindView(R.id.et_username) EditText etUsername;
    @BindView(R.id.et_password) EditText etPassword;
    @BindView(R.id.et_repeat_password) EditText etRepeatPassword;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_sign_up);
    }

    @Override
    protected void setupViews() {
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().getDecorView().requestFocus();
    }

    @Override
    protected void setListeners() {
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0)
                    profileImage.setImageDrawable(null);
                else
                    Glide.with(SignUpActivity.this)
                            .load(IdenticonUtils.generateGravatarUrl(charSequence.toString()))
                            .into(profileImage);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sign_up:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void signUpError(final String message) {
        Toasty.error(this, message).show();
    }

    @Override
    public void connectionError() {

    }
}
