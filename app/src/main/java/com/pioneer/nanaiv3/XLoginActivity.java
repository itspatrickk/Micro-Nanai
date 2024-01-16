package com.pioneer.nanaiv3;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.pioneer.nanaiv3.databinding.ActivityLoginBinding;

import java.util.Calendar;

public class XLoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    DatabaseHelper databaseHelper;


    private TextView tvSelectDate;
    private EditText etSelectDate;

    // Define the pic id
    private static final int pic_id = 123;
    // Define the button and imageview type variable
    Button camera_open_id;
    ImageView click_image_id;

    Button cameraBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        databaseHelper = new DatabaseHelper(this);
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.loginEmail.getText().toString();
                String password = binding.loginPassword.getText().toString();
                if(email.equals("")||password.equals(""))
                    Toast.makeText(XLoginActivity.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                else{
                    Boolean checkCredentials = databaseHelper.checkEmailPassword(email, password);
                    if(checkCredentials == true){
                        Toast.makeText(XLoginActivity.this, "Login Successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent  = new Intent(getApplicationContext(), XSignActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(XLoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        binding.signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(XLoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        tvSelectDate = findViewById(R.id.tvSelectDate);
        etSelectDate = findViewById(R.id.etSelectDate);

        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        tvSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dialog = new DatePickerDialog(XLoginActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        month = month+1;
                        String date = dayOfMonth+"/"+month+"/"+year;
                        tvSelectDate.setText(date);

                    }
                },year, month,day);
                dialog.show();

            }
        });

        etSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dialog = new DatePickerDialog(XLoginActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        month = month+1;
                        String date = dayOfMonth+"/"+month+"/"+year;
                        etSelectDate.setText(date);

                    }
                },year, month,day);
                dialog.show();

            }
        });

        cameraBtn = findViewById(R.id.cameraBtn);
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(XLoginActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });

        // By ID we can get each component which id is assigned in XML file get Buttons and imageview.
        camera_open_id = findViewById(R.id.camera_button);
        click_image_id = findViewById(R.id.click_image);

        ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        // Add same code that you want to add in onActivityResult method
                        // There are no request codes
                        Intent data = result.getData();
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        // Set the image in imageview for display
                        if (photo != null) {
                            click_image_id.setImageBitmap(photo);
                        }
                    }
                });


        // Camera_open button is for open the camera and add the setOnClickListener in this button
        camera_open_id.setOnClickListener(v -> {
            // Create the camera_intent ACTION_IMAGE_CAPTURE it will open the camera for capture the image
            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Start the activity with camera_intent, and request pic id
//            startActivityForResult(camera_intent, pic_id);

            startActivityIntent.launch(camera_intent);
        });


    }

//    // This method will help to retrieve the image
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        // Match the request 'pic id with requestCode
//        if (requestCode == pic_id) {
//            // BitMap is data structure of image file which store the image in memory
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
//            // Set the image in imageview for display
//            click_image_id.setImageBitmap(photo);
//        }
//    }

//    save image to storage
//    ContextWrapper cw = new ContextWrapper(getApplicationContext());
//    File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
//    File file = new File(directory, "UniqueFileName" + ".jpg");
//            if (!file.exists()) {
//        Log.d("path", file.toString());
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(file);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//            fos.flush();
//            fos.close();
//        } catch (java.io.IOException e) {
//            e.printStackTrace();
//        }
//    }

}