package com.example.quincy.a00994454asn1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quincy.a00994454asn1.helpers.photoManagementHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class SearchActivity extends AppCompatActivity {

    /**
     * Dummy data
     */
    //ArrayList<String> dateData = new ArrayList<String>(Arrays.asList("Mt. Fuji", "Mt. Everestt", "Disneyland", "Disneyworld"));
    private photoManagementHelper pmHelper = new photoManagementHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    public void findByCaption(View view) {
        //Initialize Target Button
        Button button = findViewById(R.id.searchConfirm);

        //Initialize result text box
        TextView resultText = findViewById(R.id.resultText);

        //Initialize request text box
        TextView requestText = findViewById(R.id.searchBox);

        //Initialize image area
        final ImageView mImageView = findViewById(R.id.photoFound);

        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        final File[] files = dir.listFiles();

        ArrayList<Pair<String, String>> matchedFiles = new ArrayList<Pair<String, String>>();

        String request = requestText.getText().toString();
        for (File file : files) {
            try {
                String filePath = file.getAbsolutePath();
                ExifInterface existInterface = new ExifInterface(filePath);
                String description = existInterface.getAttribute(ExifInterface.TAG_IMAGE_DESCRIPTION);

                if (description.equalsIgnoreCase(request)) {
                    matchedFiles.add(new Pair<String, String>(filePath, description));
                }
            } catch (Exception E) {
                System.out.println("Could not get metadata");
                continue;
            }
        }
        //Get requested text and see if it's in data, return something by result
        if (matchedFiles.size() > 0) {
            resultText.setText("Found something");
            //setPic(matchedFiles.get(0).first);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//            requestText.clearFocus();
        } else {
            resultText.setText("None found");
        }
    }

//    private void setPic(String mCurrentPhotoPath) {
//        ImageView mImageView = findViewById(R.id.photoFound);
//        // Get the dimensions of the View
//        int targetW = mImageView.getWidth();
//        int targetH = mImageView.getHeight();
//
//        // Get the dimensions of the bitmap
//        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//        bmOptions.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
//        int photoW = bmOptions.outWidth;
//        int photoH = bmOptions.outHeight;
//
//        // Determine how much to scale down the image
//        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
//
//        // Decode the image file into a Bitmap sized to fill the View
//        bmOptions.inJustDecodeBounds = false;
//        bmOptions.inSampleSize = scaleFactor;
//        bmOptions.inPurgeable = true;
//
//        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
//        bitmap = RotateBitmap(bitmap, 90);
//        mImageView.setImageBitmap(bitmap);
//    }

    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public void endSearch(View view) {
        finish();
    }
}
