package com.mtech.envirotrack.report;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mtech.envirotrack.R;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.HashMap;

public class EnvironmentalReport extends AppCompatActivity {

    private Spinner environmentalImpactTypeSpinner;

    private EditText excessEmissionDatePicker;
    private EditText excessEmissionTimePicker;
    private EditText incidentReportDatePicker;

    private EditText incidentReportTimePicker;

    // Image, Video and Audio upload buttons
    private ImageButton cameraButton,imageUploadButton,videoUploadButton,audioUploadButton;

    // Request codes
    private static final int REQUEST_CODE_CAPTURE_PHOTO = 1;
    private static final int REQUEST_CODE_PICK_IMAGE = 2;
    private static final int REQUEST_CODE_CAPTURE_VIDEO = 3;
    private static final int REQUEST_CODE_RECORD_AUDIO = 4;

    // firebase database
    private DatabaseReference mDatabase;

    // firebase storage
    private StorageReference storageRef;

    // firebase auth
    private FirebaseAuth mAuth;

    private LinearLayout excessEmissionDetails,spillDetails, waterQualityReportDetails, wasteManagementReportDetails, pollutionSourceReportDetails, weatherImpactReportDetails, otherReportDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_environmental_report);

        // intialize database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // intialize storage
        storageRef = FirebaseStorage.getInstance().getReference();

        // intialize auth
        mAuth = FirebaseAuth.getInstance();

        Button submitButton = findViewById(R.id.submitEnvironmentalReport);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitDataToFirebase();
            }
        });

        excessEmissionDatePicker = findViewById(R.id.ExcessEmissionDatePicker);
        excessEmissionTimePicker = findViewById(R.id.ExcessEmissionTimePicker);
        incidentReportDatePicker = findViewById(R.id.incidenReportDatePicker);
        incidentReportTimePicker = findViewById(R.id.incidentReportTimePicker);

        excessEmissionDetails = findViewById(R.id.excessEmissionDetails);
        spillDetails = findViewById(R.id.spillDetails);
        waterQualityReportDetails = findViewById(R.id.waterQualityReportDetails);
        wasteManagementReportDetails = findViewById(R.id.wasteManagementReportDetails);
        pollutionSourceReportDetails = findViewById(R.id.pollutionSourceReportDetails);
        weatherImpactReportDetails = findViewById(R.id.weatherImpactReportDetails);
        otherReportDetails = findViewById(R.id.otherReporttDetails);

        environmentalImpactTypeSpinner = findViewById(R.id.environmentalImpactTypeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.environmentalImpactType, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        environmentalImpactTypeSpinner.setAdapter(adapter);
        environmentalImpactTypeSpinner.setSelection(0, false); // This will prevent automatic selection
        environmentalImpactTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Hide all layouts
                excessEmissionDetails.setVisibility(View.GONE);
                spillDetails.setVisibility(View.GONE);
                waterQualityReportDetails.setVisibility(View.GONE);
                wasteManagementReportDetails.setVisibility(View.GONE);
                pollutionSourceReportDetails.setVisibility(View.GONE);
                weatherImpactReportDetails.setVisibility(View.GONE);
                otherReportDetails.setVisibility(View.GONE);

                switch (position) {
                    case 0:
                        // Do nothing
                        break;
                    case 1: // Excess Emission
                        excessEmissionDetails.setVisibility(View.VISIBLE);
                        setupAttachmentButtons(excessEmissionDetails);
                        break;
                    case 2: // Spill
                        // Make the Spill layout visible
                        spillDetails.setVisibility(View.VISIBLE);
                        setupAttachmentButtons(spillDetails);
                        break;
                    case 3: // Water Quality Incident
                        // Make the Water Quality Incident layout visible
                        waterQualityReportDetails.setVisibility(View.VISIBLE);
                        setupAttachmentButtons(waterQualityReportDetails);
                        break;
                    case 4: // Waste Management Incident
                        // Make the Waste Management Incident layout visible
                        wasteManagementReportDetails.setVisibility(View.VISIBLE);
                        setupAttachmentButtons(wasteManagementReportDetails);
                        break;
                    case 5: // Pollution Source Incident
                        // Make the Pollution Source Incident layout visible
                        pollutionSourceReportDetails.setVisibility(View.VISIBLE);
                        setupAttachmentButtons(pollutionSourceReportDetails);
                        break;
                    case 6: // Weather Impact Incident
                        // Make the Weather Impact Incident layout visible
                        weatherImpactReportDetails.setVisibility(View.VISIBLE);
                        setupAttachmentButtons(weatherImpactReportDetails);
                        break;
                    case 7: // Other
                        // Make the Other Incident layout visible
                        otherReportDetails.setVisibility(View.VISIBLE);
                        setupAttachmentButtons(otherReportDetails);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        excessEmissionDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        excessEmissionTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);
            }
        });

        incidentReportDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        incidentReportTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);
            }
        });


    }

    private void setupAttachmentButtons(LinearLayout layout) {
        cameraButton = layout.findViewById(R.id.camera_button);
        imageUploadButton = layout.findViewById(R.id.image_upload_button);
        videoUploadButton = layout.findViewById(R.id.video_upload_button);
        audioUploadButton = layout.findViewById(R.id.audio_upload_button);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_CODE_CAPTURE_PHOTO);
                }
            }
        });

        imageUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (pickImageIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(pickImageIntent, REQUEST_CODE_PICK_IMAGE);
                }
            }
        });

        videoUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent captureVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                if (captureVideoIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(captureVideoIntent, REQUEST_CODE_CAPTURE_VIDEO);
                }
            }
        });

        audioUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent recordAudioIntent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                if (recordAudioIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(recordAudioIntent, REQUEST_CODE_RECORD_AUDIO);
                }
            }
        });
    }

    public void showDatePickerDialog(View v) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Set the selected date to the EditText
                        EditText datePicker = findViewById(R.id.incidenReportDatePicker);
                        datePicker.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    public void showTimePickerDialog(View v) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Set the selected time to the EditText
                        EditText timePicker = findViewById(R.id.incidentReportTimePicker);
                        timePicker.setText(hourOfDay + ":" + minute);
                    }
                }, hour, minute, false);
        timePickerDialog.show();
    }

    private void submitDataToFirebase() {
        // Get the current user's ID
        String userId = mAuth.getCurrentUser().getUid();

        // Get the selected environmental impact type
        String impactType = environmentalImpactTypeSpinner.getSelectedItem().toString();

        // Initialize the Report object
        Report report = null;

        // Get the root layout
        LinearLayout rootLayout = findViewById(R.id.environmentalmpactReportLayout);
        // Traverse the view hierarchy and collect data from all EditText fields
        HashMap<String, String> data = new HashMap<>();
        for (int i = 0; i < rootLayout.getChildCount(); i++) {
            View child = rootLayout.getChildAt(i);
            if (child instanceof LinearLayout) {
                LinearLayout childLayout = (LinearLayout) child;
                for (int j = 0; j < childLayout.getChildCount(); j++) {
                    View grandChild = childLayout.getChildAt(j);
                    if (grandChild instanceof EditText) {
                        String text = ((EditText) grandChild).getText().toString();
                        if (grandChild.getId() != View.NO_ID) { // Check if the view has an ID
                            data.put(grandChild.getResources().getResourceEntryName(grandChild.getId()), text);
                        }
                    }
                }
            }
        }

        // Create a new Report object
        report = new Report(impactType, data);

        // Submit the report to Firebase
        // Submit the report to Firebase
        if (report != null) {
            String key = mDatabase.child("users").child(userId).child("reports").push().getKey();
            mDatabase.child("users").child(userId).child("reports").child(key).setValue(report).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        // Data was written successfully to the database
                        Log.d("Firebase", "Data write successful");
                        Toast.makeText(getApplicationContext(), "Report submitted successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        // Write failed
                        Log.d("Firebase", "Data write failed", task.getException());
                        Toast.makeText(getApplicationContext(), "Report submission failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Uri fileUri = null;
            String storagePath = "";
            switch (requestCode) {
                case REQUEST_CODE_CAPTURE_PHOTO:
                    // Handle captured photo
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");

                    // Convert the Bitmap to a ByteArrayOutputStream
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imageData = baos.toByteArray();

                    // Create a reference to 'images/captured.jpg'
                    storagePath = "images/captured.jpg";

                    // Upload the image to Firebase Storage
                    uploadFileToFirebaseStorage(imageData, storagePath);
                    break;
                case REQUEST_CODE_PICK_IMAGE:
                    // Handle picked image
                    fileUri = data.getData();
                    storagePath = "images/picked.jpg";
                    uploadFileToFirebaseStorage(fileUri, storagePath);
                    break;
                case REQUEST_CODE_CAPTURE_VIDEO:
                    // Handle captured video
                    fileUri = data.getData();
                    storagePath = "videos/captured.mp4";
                    uploadFileToFirebaseStorage(fileUri, storagePath);
                    break;
                case REQUEST_CODE_RECORD_AUDIO:
                    // Handle recorded audio
                    fileUri = data.getData();
                    storagePath = "audios/recorded.3gp";
                    uploadFileToFirebaseStorage(fileUri, storagePath);
                    break;
            }
        }
    }

    private void uploadFileToFirebaseStorage(Uri fileUri, String storagePath) {
        // Get the current user's ID
        String userId = mAuth.getCurrentUser().getUid();

        // Create a reference to the file
        StorageReference fileRef = storageRef.child(storagePath);

        // Upload the file to Firebase Storage
        UploadTask uploadTask = fileRef.putFile(fileUri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Get the download URL of the uploaded file
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Save the download URL to your Firebase Database
                        String fileUrl = uri.toString();
                        // Save fileUrl to your Firebase Database
                        String key = mDatabase.child("users").child(userId).child("reports").push().getKey();
                        // Replace invalid characters in the storage path
                        String dbPath = storagePath.replace('.', ',');
                        mDatabase.child("users").child(userId).child("reports").child(key).child(dbPath).setValue(fileUrl);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.d("Firebase", "File upload failed", exception);
            }
        });
    }

    private void uploadFileToFirebaseStorage(byte[] fileData, String storagePath) {
        // Get the current user's ID
        String userId = mAuth.getCurrentUser().getUid();

        // Create a reference to the file
        StorageReference fileRef = storageRef.child(storagePath);

        // Upload the file to Firebase Storage
        UploadTask uploadTask = fileRef.putBytes(fileData);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Get the download URL of the uploaded file
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Save the download URL to your Firebase Database
                        String fileUrl = uri.toString();
                        // Save fileUrl to your Firebase Database
                        String key = mDatabase.child("users").child(userId).child("reports").push().getKey();
                        // Replace invalid characters in the storage path
                        String dbPath = storagePath.replace('.', ',');
                        mDatabase.child("users").child(userId).child("reports").child(key).child(dbPath).setValue(fileUrl);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.d("Firebase", "File upload failed", exception);
            }
        });
    }
}

