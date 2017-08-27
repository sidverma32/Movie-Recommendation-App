package com.skilenza.movieai;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.skilenza.movieai.oauth2.client.SignUpService;
import com.skilenza.movieai.oauth2.request.SignUpRequest;
import com.skilenza.movieai.oauth2.response.SignUpResponse;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by dominicneeraj on 08/08/17.
 */
public class SignupActivity extends AppCompatActivity {
    private static final String TAG = SignupActivity.class.getSimpleName();
    private Button btnSignUp;
    private EditText txtUsername;
    private EditText txtEmail;
    private EditText txtPassword;
    private EditText txtFirstName;
    private EditText txtLastName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        txtUsername = (EditText) findViewById(R.id.txtUsernameSignUp);
        txtEmail = (EditText) findViewById(R.id.txtEmailSignUp);
        txtPassword = (EditText) findViewById(R.id.txtPasswordSignUp);
        txtFirstName = (EditText) findViewById(R.id.txtFirstnameSignUp);
        txtLastName = (EditText) findViewById(R.id.txtLastnameSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

    }

    public void signUp() {
        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Signing you up...");
        progressDialog.show();
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setClient_id("crciQO9o8PHbgRhIWEVupKJ5h8p6s7szhdK0hwTo");
        signUpRequest.setClient_secret("jjlyXvO7n1pb3leXrbfUkr27TrgwPLTiq7cOSc7xfc7aOwetEsMNTkhhsOiBrB5maHWKrExqgfE3IEEOlHM3u0qNgoIs53aFOk8etmthveMHbtGgRWaHtPCHqGfgokcA");
        signUpRequest.setEmail(txtEmail.getText().toString());
        signUpRequest.setFirst_name(txtFirstName.getText().toString());
        signUpRequest.setLast_name(txtLastName.getText().toString());
        signUpRequest.setPassword(txtPassword.getText().toString());
        signUpRequest.setUsername(txtUsername.getText().toString());
        SignUpService signUpService = new SignUpService();
        signUpService.signUpService().signUp(signUpRequest, new Callback<SignUpResponse>() {
            @Override
            public void success(SignUpResponse signUpResponse, Response response) {
                progressDialog.dismiss();
                Log.e(TAG,String.valueOf(signUpResponse));
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
//                Toast.makeText(getApplicationContext(), String.valueOf(signUpResponse), Toast.LENGTH_LONG).show();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "Please Try Again", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();

        if (i == android.R.id.home) {

            finish();
            return true;

        }

        else {
            return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

}
