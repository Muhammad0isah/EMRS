package com.mtech.envirotrack.report;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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
import com.mtech.envirotrack.MainActivity;
import com.mtech.envirotrack.MyApplication;
import com.mtech.envirotrack.R;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class EnvironmentalReport extends AppCompatActivity {

    private Spinner environmentalImpactTypeSpinner;

    private EditText excessEmissionDatePicker;
    private EditText excessEmissionTimePicker;
    private EditText incidentReportDatePicker;
    private EditText environmentalReportNumber;

    private  EditText environmentalmpactLocation;

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
    private String userId;
    private Uri fileUri;
    private String mediaType;
    private Button getLocation;

    private String reportKey = "";

    private ImageButton goBackButton;



    private LinearLayout excessEmissionDetails,spillDetails, waterQualityReportDetails, wasteManagementReportDetails, pollutionSourceReportDetails, weatherImpactReportDetails, otherReportDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_environmental_report);

        // intialize database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // intialize storage
        storageRef = FirebaseStorage.getInstance().getReference();

        // Get the current user's ID
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        // intialize auth
        mAuth = FirebaseAuth.getInstance();
        excessEmissionDatePicker = findViewById(R.id.ExcessEmissionDatePicker);
        excessEmissionTimePicker = findViewById(R.id.ExcessEmissionTimePicker);
        incidentReportDatePicker = findViewById(R.id.incidenReportDatePicker);
        incidentReportTimePicker = findViewById(R.id.incidentReportTimePicker);
        environmentalReportNumber = findViewById(R.id.environmentalmpactReportNumber);
        environmentalmpactLocation = findViewById(R.id.environmentalmpactLocation);
        getLocation = findViewById(R.id.getLocation);

        excessEmissionDetails = findViewById(R.id.excessEmissionDetails);
        spillDetails = findViewById(R.id.spillDetails);
        waterQualityReportDetails = findViewById(R.id.waterQualityReportDetails);
        wasteManagementReportDetails = findViewById(R.id.wasteManagementReportDetails);
        pollutionSourceReportDetails = findViewById(R.id.pollutionSourceReportDetails);
        weatherImpactReportDetails = findViewById(R.id.weatherImpactReportDetails);
        otherReportDetails = findViewById(R.id.otherReporttDetails);


        // Get the current date and time

        Date currentDate = new Date();

        // Create a SimpleDateFormat object with the desired format
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyy-HHmmss", Locale.getDefault());

        // Format the current date
        String formattedDate = formatter.format(currentDate);

        // Generate the unique number
        String uniqueNumber = "EI-" + formattedDate;

        // Set the text of the EditText
        environmentalReportNumber.setText(uniqueNumber);
        environmentalReportNumber.setEnabled(false);
        environmentalReportNumber.setTextColor(getResources().getColor(R.color.black));


        Button submitButton = findViewById(R.id.submitEnvironmentalReport);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Generate the reportKey here
                reportKey = mDatabase.child("users").child(userId).child("reports").push().getKey();
                submitDataToFirebase();
            }
        });

        goBackButton = findViewById(R.id.goBackButton);
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(EnvironmentalReport.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                    FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(EnvironmentalReport.this);
                    fusedLocationProviderClient.getLastLocation().addOnSuccessListener(EnvironmentalReport.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Update the UI with the location details
                            Geocoder geocoder = new Geocoder(EnvironmentalReport.this);
                            try {
                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                if (!addresses.isEmpty()) {
                                    Address address = addresses.get(0);
                                    environmentalmpactLocation.setText(address.getAddressLine(0));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

            }
        });

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
                        EditText datePicker1 = findViewById(R.id.incidenReportDatePicker);
                        datePicker1.setText(dayOfMonth + "/" + (month + 1) + "/" + year);

                        EditText datePicker2 = findViewById(R.id.ExcessEmissionDatePicker);
                        datePicker2.setText(dayOfMonth + "/" + (month + 1) + "/" + year);

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

                        EditText timePicker2 = findViewById(R.id.ExcessEmissionTimePicker);
                        timePicker2.setText(hourOfDay + ":" + minute);
                    }
                }, hour, minute, false);
        timePickerDialog.show();
    }

    private void submitDataToFirebase() {
        // Get the current user's ID
        String userId = mAuth.getCurrentUser().getUid();

        // Get the current user's email and name
        String userEmail = mAuth.getCurrentUser().getEmail();
        String userName = mAuth.getCurrentUser().getDisplayName();
        String reportNumber = environmentalReportNumber.getText().toString();

        // Get the selected environmental impact type
        String impactType = environmentalImpactTypeSpinner.getSelectedItem().toString();

        // Initialize the UserReport object
        UserReport userReport = null;

        // Get the root layout
        LinearLayout rootLayout = findViewById(R.id.root_layout);

        // Traverse the view hierarchy and collect data from all EditText fields
        HashMap<String, String> data = new HashMap<>();
        collectDataFromEditTexts(rootLayout, data);

        // Create a new UserReport object
        userReport = new UserReport(reportNumber, userName, userEmail, impactType, data);

        // Submit the userReport to Firebase
        if (userReport != null) {
            mDatabase.child("users").child(userId).child("reports").child(reportKey).setValue(userReport).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        // Data was written successfully to the database
                        Log.d("Firebase", "Data write successful");
                        Toast.makeText(getApplicationContext(), "UserReport submitted successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        // Write failed
                        Log.d("Firebase", "Data write failed", task.getException());
                        Toast.makeText(getApplicationContext(), "UserReport submission failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            // Pass the reportKey to the uploadFileToFirebaseStorage methods
            uploadFileToFirebaseStorage(fileUri, mediaType, reportKey);
        }
    }
    private void collectDataFromEditTexts(View view, HashMap<String, String> data) {
        if (view instanceof EditText) {
            // Check if the EditText is not environmentalReportNumber
            if (view.getId() != R.id.environmentalmpactReportNumber) {
                String text = ((EditText) view).getText().toString();
                if (view.getId() != View.NO_ID && text != null && !text.isEmpty()) {
                    data.put(view.getResources().getResourceEntryName(view.getId()), text);
                }
            }
        } else if (view instanceof CheckBox) {
            CheckBox checkBox = (CheckBox) view;
            if (checkBox.isChecked()) {
                data.put(view.getResources().getResourceEntryName(view.getId()), "checked");
            }
        } else if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                collectDataFromEditTexts(viewGroup.getChildAt(i), data);
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            // Initialize fileUri and mediaType here
            fileUri = null;
            mediaType = "";
            switch (requestCode) {
                case REQUEST_CODE_CAPTURE_PHOTO:
                    // Handle captured photo
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");

                    // Convert the Bitmap to a ByteArrayOutputStream
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imageData = baos.toByteArray();

                    mediaType = "image";

                    // Upload the image to Firebase Storage
                    uploadFileToFirebaseStorage(imageData, mediaType, reportKey);
                    break;
                case REQUEST_CODE_PICK_IMAGE:
                    // Handle picked image
                    mediaType = "image";

                    fileUri = data.getData();
                    uploadFileToFirebaseStorage(fileUri, mediaType, reportKey);
                    break;
                case REQUEST_CODE_CAPTURE_VIDEO:
                    // Handle captured video
                    mediaType = "video";
                    fileUri = data.getData();
                    uploadFileToFirebaseStorage(fileUri, mediaType, reportKey);
                    break;
                case REQUEST_CODE_RECORD_AUDIO:
                    // Handle recorded audio
                    mediaType = "audio";
                    fileUri = data.getData();
                    uploadFileToFirebaseStorage(fileUri, mediaType, reportKey);
                    break;
            }
        }
    }
    private void uploadFileToFirebaseStorage(Uri fileUri, String mediaType, String reportKey) {
        // Get the current user's ID
        String userId = mAuth.getCurrentUser().getUid();

        // Create a reference to the file
        String storagePath = "reports/" + reportKey + "/attachments/" + mediaType;
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
                        mDatabase.child("users").child(userId).child("reports").child(reportKey).child("attachments").child(mediaType).setValue(fileUrl);
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

    private void uploadFileToFirebaseStorage(byte[] fileData, String mediaType, String reportKey) {
        // Get the current user's ID
        String userId = mAuth.getCurrentUser().getUid();

        // Create a reference to the file
        String storagePath = "reports/" + reportKey + "/attachments/" + mediaType;
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
                        mDatabase.child("users").child(userId).child("reports").child(reportKey).child("attachments").child(mediaType).setValue(fileUrl);
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

