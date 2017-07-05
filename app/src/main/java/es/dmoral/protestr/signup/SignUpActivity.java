package es.dmoral.protestr.signup;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.protestr.R;
import es.dmoral.protestr.base.BaseActivity;
import es.dmoral.protestr.create_event.CreateEventActivity;
import es.dmoral.protestr.custom.SimplifiedTextWatcher;
import es.dmoral.protestr.main.MainActivity;
import es.dmoral.protestr.splash.SplashActivity;
import es.dmoral.protestr.utils.Constants;
import es.dmoral.protestr.utils.FormatUtils;
import es.dmoral.protestr.utils.IdenticonUtils;
import es.dmoral.protestr.utils.KeyboardUtils;
import es.dmoral.protestr.utils.RotationUtils;
import es.dmoral.protestr.utils.Sha256Utils;
import es.dmoral.protestr.utils.TimeUtils;
import es.dmoral.toasty.Toasty;
import tyrantgit.explosionfield.ExplosionField;

public class SignUpActivity extends BaseActivity implements SignUpView {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.profile_image) ImageView profileImage;
    @BindView(R.id.et_email) EditText etEmail;
    @BindView(R.id.et_username) EditText etUsername;
    @BindView(R.id.password_input_layout) TextInputLayout passwordInputLayout;
    @BindView(R.id.repeat_password_input_layout) TextInputLayout repeatPasswordInputLayout;
    @BindView(R.id.et_password) EditText etPassword;
    @BindView(R.id.et_repeat_password) EditText etRepeatPassword;
    @BindView(R.id.full_view) View fullView;
    @BindView(R.id.content_scroll_view) ScrollView contentScrollView;
    @BindView(R.id.profile_image_container) LinearLayout profileImageContainer;
    @BindView(R.id.logo_image) ImageView logoImage;
    @BindView(R.id.tv_terms_and_conditions) TextView tvTermsAndConditions;

    private ExplosionField explosionField;

    private float[] previousImageCoords = {0, 0};
    private SignUpPresenterImpl signUpPresenter;
    private ValueAnimator animator;
    private boolean loading = false;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_sign_up);
        signUpPresenter = new SignUpPresenterImpl(this);
    }

    @Override
    protected void setupViews() {
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().getDecorView().requestFocus();
        explosionField = ExplosionField.attach2Window(this);
    }

    @Override
    protected void setListeners() {
        etEmail.addTextChangedListener(new SimplifiedTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0 || !FormatUtils.isValidEmailFormat(charSequence))
                    profileImage.setImageDrawable(null);
                else
                    Glide.with(SignUpActivity.this)
                            .load(IdenticonUtils.generateGravatarUrl(charSequence.toString()))
                            .into(profileImage);
            }
        });
        etPassword.addTextChangedListener(new SimplifiedTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etPassword.getError() != null)
                    passwordInputLayout.setPasswordVisibilityToggleEnabled(true);
            }
        });
        etRepeatPassword.addTextChangedListener(new SimplifiedTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etRepeatPassword.getError() != null)
                    repeatPasswordInputLayout.setPasswordVisibilityToggleEnabled(true);
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
                KeyboardUtils.closeKeyboard(getCurrentFocus());
                if (!checkParametersErrors()) {
                    startAnimation();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    signUpPresenter.attemptSignUp(
                                            etUsername.getText().toString().trim(),
                                            etEmail.getText().toString().trim(),
                                            Sha256Utils.digest(etPassword.getText().toString().trim()),
                                            IdenticonUtils.generateGravatarUrl(etEmail.getText().toString().trim())
                                    );
                                }
                            });
                        }
                    }, 750);
                } else {
                    contentScrollView.smoothScrollTo(0, 0);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void signUpError(final String message) {
        Toasty.error(this, message).show();
    }

    @Override
    public void startAnimation() {
        loading = true;
        final DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        RotationUtils.lockOrientation(this);

        previousImageCoords[0] = profileImageContainer.getX();
        previousImageCoords[1] = profileImageContainer.getY();

        TranslateAnimation translateContent =
                new TranslateAnimation(0, 0, 0, metrics.heightPixels);
        translateContent.setStartOffset(150);
        translateContent.setDuration(350);
        translateContent.setFillAfter(true);

        TranslateAnimation translateFullView =
                new TranslateAnimation(0, 0, -metrics.heightPixels, 0);
        translateFullView.setDuration(500);
        translateFullView.setFillAfter(true);

        final TranslateAnimation translateProfilePic =
                new TranslateAnimation(0, 0, 0, (metrics.heightPixels / 2) -
                        (profileImageContainer.getHeight()));
        translateProfilePic.setStartOffset(100);
        translateProfilePic.setDuration(350);
        translateProfilePic.setFillAfter(true);
        translateProfilePic.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                contentScrollView.setVisibility(View.GONE);
                contentScrollView.clearAnimation();
                logoImage.setVisibility(View.VISIBLE);
                animator = ValueAnimator
                        .ofFloat(0f, 1f)
                        .setDuration(150);
                animator.setRepeatCount(ValueAnimator.INFINITE);
                animator.setRepeatMode(ValueAnimator.RESTART);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    Random random = new Random();

                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        profileImageContainer.requestLayout();
                        profileImageContainer.setTranslationX((random.nextFloat() - 0.5f)
                                * profileImageContainer.getWidth() * 0.05f);
                        profileImageContainer.setTranslationY((random.nextFloat() - 0.5f)
                                * profileImageContainer.getHeight() * 0.05f);

                    }
                });
                animator.start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        contentScrollView.startAnimation(translateContent);
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            toolbar.getChildAt(i).setVisibility(View.INVISIBLE);
        }
        fullView.setVisibility(View.VISIBLE);
        fullView.startAnimation(translateFullView);
        profileImageContainer.startAnimation(translateProfilePic);
    }

    @Override
    public void restorePreAnimationState() {
        loading = false;
        if (animator != null) {
            animator.end();
            animator = null;
        }
        final DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        RotationUtils.restoreOrientation(this);

        TranslateAnimation translateContent =
                new TranslateAnimation(0, 0, metrics.heightPixels, 0);
        translateContent.setDuration(350);
        translateContent.setFillAfter(true);

        TranslateAnimation translateFullView =
                new TranslateAnimation(0, 0, 0, -metrics.heightPixels);
        translateFullView.setDuration(500);
        translateFullView.setFillAfter(true);
        translateFullView.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                logoImage.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                fullView.setVisibility(View.GONE);
                for (int i = 0; i < toolbar.getChildCount(); i++) {
                    toolbar.getChildAt(i).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        translateContent.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                contentScrollView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        TranslateAnimation translateProfilePic =
                new TranslateAnimation(0, 0, (metrics.heightPixels / 2) -
                        (profileImageContainer.getHeight()), 0);
        translateProfilePic.setStartOffset(100);
        translateProfilePic.setDuration(350);
        translateProfilePic.setFillAfter(true);

        translateProfilePic.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                profileImageContainer.setX(previousImageCoords[0]);
                profileImageContainer.setY(previousImageCoords[1]);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        contentScrollView.startAnimation(translateContent);
        fullView.startAnimation(translateFullView);
        profileImageContainer.startAnimation(translateProfilePic);
    }

    @Override
    public void endLoadingAnimation() {
        if (animator != null) {
            animator.end();
            animator = null;
        }

        final DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        RotationUtils.restoreOrientation(this);

        profileImageContainer.setY((metrics.heightPixels / 2) -
                (profileImageContainer.getHeight()) +
                (profileImageContainer.getHeight() / 2));
        explosionField.explode(profileImageContainer);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                finish();
            }
        }, 1500);
    }

    @Override
    public void connectionError() {
        restorePreAnimationState();
        Toasty.error(this, getString(R.string.connection_error)).show();
    }

    @Override
    public boolean checkParametersErrors() {
        final String email = etEmail.getText().toString().trim();
        final String userName = etUsername.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();
        final String passwordRepeat = etRepeatPassword.getText().toString().trim();

        boolean error = false;
        View focusViewOnError = null;

        if (TextUtils.isEmpty(email)) {
            etEmail.setError(getString(R.string.empty_field));
            focusViewOnError = etEmail;
            error = true;
        }
        if (TextUtils.isEmpty(userName)) {
            etUsername.setError(getString(R.string.empty_field));
            focusViewOnError = etUsername;
            error = true;
        }
        if (TextUtils.isEmpty(password)) {
            passwordInputLayout.setPasswordVisibilityToggleEnabled(false);
            etPassword.setError(getString(R.string.empty_field));
            focusViewOnError = etPassword;
            error = true;
        }
        if (TextUtils.isEmpty(passwordRepeat)) {
            repeatPasswordInputLayout.setPasswordVisibilityToggleEnabled(false);
            etRepeatPassword.setError(getString(R.string.empty_field));
            focusViewOnError = etRepeatPassword;
            error = true;
        }

        if (!TextUtils.isEmpty(email) && !FormatUtils.isValidEmailFormat(email)) {
            passwordInputLayout.setPasswordVisibilityToggleEnabled(false);
            etEmail.setError(getString(R.string.email_not_valid_error));
            focusViewOnError = etEmail;
            error = true;
        }

        if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(passwordRepeat) &&
                !password.equals(passwordRepeat)) {
            repeatPasswordInputLayout.setPasswordVisibilityToggleEnabled(false);
            etRepeatPassword.setError(getString(R.string.password_doesnt_match));
            focusViewOnError = etRepeatPassword;
            error = true;
        }

        if (error) {
            focusViewOnError.requestFocus();
        }

        return error;
    }

    @OnClick(R.id.tv_terms_and_conditions)
    @Override
    public void openTermsAndConditions() {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        builder.setStartAnimations(this, R.anim.activity_in, R.anim.activity_out);
        builder.setExitAnimations(this, R.anim.activity_back_in, R.anim.activity_back_out);
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(Constants.TERMS_URL));
    }

    @Override
    protected void onDestroy() {
        signUpPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (!loading)
            super.onBackPressed();
    }
}
