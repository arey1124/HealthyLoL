package com.at.healthylol;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class PatientPsychology extends AppCompatActivity {

    SQLiteDatabase db;
    private Cursor c;
    TextView result,val1;
    ContentValues cv;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_psychology);

        mAuth= FirebaseAuth.getInstance();
        result =(TextView)findViewById(R.id.userdata);
        val1=(TextView)findViewById(R.id.userval);

        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null)
                {
                    Intent loginIntent=new Intent(PatientPsychology.this,SignUp.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };

        next=(Button)findViewById(R.id.percentage);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PatientPsychology.this,DisplayPercentage.class);
                startActivity(i);
            }
        });

        db=this.openOrCreateDatabase("mydb",MODE_PRIVATE,null);



        c=db.rawQuery("Select * from Data;",null);
        if(c.moveToFirst()){
            while (c.isAfterLast()==false){
                String name = c.getString(c.getColumnIndex("Name"));
                String val =c.getString(c.getColumnIndex("Val"));
                result.append(name+"\n");
                val1.append(val+"\n");
                //c.moveToLast();
                c.moveToNext();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(item.getItemId()==R.id.action_logout)
        {
            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("User",null).commit();
            signout();
        }

        return super.onOptionsItemSelected(item);
    }
    private void signout() {
        mAuth.signOut();
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

}
