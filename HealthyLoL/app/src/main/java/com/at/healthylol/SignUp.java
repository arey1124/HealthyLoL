package com.at.healthylol;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class SignUp extends AppCompatActivity {

    private EditText mNameField;
    private EditText mEmailField;
    private EditText mPasswordField;

    private Button mRegisterBtn;
    private TextView logIn;

    private ImageView upload;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;

    private ProgressDialog mProgress;
    private static final int GALLERY_INTENT=2;
    private static final int CAMERA_REQUEST_CODE=1;

    Uri downloadUri;
    String img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        mAuth=FirebaseAuth.getInstance();

        mStorage= FirebaseStorage.getInstance().getReference();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users");

        mProgress=new ProgressDialog(this);

        mEmailField=(EditText)findViewById(R.id.email);
        mNameField=(EditText)findViewById(R.id.name);
        mPasswordField=(EditText)findViewById(R.id.password);
        mRegisterBtn=(Button)findViewById(R.id.signup_btn);
        upload=(ImageView)findViewById(R.id.upload1);
        logIn=(TextView)findViewById(R.id.loginbold_text);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadImage(SignUp.this,"Upload an image ?","Gallery","Camera");
            }
        });

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startRegister();

            }
        });
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent=new Intent(SignUp.this,LoginActivity.class);
                startActivity(mainIntent);
            }
        });
    }
    private void startRegister() {

        final String name=mNameField.getText().toString().trim();
        String email=mEmailField.getText().toString().trim();
        String password=mPasswordField.getText().toString().trim();

        if(downloadUri!=null){
            img = downloadUri.toString();
            Toast.makeText(this, ""+img, Toast.LENGTH_SHORT).show();
        }else{
            /*Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.upload);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            byte img2[] =bos.toByteArray();
            img = img2.toString();
            Toast.makeText(this, ""+img, Toast.LENGTH_SHORT).show();*/
            img="default";
        }


        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

            mProgress.setMessage("Signing Up...");
            mProgress.show();

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){


                        FirebaseUser user=mAuth.getCurrentUser();
                        /*
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(Uri.parse(img))
                                .build();

                        if (user != null) {
                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("E_", "User profile updated.");
                                            }
                                        }
                                    });
                        }
                        else
                        {
                            Log.d("E_", "Error Updating Profile!!!!.");
                        }
                        */

                        String user_id=user.getUid();
                        Toast.makeText(SignUp.this, ""+user_id, Toast.LENGTH_LONG).show();

                        DatabaseReference current_user_db= mDatabase.child(user_id);
                        current_user_db.child("name").setValue(name);
                        current_user_db.child("image").setValue(img);
                        mProgress.dismiss();

                        Intent mainIntent=new Intent(SignUp.this,Dashboard.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// can't go back
                        startActivity(mainIntent);
                    }

                }
            });

        }

    }
    private void ImageStorage() {
        Intent intent=new Intent(Intent.ACTION_PICK);

        intent.setType("image/*");
        startActivityForResult(intent,GALLERY_INTENT);
    }

    private void ImageCamera() {
        Intent intent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,CAMERA_REQUEST_CODE);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            mProgress.setMessage("Uploading ...");
            mProgress.show();
            Uri uri = data.getData();

            StorageReference filePath = mStorage.child("Photos").child(uri.getLastPathSegment());

            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(SignUp.this, "Uploaded Done", Toast.LENGTH_SHORT).show();
                    mProgress.dismiss();

                    downloadUri = taskSnapshot.getDownloadUrl();

                    Picasso.with(SignUp.this).load(downloadUri).fit().centerCrop().into(upload);
                }
            });


        }

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            //set the progress dialog
            mProgress.setMessage("Uploding image...");
            mProgress.show();

            //get the camera image
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] databaos = baos.toByteArray();

            //set the image into imageview
            upload.setImageBitmap(bitmap);
            //String img = "fire"

            //Firebase storage folder where you want to put the images
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();

            //name of the image file (add time to have different files to avoid rewrite on the same file)
            StorageReference imagesRef = storageRef.child("Photos").child("P" + new Date().getTime());
            //send this name to database
            //upload image
            UploadTask uploadTask = imagesRef.putBytes(databaos);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(SignUp.this, "Sending failed", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    downloadUri = taskSnapshot.getDownloadUrl();

                    Picasso.with(SignUp.this).load(downloadUri).fit().centerCrop().into(upload);
                    mProgress.dismiss();
                }
            });

        }
    }

    public void UploadImage(Activity activity, String msg, String msg2, String msg3){


        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.customdialogtwobtn);


        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.customdia_text);
        text.setText(msg);


        Button dialogButtonno = (Button) dialog.findViewById(R.id.custdia_btnfirst);
        dialogButtonno.setText(msg2);
        dialogButtonno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageStorage();
                dialog.dismiss();


            }
        });

        Button dialogButtonyes = (Button) dialog.findViewById(R.id.custdia_btnsec);
        dialogButtonyes.setText(msg3);
        dialogButtonyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageCamera();
                dialog.dismiss();

            }
        });

        dialog.show();

    }
}
