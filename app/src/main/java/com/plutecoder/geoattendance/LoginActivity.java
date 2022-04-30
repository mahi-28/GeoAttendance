package com.plutecoder.geoattendance;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import settergetter.UserGtSt;
import utils.PrefHelperOffline;
import utils.PreferenceHelper;
import utils.Typefaces;
import utils.Utility;
import utils.Utils;

import static android.text.TextUtils.isEmpty;

public class LoginActivity extends AppCompatActivity {
    Button btnForgotPass, btnLogin;
    EditText EdtEmail, EdtPass;
    ProgressBar pb;
    public static Activity activity;
    TextView txtsignup, txtwelcome, txtsignintext;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        mAuth = FirebaseAuth.getInstance();
        btnForgotPass = (Button) findViewById(R.id.forgotBtn);
        txtsignup = (TextView) findViewById(R.id.textView23);
        txtwelcome = (TextView) findViewById(R.id.textView);
        txtsignintext = (TextView) findViewById(R.id.txtsmall);
        String text = "<font color=#a8a6a6>Don't have an account? </font> <font color=#5A5A5A>Sign up</font>";
        txtsignup.setText(Html.fromHtml(text));
        btnLogin = (Button) findViewById(R.id.loginBtn);
        EdtEmail = (EditText) findViewById(R.id.input_email);
        EdtPass = (EditText) findViewById(R.id.input_password);
        EdtPass.setTypeface(Typeface.DEFAULT);
        EdtPass.setTransformationMethod(new PasswordTransformationMethod());
        pb = (ProgressBar) findViewById(R.id.LoginProgress);
        TextView textView2 = (TextView) findViewById(R.id.textView);
        try {
            if (Typefaces.get(LoginActivity.this, "HelveticaNeue Light") != null) {
                btnLogin.setTypeface(Typefaces.get(LoginActivity.this, "HelveticaNeue Light"));
                EdtEmail.setTypeface(Typefaces.get(LoginActivity.this, "HelveticaNeue Light"));
                EdtPass.setTypeface(Typefaces.get(LoginActivity.this, "HelveticaNeue Light"));
                btnForgotPass.setTypeface(Typefaces.get(LoginActivity.this, "HelveticaNeueLt"));
                txtsignup.setTypeface(Typefaces.get(LoginActivity.this, "HelveticaNeue Light"));
//
//                txtwelcome.setTypeface(Typefaces.get(LoginActivity.this, "HelveticaNeueBd"));
//                        txtsignintext.setTypeface(Typefaces.get(LoginActivity.this, "HelveticaNeue Medium"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        txtsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(i);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!isEmpty(EdtEmail.getText().toString()) && !isEmpty(EdtPass.getText().toString())) {
                        String username = EdtEmail.getText().toString();
                        String userpin = EdtPass.getText().toString();
                            AnnonmSignup(username, userpin);

                    } else {
                        Toast.makeText(LoginActivity.this, "Please enter username and pin", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        try{
            Utility.checkPermission(LoginActivity.this);
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void AnnonmSignup(final String username, final String pin) {
        pb.setVisibility(View.VISIBLE);
        try {
            mAuth.signInAnonymously().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pb.setVisibility(View.INVISIBLE);

                }
            }).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //  Log.d(TAG, "OnComplete : " +task.isSuccessful());
                    try {
                        if (!task.isSuccessful()) {
                            pb.setVisibility(View.INVISIBLE);
                            //  Log.w(TAG, "Failed : ", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        } else {
                            final FirebaseUser firebaseUser = task.getResult().getUser();
                            System.out.println("firebaseUser------" + firebaseUser.getUid());

                            IfuserAlreadyExist(username, pin);
                        }
                    } catch (Exception e) {
                        pb.setVisibility(View.INVISIBLE);
                        e.printStackTrace();
                    }


                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void IfuserAlreadyExist(final String username, final String pin) {
        try {
            pb.setVisibility(View.VISIBLE);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

            Query query = reference.child("users").orderByChild("username").equalTo(username);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    try {
                        System.out.println("----username-----"+username);
                        System.out.println("---------"+dataSnapshot.toString());
                        if (dataSnapshot.exists())
                        {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                // UserGtSt user = snapshot.getValue(UserGtSt.class);
                                pb.setVisibility(View.INVISIBLE);
                                JSONArray jsonObject = new JSONArray();
                                HashMap<String, Object> yourData = (HashMap<String, Object>) snapshot.getValue();
                                if (yourData != null) {
                                    String retrivedpin = Utils.fromBase64(yourData.get("pin").toString());
                                    if (retrivedpin != null && !retrivedpin.equals("") && retrivedpin.equals(pin)) {
                                        try {
                                            //Profile created successfully
                                            try {
                                                if (jsonObject != null && jsonObject.length() > 0) {
                                                    PrefHelperOffline.storefuids(LoginActivity.this, jsonObject.toString());
                                                }
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                            PreferenceHelper.storeuseruid(LoginActivity.this, snapshot.getKey());
                                            PreferenceHelper.storename(LoginActivity.this, yourData.get("name").toString());
                                            PreferenceHelper.storeusercode(LoginActivity.this, yourData.get("code").toString());
                                            PreferenceHelper.storeLoginstatus(LoginActivity.this, true);
                                            if(username.equals("admin")){
                                                Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            }else {
                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    } else {
                                        Toast.makeText(LoginActivity.this, "Wrong pin entered", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    pb.setVisibility(View.INVISIBLE);
                                }
                            }
                        } else {
                            pb.setVisibility(View.INVISIBLE);
                            FirebaseAuth.getInstance().signOut();
                            Toast.makeText(LoginActivity.this, "User not exist", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        pb.setVisibility(View.INVISIBLE);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    pb.setVisibility(View.INVISIBLE);
                }
            });
        } catch (Exception e) {
            pb.setVisibility(View.INVISIBLE);
            e.printStackTrace();
        }

    }

    private void redirectMainScreen() {
        //  Log.d(TAG, "redirectLoginScreen: redirecting to login screen.");
        try {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            LoginActivity.this.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
