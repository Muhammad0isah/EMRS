package com.mtech.envirotrack.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mtech.envirotrack.MainActivity;
import com.mtech.envirotrack.R;
import com.mtech.envirotrack.report.ReportHistory;

import java.io.IOException;

public class
Profile extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

    private static final int PICK_IMAGE = 1;

    private ImageView photoUrlImageView;

    private StorageReference mStorageRef;

    private AppCompatButton reportHistoryButton;

    private Button logoutButton;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        logoutButton = findViewById(R.id.logoutButton);
        backButton = findViewById(R.id.goBackButton);
        mAuth = FirebaseAuth.getInstance();


        ScrollView rootLayout = findViewById(R.id.root_layout);

        reportHistoryButton = findViewById(R.id.button);

        photoUrlImageView = findViewById(R.id.photoUrlImageView);

        ImageButton uploadButton = findViewById(R.id.upload_button);

        // storage reference
        mStorageRef = FirebaseStorage.getInstance().getReference();

        reportHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new instance of ReportHistoryFragment
                ReportHistory reportHistoryFragment = new ReportHistory();


                // Use the FragmentManager to replace the current fragment with the ReportHistoryFragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, reportHistoryFragment)
                        .addToBackStack(null)
                        .commit();
                rootLayout.setVisibility(View.GONE);

            }
        });



        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            String phone = user.getPhoneNumber();
            if(user.getPhotoUrl() != null){
                String photoUrl = user.getPhotoUrl().toString();
                Glide.with(this)
                        .load(photoUrl)
                        .apply(RequestOptions.bitmapTransform(new PhotoCircleTransformWithBorder(this, Color.WHITE, 7)))
                        .into(photoUrlImageView);
            }
            TextView nameTextView = findViewById(R.id.nameTextView);
            TextView emailTextView = findViewById(R.id.emailTextView);
//            TextView phoneTextView = findViewById(R.id.phoneTextView);

            nameTextView.setText(name);
            emailTextView.setText(email);
//            phoneTextView.setText("Phone "+ phone);
        }

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        // Configure Google Sign In

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Firebase sign out
                mAuth.signOut();
                // Google sign out
                mGoogleSignInClient.signOut().addOnCompleteListener(Profile.this,
                            new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                startActivity(new Intent(Profile.this, MainActivity.class));
                                finish();
                            }
                });
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                photoUrlImageView.setImageBitmap(bitmap);

                // upload image to firebase storage
                uploadImageToFirebaseStorage(uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method uploads the selected image to Firebase storage.
     *
     * @param uri The Uri of the image to be uploaded.
     */
    private void uploadImageToFirebaseStorage(Uri uri) {
        // Create a ProgressDialog to show the progress of the upload
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        // Create a reference to the location where the image will be stored in Firebase Storage
        final StorageReference profileImageRef = mStorageRef.child("profilepics/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + ".jpg");

        // Start the upload of the image to Firebase Storage
        profileImageRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Dismiss the ProgressDialog when the upload is successful
                        progressDialog.dismiss();
                        // Show a toast message to inform the user that the upload was successful
                        Toast.makeText(Profile.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        // Get the download URL of the uploaded image
                        profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Get the current user
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                // Create a UserProfileChangeRequest to update the user's profile picture
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setPhotoUri(uri)
                                        .build();

                                // Update the user's profile picture
                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    // Show a toast message to inform the user that the profile picture was updated
                                                    Toast.makeText(Profile.this, "Profile image updated", Toast.LENGTH_SHORT).show();
                                                    // Refresh the activity to show the updated profile picture
                                                    finish();
                                                    startActivity(getIntent());
                                                }
                                            }
                                        });
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Dismiss the ProgressDialog when the upload fails
                        progressDialog.dismiss();
                        // Show a toast message to inform the user of the error
                        Toast.makeText(Profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        // Calculate the progress of the upload
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        // Update the message of the ProgressDialog to show the progress
                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                    }
                });
    }
}