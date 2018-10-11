package com.example.quincy.a00994454asn1;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.nfc.Tag;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.quincy.a00994454asn1.helpers.photoManagementHelper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class SearchActivity extends AppCompatActivity {

    private photoManagementHelper pmHelper = new photoManagementHelper();

    private DatePickerDialog.OnDateSetListener mDateSetListener;


    //Date filter
    private EditText startDate;

    private EditText endDate;

    //Coordinate filter
    private EditText topLeftLat;

    private EditText topLeftLong;

    private EditText botRightLat;

    private EditText botRightLong;

    private String TAG = "SearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        startDate = findViewById(R.id.startDate);
        endDate = findViewById(R.id.endDate);
        topLeftLat = findViewById(R.id.topLeftLat);
        topLeftLong = findViewById(R.id.topLeftLong);
        botRightLat = findViewById(R.id.BotRightLat);
        botRightLong = findViewById(R.id.BotRightLong);
    }

    public void startSelect(View view) {

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = year + ":" + month + ":" + dayOfMonth;
                startDate.setText(date);
            }
        };

        DatePickerDialog dialog = new DatePickerDialog(
                SearchActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener, year, month, day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();


    }

    public void endSelect(View view) {

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = year + ":" + month + ":" + dayOfMonth;
                endDate.setText(date);
            }
        };

        DatePickerDialog dialog = new DatePickerDialog(
                SearchActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener, year, month, day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();


    }

    public void searchByFilter(View view) {
        //Initialize Target Button
        Button button = findViewById(R.id.searchConfirm);

        //Initialize request text box
        TextView requestText = findViewById(R.id.searchBox);

        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        final File[] files = dir.listFiles();

        //Radio Button
        RadioButton captionBut = findViewById(R.id.captionRadio);
        RadioButton dateBut = findViewById(R.id.dateRadio);
        RadioButton locaBut = findViewById(R.id.searchArea);

        //Path + Description
        ArrayList<String> matchedFiles = new ArrayList<String>();
        if (captionBut.isChecked()) {
            matchedFiles.clear();
            String request = requestText.getText().toString();
            for (File file : files) {
                try {
                    String filePath = file.getAbsolutePath();
                    ExifInterface existInterface = new ExifInterface(filePath);
                    String description = existInterface.getAttribute(ExifInterface.TAG_IMAGE_DESCRIPTION);

                    if (request.isEmpty()) {
                        Log.d(TAG, "searchByFiler: " + "request was empty");
                    } else {
                        if (description.equalsIgnoreCase(request)) {
                            matchedFiles.add(filePath);
                        }
                    }
                } catch (Exception E) {
                    System.out.println("Could not get metadata");
                }
            }
        //if date was chosen
        } else if (dateBut.isChecked()) {
            matchedFiles.clear();
            for (File file : files) {
                try {
                    String filePath = file.getAbsolutePath();
                    ExifInterface existInterface = new ExifInterface(filePath);
                    String dateString = existInterface.getAttribute(ExifInterface.TAG_DATETIME);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd");

                    //read date from file
                    Date fileDate = dateFormat.parse(dateString);

                    String startDateRequest = startDate.getText().toString();
                    String endDateRequest = endDate.getText().toString();

                    if (startDateRequest.isEmpty() || endDateRequest.isEmpty()) {
                        Log.d(TAG, "searchByFiler: " + "request was empty");
                    } else {
                        Date startDateQuery = dateFormat.parse(startDateRequest);
                        Date endDateQuery = dateFormat.parse(endDateRequest);
                        if (startDateQuery.getTime() <= fileDate.getTime() && fileDate.getTime() <= endDateQuery.getTime()) {
                            matchedFiles.add(filePath);
                        }
                    }
                    Log.d(TAG, "searchByFilter: " + dateString);

                } catch (Exception E) {
                    System.out.println("Could not get metadata");
                }
            }
        } else if (locaBut.isChecked()) {
            matchedFiles.clear();
            for (File file : files) {
                try {
                    String filePath = file.getAbsolutePath();
                    ExifInterface existInterface = new ExifInterface(filePath);
                    String lat = existInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
                    String lng = existInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);

                    float latInDegree = convertToDegree(lat);
                    float longInDegree = convertToDegree(lng);

                    //Get coords from text
                    if (topLeftLat.getText().toString().isEmpty() ||
                            topLeftLong.getText().toString().isEmpty() ||
                            botRightLat.getText().toString().isEmpty() ||
                            botRightLong.getText().toString().isEmpty()) {
                        Log.d(TAG, "Empty");
                    } else {
                        float topLeftValueX = Float.parseFloat(topLeftLat.getText().toString());
                        float topLeftValueY = Float.parseFloat(topLeftLong.getText().toString());
                        float botLeftValueX = Float.parseFloat(botRightLat.getText().toString());
                        float botLeftValueY = Float.parseFloat(botRightLong.getText().toString());

                    //Compare Coordinates
                        if (topLeftValueX <= latInDegree && latInDegree <= botLeftValueX) {
                            if (topLeftValueY <= longInDegree && longInDegree <= botLeftValueY) {
                                matchedFiles.add(filePath);
                            }
                        }
                        Log.d(TAG, "searchByFilter: " + "coordinates");
                    }
                } catch (Exception E) {
                    System.out.println("Could not get metadata");
                }
            }
        }
        //Get requested text and see if it's in data, return something by result
        if (matchedFiles.size() > 0) {
            Intent intent = new Intent(this, MainActivity.class);
            Bundle extra = new Bundle();
            extra.putSerializable("objects",matchedFiles);
            intent.putExtra("filterData", extra);
            startActivity(intent);
        } else {
            //resultText.setText("None found");
        }
    }

    public void endSearch(View view) {
        finish();
    }


    private Float convertToDegree(String stringDMS){
        Float result = null;
        String[] DMS = stringDMS.split(",", 3);

        String[] stringD = DMS[0].split("/", 2);
        Double D0 = new Double(stringD[0]);
        Double D1 = new Double(stringD[1]);
        Double FloatD = D0/D1;

        String[] stringM = DMS[1].split("/", 2);
        Double M0 = new Double(stringM[0]);
        Double M1 = new Double(stringM[1]);
        Double FloatM = M0/M1;

        String[] stringS = DMS[2].split("/", 2);
        Double S0 = new Double(stringS[0]);
        Double S1 = new Double(stringS[1]);
        Double FloatS = S0/S1;

        result = new Float(FloatD + (FloatM/60) + (FloatS/3600));

        return result;

    };
}
