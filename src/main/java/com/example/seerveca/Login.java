package com.example.seerveca;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.local.UserIdStorageFactory;

public class Login extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;
    EditText etmail,etpassword;
    TextView tvreset,tvcreate;
    Button btnlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mProgressView = findViewById(R.id.login_progressbar);
        mLoginFormView = findViewById(R.id.login_form);
        tvLoad = findViewById(R.id.tvload);
        etmail=findViewById(R.id.etemail);
        etpassword=findViewById(R.id.etpassword);
        tvreset=findViewById(R.id.tvreset);
        tvcreate=findViewById(R.id.tvcreate);
        btnlogin=findViewById(R.id.btnlogin);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etmail.getText().toString().isEmpty()||etpassword.getText().toString().isEmpty())
                {
                    Toast.makeText(Login.this, "Please enter all feilds!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String email=etmail.getText().toString().trim();
                    String password=etpassword.getText().toString().trim();
                    showProgress(true);
                    tvLoad.setText("Checking login credentials...please  wait...");
                    Backendless.UserService.login(email, password, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response) {
                            tvLoad.setText("Logging you in");
                            Toast.makeText(Login.this, "Logged in successfully!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Login.this, MainActivity.class));
                            Login.this.finish();

                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(Login.this, "Error"+fault.getMessage(), Toast.LENGTH_SHORT).show();
                            showProgress(false);

                        }
                    },true);
                }
            }
        });
        tvcreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(Login.this,register.class));


            }
        });
        tvreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etmail.getText().toString().isEmpty())
                {
                    Toast.makeText(Login.this, "Please enter E-mail id!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String email= etmail.getText().toString().trim();
                    showProgress(true);
                    tvLoad.setText("Busy sending reset instruction...please  wait...");
                    Backendless.UserService.restorePassword(email, new AsyncCallback<Void>() {
                        @Override
                        public void handleResponse(Void response) {
                            Toast.makeText(Login.this, "Reset instruction set to email address!", Toast.LENGTH_SHORT).show();
                            showProgress(false);

                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(Login.this, "Error "+fault.getMessage(), Toast.LENGTH_SHORT).show();
                            showProgress(false);

                        }
                    });
                }

            }
        });
        showProgress(true);
        tvLoad.setText("Checking login credentials...please  wait...");
        Backendless.UserService.isValidLogin(new AsyncCallback<Boolean>() {
            @Override
            public void handleResponse(Boolean response) {
                if (response)
                {
                    String userobjectid= UserIdStorageFactory.instance().getStorage().get();
                    tvLoad.setText("Logging you in...please  wait...");
                    Backendless.Data.of(BackendlessUser.class).findById(userobjectid, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response) {

                            startActivity(new Intent(Login.this,MainActivity.class));
                            Login.this.finish();

                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(Login.this, "Error "+fault.getMessage(), Toast.LENGTH_SHORT).show();
                            showProgress(false);

                        }
                    });

                }
                else
                {
                    showProgress(false);
                }


            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(Login.this, "Error "+fault.getMessage(), Toast.LENGTH_SHORT).show();
                showProgress(false);
            }
        });


    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}