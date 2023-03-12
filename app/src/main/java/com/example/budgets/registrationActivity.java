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

public class registrationActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPAss;
    private Button btnReg;
    private TextView mSignin;
    private ProgressDialog mDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        mAuth=FirebaseAuth.getInstance();
        mDialog=new ProgressDialog(this);
        registration();

    }

    private void registration() {

        mEmail=findViewById(R.id.email_reg);
        mPAss=findViewById(R.id.password_reg);
        btnReg=findViewById(R.id.btn_Reg);
        mSignin=findViewById(R.id.signin_here);

        btnReg.setOnClickListener(new View.OnClickListener() {
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
                }
                 mDialog.setMessage("Processing");
                mDialog.show();
                mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                             mDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"register",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        }
                        else
                        {
                            mDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"register Fail",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        mSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });





    }

}