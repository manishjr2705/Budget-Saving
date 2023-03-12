package com.example.budgets;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private EditText mEmail;
    private EditText mPAss;
    private Button btnLogin;
    private TextView mForgotPassword;
    private TextView msigninhere;
    private ProgressDialog mDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth=FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        }

        mDialog=new ProgressDialog(this);
        LoginDetail();
    }
        private void LoginDetail(){
            mEmail=findViewById(R.id.email_reg);
            mPAss=findViewById(R.id.password_reg);
            btnLogin=findViewById(R.id.btn_Login);
            msigninhere=findViewById(R.id.signup_reg);
            mForgotPassword=findViewById(R.id.forget_password);

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String email=mEmail.getText().toString().trim();
                    String pass=mPAss.getText().toString().trim();

                    if (TextUtils.isEmpty(email)){
                        mEmail.setError("Email required..");
                        return;
                    }
                    if (TextUtils.isEmpty(pass)){
                        mPAss.setError("password required..");
                        return;
                    }

                       mDialog.setMessage("Processing");
                    mDialog.show();

                    mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                mDialog.dismiss();
                                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                                Toast.makeText(getApplicationContext(),"Login successful",Toast.LENGTH_SHORT).show();
                            }else
                            {
                                mDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"Login Fail",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            });
            //Registration activity
            msigninhere.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(),reseatActivity.class));
                }
            });

            //

            mForgotPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(),reseatActivity.class));
                }
            });


        }

    }