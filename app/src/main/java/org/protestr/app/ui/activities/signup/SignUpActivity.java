package org.protestr.app.ui.activities.signup;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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

import org.protestr.app.utils.FormatUtils;
import org.protestr.app.utils.GravatarUtils;
import org.protestr.app.utils.KeyboardUtils;
import org.protestr.app.utils.RotationUtils;
import org.protestr.app.utils.Sha256Utils;

import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;
import org.protestr.app.R;
import org.protestr.app.ui.activities.BaseActivity;
import org.protestr.app.ui.activities.main.MainActivity;
import org.protestr.app.ui.custom.SimplifiedTextWatcher;
import org.protestr.app.utils.Constants;
import org.protestr.app.utils.FormatUtils;
import org.protestr.app.utils.GravatarUtils;
import org.protestr.app.utils.KeyboardUtils;
import org.protestr.app.utils.RotationUtils;
import org.protestr.app.utils.Sha256Utils;
import es.dmoral.toasty.Toasty;
import tyrantgit.explosionfield.ExplosionField;

public class SignUpActivity extends BaseActivity implements SignUpView {

    @BindView(org.protestr.app.R.id.toolbar)
    Toolbar toolbar;
    @BindView(org.protestr.app.R.id.profile_image)
    ImageView profileImage;
    @BindView(org.protestr.app.R.id.et_email)
    EditText etEmail;
    @BindView(org.protestr.app.R.id.et_username)
    EditText etUsername;
    @BindView(org.protestr.app.R.id.password_input_layout)
    TextInputLayout passwordInputLayout;
    @BindView(org.protestr.app.R.id.repeat_password_input_layout)
    TextInputLayout repeatPasswordInputLayout;
    @BindView(org.protestr.app.R.id.et_password)
    EditText etPassword;
    @BindView(org.protestr.app.R.id.et_repeat_password)
    EditText etRepeatPassword;
    @BindView(org.protestr.app.R.id.full_view)
    View fullView;
    @BindView(org.protestr.app.R.id.content_scroll_view)
    ScrollView contentScrollView;
    @BindView(org.protestr.app.R.id.profile_image_container)
    LinearLayout profileImageContainer;
    @BindView(org.protestr.app.R.id.logo_image)
    ImageView logoImage;
    @BindView(org.protestr.app.R.id.tv_terms_and_conditions)
    TextView tvTermsAndConditions;

    private ExplosionField explosionField;

    private float[] previousImageCoords = {0, 0};
    private SignUpPresenterImpl signUpPresenter;
    private ValueAnimator animator;
    private boolean loading = false;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, org.protestr.app.R.layout.activity_sign_up);
        signUpPresenter = new SignUpPresenterImpl(this);
    }

    @Override
    protected void setupViews() {
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().getDecorView().requestFocus();
        explosionField = ExplosionField.attach2Window(this);
        etEmail.setError(null);
        etUsername.setError(null);
        etPassword.setError(null);
        etRepeatPassword.setError(null);
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
                            .load(GravatarUtils.generateGravatarUrl(charSequence.toString()))
                            .into(profileImage);
            }
        });
        etUsername.addTextChangedListener(new SimplifiedTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etUsername.getError() != null)
                    etUsername.setError(null);
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
        getMenuInflater().inflate(org.protestr.app.R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case org.protestr.app.R.id.action_sign_up:
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
                                            GravatarUtils.generateGravatarUrl(etEmail.getText().toString().trim())
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
                overridePendingTransition(org.protestr.app.R.anim.activity_in, org.protestr.app.R.anim.activity_out);
                finish();
            }
        }, 1500);
    }

    @Override
    public void connectionError() {
        restorePreAnimationState();
        Toasty.error(this, getString(org.protestr.app.R.string.connection_error)).show();
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
            etEmail.setError(getString(org.protestr.app.R.string.empty_field));
            focusViewOnError = etEmail;
            error = true;
        }
        if (TextUtils.isEmpty(userName)) {
            etUsername.setError(getString(org.protestr.app.R.string.empty_field));
            focusViewOnError = etUsername;
            error = true;
        }
        if (TextUtils.isEmpty(password)) {
            passwordInputLayout.setPasswordVisibilityToggleEnabled(false);
            etPassword.setError(getString(org.protestr.app.R.string.empty_field));
            focusViewOnError = etPassword;
            error = true;
        }
        if (TextUtils.isEmpty(passwordRepeat)) {
            repeatPasswordInputLayout.setPasswordVisibilityToggleEnabled(false);
            etRepeatPassword.setError(getString(org.protestr.app.R.string.empty_field));
            focusViewOnError = etRepeatPassword;
            error = true;
        }

        if (!TextUtils.isEmpty(email) && !FormatUtils.isValidEmailFormat(email)) {
            passwordInputLayout.setPasswordVisibilityToggleEnabled(false);
            etEmail.setError(getString(org.protestr.app.R.string.email_not_valid_error));
            focusViewOnError = etEmail;
            error = true;
        }

        if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(passwordRepeat) &&
                !password.equals(passwordRepeat)) {
            repeatPasswordInputLayout.setPasswordVisibilityToggleEnabled(false);
            etRepeatPassword.setError(getString(org.protestr.app.R.string.password_doesnt_match));
            focusViewOnError = etRepeatPassword;
            error = true;
        }

        if (error) {
            focusViewOnError.requestFocus();
        }

        return error;
    }

    @OnClick(org.protestr.app.R.id.tv_terms_and_conditions)
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
