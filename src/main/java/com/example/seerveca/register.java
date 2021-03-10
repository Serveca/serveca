package com.example.seerveca;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class register extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;
    Spinner spinner;
    String service;
    ArrayAdapter<CharSequence> adapter;
    Button btnuser, btnbuser, btnsubmit;
    EditText etname, etMail, etPhone, etFirm, etpass, etrepass;
    TextView tvspinner;
    int c = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mProgressView = findViewById(R.id.login_progressbar);
        mLoginFormView = findViewById(R.id.login_form);
        tvLoad = findViewById(R.id.tvload);
        btnuser = (Button) findViewById(R.id.btnuser);
        btnbuser = (Button) findViewById(R.id.btnbuser);
        etname = findViewById(R.id.etname);
        etMail = findViewById(R.id.etMail);
        etPhone = findViewById(R.id.etPhone);
        etFirm = findViewById(R.id.etFirm);
        etpass = findViewById(R.id.etpass);
        etrepass = findViewById(R.id.etrepass);
        tvspinner = findViewById(R.id.tvspinner);
        btnsubmit = findViewById(R.id.btnSubmit);

        spinner = findViewById(R.id.etspinner);
        etFirm.setVisibility(View.GONE);
        tvspinner.setVisibility(View.GONE);
        spinner.setVisibility(View.GONE);
        adapter = ArrayAdapter.createFromResource(this, R.array.service, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                service = (String) parent.getItemAtPosition(position);



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });
        btnuser.setSelected(true);


        btnuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnuser.setSelected(!btnuser.isSelected());
                btnbuser.setSelected(!btnuser.isSelected());
                etFirm.setVisibility(View.GONE);
                tvspinner.setVisibility(View.GONE);
                spinner.setVisibility(View.GONE);
                etFirm.setText("");
                spinner.equals("");

                c = 1;

            }
        });
        btnbuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnbuser.setSelected(!btnbuser.isSelected());
                btnuser.setSelected(!btnbuser.isSelected());
                etFirm.setVisibility(View.VISIBLE);
                tvspinner.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.VISIBLE);
                c = 0;


            }
        });
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (c == 1) {
                    service = null;
                }

                if (etname.getText().toString().isEmpty() || etPhone.getText().toString().isEmpty()
                        || etMail.getText().toString().isEmpty() || etpass.getText().toString().isEmpty() || etrepass.getText().toString().isEmpty()) {
                    Toast.makeText(register.this, "Please enter all fields!", Toast.LENGTH_SHORT).show();
                } else {
                    if (etpass.getText().toString().trim().equals(etrepass.getText().toString().trim())) {
                        String name = etname.getText().toString().trim();
                        String email = etMail.getText().toString().trim();
                        String phone = etPhone.getText().toString().trim();
                        String password = etpass.getText().toString().trim();
                        String firm = etFirm.getText().toString().trim();
                        if (firm.equals("")) {
                            firm = null;
                        }
                        BackendlessUser user = new BackendlessUser();
                        user.setEmail(email);
                        user.setPassword(password);
                        user.setProperty("name", name);
                        user.setProperty("phone", phone);
                        user.setProperty("firm", firm);
                        user.setProperty("service", service);
                        showProgress(true);
                        tvLoad.setText("Busy registering user.. please wait...");
                        String finalFirm = firm;
                        Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
                            @Override
                            public void handleResponse(BackendlessUser response) {
                                showProgress(false);
                                if(service!=null)
                                {
                                    provider provider = new provider();
                                    provider.setName(name);
                                    provider.setPhone(phone);
                                    provider.setFirm(finalFirm);
                                    provider.setService(service);
                                    Backendless.Data.of(provider.class).save(provider, new AsyncCallback<com.example.seerveca.provider>() {
                                        @Override
                                        public void handleResponse(com.example.seerveca.provider response) {
                                            Toast.makeText(register.this, "User successfully register!", Toast.LENGTH_LONG).show();

                                        }

                                        @Override
                                        public void handleFault(BackendlessFault fault) {
                                            Toast.makeText(register.this, "Error "+fault.getMessage(), Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }
                                Toast.makeText(register.this, "User successfully register!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(register.this,Login.class));
                                register.this.finish();



                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Toast.makeText(register.this, "Error " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                                showProgress(false);

                            }
                        });


                    } else {
                        Toast.makeText(register.this, "Password did not match!", Toast.LENGTH_SHORT).show();
                    }

                }

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



