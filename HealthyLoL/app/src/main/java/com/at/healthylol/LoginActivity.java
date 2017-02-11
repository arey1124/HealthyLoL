package com.at.healthylol;

        import android.accounts.Account;
        import android.content.Intent;
        import android.os.Bundle;
        import android.preference.PreferenceManager;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.support.v7.app.AppCompatActivity;
        import android.text.TextUtils;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.AuthResult;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.ChildEventListener;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

/**
 * Created by Anirudh Trigunayat on 20-01-2017.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPass;
    private Button mLoginBtn;
    private Button mSignUpBtn;
    private DatabaseReference mDatabase;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        mAuth=FirebaseAuth.getInstance();

        mEmail=(EditText)findViewById(R.id.email);
        mPass=(EditText)findViewById(R.id.pass);
        mLoginBtn=(Button)findViewById(R.id.login);

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users");

        mAuthListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser()!=null){

                    FirebaseUser user=mAuth.getCurrentUser();

                    //String a=user.getDisplayName();
                    try {
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("User", user.getDisplayName()).commit();
                        String a = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("User", null);
                        if (a.equalsIgnoreCase("Samuel Wilson")) {
                            Intent i = new Intent(LoginActivity.this, DocDashboard.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        } else {
                            Intent i = new Intent(LoginActivity.this, Dashboard.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }
                        // Toast.makeText(LoginActivity.this, ""+user.getDisplayName(), Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e){

                    }

                }

            }
        };

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSignIn();

            }
        });

    }



    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    private void startSignIn(){

        String email=mEmail.getText().toString();
        String pass=mPass.getText().toString();

        if(TextUtils.isEmpty(email)|| TextUtils.isEmpty(pass)) {

            Toast.makeText(LoginActivity.this, "Fields are empty!", Toast.LENGTH_LONG).show();
        }
        else
        {
            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (!task.isSuccessful()) {

                        Toast.makeText(LoginActivity.this, "Sign in Problem", Toast.LENGTH_LONG).show();

                    }
                    else
                    {
                        checkUserExist();
                    }

                }
            });
        }
    }

    private void checkUserExist() {



    }
}
