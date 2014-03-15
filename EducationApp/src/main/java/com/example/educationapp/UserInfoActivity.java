package com.example.educationapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kevin on 14-3-13.
 */
public class UserInfoActivity extends SherlockActivity implements View.OnClickListener {
    private static final int REQUEST_CODE_CAPTURE_IMAGE = 1;
    private static final int REQUEST_CODE_CHOOSE_IMAGE = 2;
    private static final int REQUEST_CODE_CUT_IMAGE = 10;
    private static final String PHOTO_FILE_PATH = Environment.getExternalStorageDirectory() + "/EducationApp/Camera";
    private CheckedTextView mStudent;
    private CheckedTextView mTeacher;
    private ImageView mPhoto;
    private Dialog mChoosePhotoDialog;
    private String mCurrentPhotoFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mPhoto = (ImageView)findViewById(R.id.photo);
        mStudent = (CheckedTextView)findViewById(R.id.identity_student);
        mTeacher = (CheckedTextView)findViewById(R.id.identity_teacher);
        mPhoto.setOnClickListener(this);
        mStudent.setOnClickListener(this);
        mTeacher.setOnClickListener(this);

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_choose_photo, null);
        dialogView.findViewById(R.id.shot_photo).setOnClickListener(this);
        dialogView.findViewById(R.id.choose_photo).setOnClickListener(this);
        mChoosePhotoDialog = new AlertDialog.Builder(this).setView(dialogView).create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.identity_student:
            case R.id.identity_teacher:
                ((CheckedTextView)v).setChecked(!((CheckedTextView) v).isChecked());
                break;
            case R.id.photo:
                mChoosePhotoDialog.show();
                break;
            case R.id.shot_photo:
                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "'IMG'_yyyy-MM-dd HH:mm:ss");
                String photoName = dateFormat.format(date) + ".jpg";
                File f = new File(PHOTO_FILE_PATH, photoName);
                mCurrentPhotoFilePath = f.getAbsolutePath();
                Intent shotIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                shotIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                startActivityForResult(shotIntent, REQUEST_CODE_CAPTURE_IMAGE);
                mChoosePhotoDialog.dismiss();
                break;
            case R.id.choose_photo:
                Intent chooseIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(chooseIntent, REQUEST_CODE_CHOOSE_IMAGE);
                mChoosePhotoDialog.dismiss();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case REQUEST_CODE_CHOOSE_IMAGE:
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    mCurrentPhotoFilePath = cursor.getString(columnIndex);
                    cursor.close();
                case REQUEST_CODE_CAPTURE_IMAGE:
                    Intent intent = new Intent();
                    intent.setClass(this, CutImageActivity.class);
                    intent.putExtra(CutImageActivity.EXTRA_IMAGE_PATH, mCurrentPhotoFilePath);
                    startActivityForResult(intent, REQUEST_CODE_CUT_IMAGE);

            }
        }
    }
}
