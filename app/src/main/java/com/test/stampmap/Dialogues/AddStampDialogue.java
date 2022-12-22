package com.test.stampmap.Dialogues;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import java.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;
import com.test.stampmap.Adapter.StampRecyclerAdapter;
import com.test.stampmap.R;
import com.test.stampmap.Stamp.StampCollection;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class AddStampDialogue extends DialogFragment {

    private static final int SELECT_PICTURE = 200;

    private ImageView stampImage;

    private static String base64Image;

    public AddStampDialogue(){}
    public AddStampDialogue(int BodgeTwoElectricBoogaloo){
        base64Image = null;;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance) {
        View v = layoutInflater.inflate(R.layout.add_stamp_popup, container,false);
        Button addImageButton = v.findViewById(R.id.addImageButton);
        Button confirmStampButton = v.findViewById(R.id.addStampButton);
        EditText stampName = v.findViewById(R.id.newStampName);
        stampImage = v.findViewById(R.id.newStampImage);
        addImageButton.setOnClickListener(v1 -> imageChooser());
        confirmStampButton.setOnClickListener(v1 -> {
            if (stampImage.getDrawable() == null || stampName.getText().toString().equals("")){
                Toast.makeText(requireContext(), "no name or image boi", Toast.LENGTH_SHORT).show();
                return;
            }
            List<String> data = new ArrayList<>();

            data.add(stampName.getText().toString());
            data.add(base64Image);
            StampRecyclerAdapter stampAdapter = AddStampSetDialogue.adapter;
            List<List<String>> dataSet = (List<List<String>>) stampAdapter.getDataSet();
            dataSet.add(data);
            stampAdapter.notifyDataSetChanged();
            dismiss();
        });
        return v;
    }

    // Here we will pick image from gallery or camera
    private void pickFromGallery(Uri uri) {
        CropImage.activity(uri).setAspectRatio(1, 1).setRequestedSize(400, 400).start(requireContext(), this);
    }

    private String encodeBase64(ImageView image){
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100, stream);
        byte[] bytes=stream.toByteArray();
        return Base64.getEncoder().encodeToString(bytes);
    }

    // this function is triggered when
    // the Select Image Button is clicked
    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                stampImage.setImageURI(resultUri);
                base64Image = encodeBase64(stampImage);
            }
        }
        if (requestCode == SELECT_PICTURE) {
            if (resultCode == RESULT_OK) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    pickFromGallery(selectedImageUri);
                }
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if (base64Image != null && !base64Image.equals("")
        && stampImage != null && stampImage.getDrawable() == null) StampCollection.loadImage(base64Image, stampImage);
    }
}
