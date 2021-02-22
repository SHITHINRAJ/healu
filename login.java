package com.example.healyou;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity
{
    private TextView heal1,forgottextlink;
    private EditText mailid,password;
    private Button sigin,textreg;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_reg);

        mFirebaseAuth = FirebaseAuth.getInstance();
        heal1 = (TextView) findViewById(R.id.txtheal1);
        textreg = (Button) findViewById(R.id.txtreg);
        mailid = (EditText) findViewById(R.id.edtlogin);
        password = (EditText) findViewById(R.id.editTextTextPassword);
        sigin = (Button)findViewById(R.id.btnlogin);
        forgottextlink = (TextView)findViewById(R.id.txtfrgtpwd);


        mAuthStateListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if (mFirebaseUser != null)
                {
                    Toast.makeText(Login.this,"Your are logged in",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Login.this,Home.class);
                    startActivity(i);
            }
                else {
                    Toast.makeText(Login.this,"Please Login",Toast.LENGTH_SHORT).show();

                }
        }


        };
        sigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = mailid.getText().toString();
                String pwd = password.getText().toString();
                if(mail.isEmpty())
                {
                    mailid.setError("Please enter mail id");
                    mailid.requestFocus();
                }
                else if(pwd.isEmpty())
                {
                    password.setError("Please enter your password");
                    password.requestFocus();
                }
                else if(mail.isEmpty() && pwd.isEmpty())
                {
                    Toast.makeText(Login.this, "Fields Are Empty!",Toast.LENGTH_SHORT).show();
                }
                else if (!(mail.isEmpty()&&pwd.isEmpty()))
                {
                    mFirebaseAuth.signInWithEmailAndPassword(mail,pwd).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(Login.this, "Login Error, Please Login Again",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Intent intToHome = new Intent(Login.this,Home.class);
                                startActivity(intToHome);
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(Login.this, "Error Ocurred!",Toast.LENGTH_SHORT).show();
                }

            }
        });

        textreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });

        forgottextlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Paswword ?");
                passwordResetDialog.setMessage("Enter Your Email Id to Receive Reset Link");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        String mail = resetMail.getText().toString();
                        mFirebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this,"Reset link has been send to your Mail id",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this,"Error Reset link has not been sent"+e.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
                            }
                        });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                    }
                });

                passwordResetDialog.create().show();
            }
        });

    }
    @Override
    protected void onStart(){
       super.onStart();
       mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}
