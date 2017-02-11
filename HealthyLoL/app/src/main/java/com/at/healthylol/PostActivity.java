package com.at.healthylol;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class PostActivity extends AppCompatActivity {

    private ImageButton mSelectImage;
    private static final int GALLERY_REQUEST=1;
    private EditText mPostTitle;
    private EditText mPostDescription;
    private Button mSubmitBtn;
    String result;

    private DatabaseReference mDatabase;

    SQLiteDatabase db;
    private Cursor c;
    ContentValues cv;

    private ProgressDialog mProgressDialog;

    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Date date = new Date();

    final String title_val=dateFormat.format(date).toString();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        db=this.openOrCreateDatabase("mydb",MODE_PRIVATE,null);
        db.execSQL("create table if not exists Data(Name varchar(50),Val varchar(50));");

        mProgressDialog=new ProgressDialog(this);

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Blog");  //JSON TYPE STORAGE

        mPostTitle=(EditText)findViewById(R.id.titleField);

        mPostTitle.setText(title_val);
        mPostDescription=(EditText)findViewById(R.id.descriptionField);

        mSubmitBtn=(Button)findViewById(R.id.submitBtn);



        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosting();
            }
        });
    }

    private void startPosting() {

        mProgressDialog.setMessage("Posting to Blog...");



        final String desc_val=mPostDescription.getText().toString().trim();

        if(!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(title_val) )
        {

            mProgressDialog.show();
            //StorageReference filepath=mStorage.child("Blog_Images").child(mResultUri.getLastPathSegment());// can search for random name generator for android


                    DatabaseReference newPost=mDatabase.push();//push means, it creates a uid everytime
                    result=desc_val;
                    newPost.child("title").setValue(title_val);
                    newPost.child("desc").setValue(desc_val);
                    UpdateTask a=new UpdateTask();
                    a.execute(result);

                    mProgressDialog.dismiss();
                    startActivity(new Intent(PostActivity.this,BlogActivity.class));// after posting redirect to main page
                }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    class UpdateTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {

            ToneAnalyzer service = new ToneAnalyzer(ToneAnalyzer.VERSION_DATE_2016_05_19);
            service.setUsernameAndPassword("51db7ecd-ad14-43ff-818e-945c1a98ec42", "RfhDLxdwM1Ys");
            ToneAnalysis tone = service.getTone(params[0], null).execute();
            Log.e("E_",tone.toString());

            result=tone.toString();

            try {
                JSONObject o = new JSONObject(result);
                JSONObject document_tone = o.getJSONObject("document_tone");

                JSONArray tone_categories = document_tone.getJSONArray("tone_categories");
                JSONArray tones = tone_categories.getJSONObject(0).getJSONArray("tones");

                for(int i=0;i<tones.length();i++)
                {
                    JSONObject temp=tones.getJSONObject(i);
                    String tone_name=temp.getString("tone_name");
                    String score=temp.getString("score");
                    Log.e(tone_name,score);
                    cv=new ContentValues();
                    cv.put("Name",tone_name);
                    cv.put("Val",score);
                    db.insert("Data",null,cv);
                }

                tones = tone_categories.getJSONObject(1).getJSONArray("tones");

                for(int i=0;i<tones.length();i++)
                {
                    JSONObject temp=tones.getJSONObject(i);
                    String tone_name=temp.getString("tone_name");
                    String score=temp.getString("score");
                    Log.e(tone_name,score);
                    cv=new ContentValues();
                    cv.put("Name",tone_name);
                    cv.put("Val",score);
                    db.insert("Data",null,cv);
                }
                tones = tone_categories.getJSONObject(2).getJSONArray("tones");
                for(int i=0;i<tones.length();i++)
                {
                    JSONObject temp=tones.getJSONObject(i);
                    String tone_name=temp.getString("tone_name");
                    String score=temp.getString("score");
                    Log.e(tone_name,score);
                    cv=new ContentValues();
                    cv.put("Name",tone_name);
                    cv.put("Val",score);
                    db.insert("Data",null,cv);
                }








                //Log.e("gg",sds);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            //System.out.println(tone);
            return null;
        }
    }
}
