package com.example.aegis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private EditText LoginEmail;
    private EditText LoginPassword;
    private Button LoginLogin;
    private TextView LoginSignup;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LoginEmail=(EditText) findViewById(R.id.etLoginEmail);
        LoginPassword=(EditText) findViewById(R.id.etLoginPassword);
        LoginLogin=(Button) findViewById(R.id.btnLoginLogin);
        LoginSignup=(TextView) findViewById(R.id.tvLoginSignup);

        firebaseAuth=FirebaseAuth.getInstance();

        LoginSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
            }
        });

        LoginLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });
    }

    private void userLogin(){
        String Email=LoginEmail.getText().toString();
        String Password=LoginPassword.getText().toString();
        if(Email.isEmpty()){
            LoginEmail.setError("Email is Required");
            LoginEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            LoginEmail.setError("Please enter a valid email");
            LoginEmail.requestFocus();
            return;
        }
        if(Password.isEmpty()){
            LoginPassword.setError("Password is Required");
            LoginPassword.requestFocus();
            return;
        }
        if(Password.length()<6){
            LoginPassword.setError("Minimum 6 characters required");
            LoginPassword.requestFocus();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user= firebaseAuth.getCurrentUser();
                    if(user.isEmailVerified()){
                        Intent intent=new Intent(MainActivity.this,DashboardActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this,"Check your email to verify your acccount!",Toast.LENGTH_LONG).show();
                    }


                }
                else{
                    Toast.makeText(MainActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}