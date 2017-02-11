package com.at.healthylol;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PostActivity extends AppCompatActivity {

    private ImageButton mSelectImage;
    private static final int GALLERY_REQUEST=1;
    private EditText mPostTitle;
    private EditText mPostDescription;
    private Button mSubmitBtn;


    private DatabaseReference mDatabase;

    private ProgressDialog mProgressDialog;

    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Date date = new Date();

    final String title_val=dateFormat.format(date).toString();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

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

                    newPost.child("title").setValue(title_val);
                    newPost.child("desc").setValue(desc_val);

                    mProgressDialog.dismiss();
                    startActivity(new Intent(PostActivity.this,BlogActivity.class));// after posting redirect to main page
                }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }
}
