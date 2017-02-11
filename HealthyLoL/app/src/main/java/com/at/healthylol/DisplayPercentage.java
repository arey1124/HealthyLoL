package com.at.healthylol;

import android.content.Intent;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Random;

public class DisplayPercentage extends AppCompatActivity {

    TextView tx2;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseAuth mAuth;
    int idsel[] = {R.drawable.im1, R.drawable.im2, R.drawable.im3};
    String inMessage="62.4%";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_percentage);

        mAuth= FirebaseAuth.getInstance();

        Random r = new Random();

        int a = r.nextInt(2);
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null)
                {
                    Intent loginIntent=new Intent(DisplayPercentage.this,SignUp.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };

        ImageView a1 = (ImageView) findViewById(R.id.img);

        tx2=(TextView)findViewById(R.id.textView2);
        a1.setImageResource(idsel[a]);

        Log.e("log", "log");

        UpdateTask ut = new UpdateTask();

        ut.execute();

        tx2.setText(inMessage.toString()+"%");
    }


    class UpdateTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Log.e("log", "log");

            int port;
            String ip;
            Socket socket;
            String outMessage;


            //port=Integer.parseInt(args[1]);
            //ip=args[0];

            port = 25000;
            ip = "192.168.43.114";


            try {

                socket = new Socket(ip, port);
                //System.out.println("Connected to Server : "+socket.getRemoteSocketAddress());

                BufferedReader xxx = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                outMessage = "12,1,1,88,99,1,0,235,1,1,3,2,3";
                bw.write(outMessage);
                bw.newLine();
                bw.flush();

                inMessage = xxx.readLine();

                socket.close();

                Log.e("The result :\t", inMessage);




            } catch (SocketException e) {
                finish();
            } catch (IOException e) {
                finish();
            } catch (Exception e) {
                finish();

            }
            //System.out.println(tone);
            return null;
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