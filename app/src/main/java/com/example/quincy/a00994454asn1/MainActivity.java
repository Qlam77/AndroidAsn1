package com.example.quincy.a00994454asn1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.media.ExifInterface;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    static final int REQUEST_TAKE_PHOTO = 1;

    static final private int ANGLE = 90;

    String mCurrentPhotoPath;

    private File storageDir;

    private int curIndex = 1;

    private File[] listOfFiles;

    private int pictureTaken = 0;

    private int pictureSet = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    }

    public void searchMode(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    public void snapMode(View view) {
        dispatchTakePictureIntent();
    }

    public void save(View view) {
        try {
            createImageFile();
        } catch (Exception e) {
            System.out.println("Issue with saving");
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.quincy.a00994454asn1",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }

            if (photoFile.length() == 0) {
                photoFile.delete();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            galleryAddPic();
            setPic(mCurrentPhotoPath);
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            listOfFiles = storageDir.listFiles();
            curIndex = listOfFiles.length - 1;
            pictureTaken = 1;
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prejfix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void setPic(String path) {
        ImageView mImageView = findViewById(R.id.photoDisplay);
        // Get the dimensions of the View
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
        bitmap = RotateBitmap(bitmap, ANGLE);
        setTime(path);
        setLocation(path);
        setCaption(path);
        mImageView.setImageBitmap(bitmap);
    }

    public void setCaption(String path) {
        //if not set picture
        TextView mEditText = findViewById(R.id.caption);
        try {
            ExifInterface existInterface = new ExifInterface(path);
            String description = existInterface.getAttribute(ExifInterface.TAG_IMAGE_DESCRIPTION);
            if (description == null) {
                existInterface.setAttribute(ExifInterface.TAG_IMAGE_DESCRIPTION, "");
                existInterface.saveAttributes();
                mEditText.setText("");
            } else {
                mEditText.setText(description);
            }
        } catch (Exception E) {
            System.out.println("Could not open file");
        }
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public void setTime(String path) {
        TextView mtextView = findViewById(R.id.timeStamp);
        try {
            ExifInterface existInterface = new ExifInterface(path);
            String time = existInterface.getAttribute(ExifInterface.TAG_DATETIME);
            mtextView.setText(time);
        } catch (Exception E) {
            System.out.println("Could not open file");
        }
    }

    public void setLocation(String path) {
        TextView mtextView = findViewById(R.id.Location);
        try {
            ExifInterface existInterface = new ExifInterface(path);
            String lat = existInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            String lng = existInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            if (lat == null || lng == null) {
                lat = "0";
                lng = "0";
            }
            mtextView.setText("Lat: " + lat + ", Long: " + lng);
        } catch (Exception E) {
            System.out.println("Could not open file");
        }
    }

    public void saveCaption(View view) {
        //if not set picture
        TextView mEditText = findViewById(R.id.caption);
        if (!mEditText.getText().toString().isEmpty()) {
            try {
                ExifInterface existInterface = new ExifInterface(listOfFiles[curIndex].getAbsolutePath());
                existInterface.setAttribute(ExifInterface.TAG_IMAGE_DESCRIPTION, mEditText.getText().toString());
                existInterface.saveAttributes();
            } catch (Exception E) {
                System.out.println("Could not open file");
            }
        }
    }

    public void prevNextSaveCaption(String path) {
        //if not set picture
        TextView mEditText = findViewById(R.id.caption);
        try {
            ExifInterface existInterface = new ExifInterface(path);
            String caption = mEditText.getText().toString();
            existInterface.setAttribute(ExifInterface.TAG_IMAGE_DESCRIPTION, caption);
            existInterface.saveAttributes();
        } catch (Exception E) {
            System.out.println("Could not open file");
        }
    }

    public void left(View view) {
        listOfFiles = storageDir.listFiles();
        if (listOfFiles.length != 0) {
            if (pictureTaken == 1) {
                prevNextSaveCaption(mCurrentPhotoPath);
                pictureTaken = 0;
            } else if (pictureSet == 1) {
                prevNextSaveCaption(listOfFiles[curIndex].getAbsolutePath());
            }
            curIndex = curIndex - 1;
            if(curIndex < 0) {
                curIndex = listOfFiles.length - 1;
            }
            setTime(listOfFiles[curIndex].getAbsolutePath());
            setPic(listOfFiles[curIndex].getAbsolutePath());
            setLocation(listOfFiles[curIndex].getAbsolutePath());
            setCaption(listOfFiles[curIndex].getAbsolutePath());
            pictureSet = 1;
        }
    }

    public void right(View view){
        listOfFiles = storageDir.listFiles();
        if (listOfFiles.length != 0) {
            if (pictureTaken == 1) {
                prevNextSaveCaption(mCurrentPhotoPath);
                pictureTaken = 0;
            } else if (pictureSet == 1) {
                prevNextSaveCaption(listOfFiles[curIndex].getAbsolutePath());
                pictureSet = 0;
            }
            curIndex = curIndex + 1;
            if (curIndex >= listOfFiles.length) {
                curIndex = 0;
            }
            setTime(listOfFiles[curIndex].getAbsolutePath());
            setPic(listOfFiles[curIndex].getAbsolutePath());
            setLocation(listOfFiles[curIndex].getAbsolutePath());
            setCaption(listOfFiles[curIndex].getAbsolutePath());
            pictureSet = 1;
        }
    }
}
