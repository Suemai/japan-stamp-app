package com.test.stampmap.Dialogues;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import java.util.Base64;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.DialogFragment;
import com.canhub.cropper.CropImageActivity;
import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.test.stampmap.Adapter.StampRecyclerAdapter;
import com.test.stampmap.R;
import com.test.stampmap.Stamp.StampCollection;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class AddStampDialogue extends DialogFragment {

    private ImageView stampImage;

    public static AddStampDialogue.StampAddedEvent event;

    private static String base64Image;

    private final ActivityResultLauncher<CropImageContractOptions> cropImage = registerForActivityResult(new CropImageContract(), result -> {
        if (result.isSuccessful()) {
            stampImage.setImageURI(result.getUriContent());
            base64Image = encodeBase64(stampImage);
        }
    });

    public AddStampDialogue(){}
    public AddStampDialogue(int BodgeTwoElectricBoogaloo){
        base64Image = null;
        event = null;
    }
    public AddStampDialogue(AddStampDialogue.StampAddedEvent event){
        base64Image = null;
        AddStampDialogue.event = event;
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
            // if information not filled out
            if (stampImage.getDrawable() == null || stampName.getText().toString().equals("")){
                Toast.makeText(requireContext(), "no name or image boi", Toast.LENGTH_SHORT).show();
            }
            // if being called from the StampSheet, return information
            else if (event != null){
                event.onStampCreated(stampName.getText().toString(), base64Image);
                dismiss();
            }
            // else add data to the AddStampSetDialogue page
            else {
                List<String> data = new ArrayList<>();

                data.add(stampName.getText().toString());
                data.add(base64Image);
                StampRecyclerAdapter stampAdapter = AddStampSetDialogue.adapter;
                List<List<String>> dataSet = (List<List<String>>) stampAdapter.getDataSet();
                dataSet.add(data);
                stampAdapter.notifyDataSetChanged();
                dismiss();
            }
        });
        return v;
    }

    private String encodeBase64(ImageView image){
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100, stream);
        byte[] bytes = stream.toByteArray();
        return Base64.getEncoder().encodeToString(bytes);
    }

    private static CropImageOptions getCropImageOptions() {
        CropImageOptions options = new CropImageOptions();
        options.imageSourceIncludeCamera = true;
        options.imageSourceIncludeGallery = true;
        options.aspectRatioX = 1;
        options.aspectRatioY = 1;
        options.activityTitle = "Pick Stamp Image";
        options.fixAspectRatio = true;
        options.activityBackgroundColor = Color.alpha(0);
        return options;
    }

    // this function is triggered when
    // the Select Image Button is clicked
    void imageChooser() {
        cropImage.launch(new CropImageContractOptions(null, getCropImageOptions()));
    }

    @Override
    public void onResume(){
        super.onResume();
        if (base64Image != null && !base64Image.equals("")
        && stampImage != null && stampImage.getDrawable() == null) StampCollection.loadImage(base64Image, stampImage);
    }

    public interface StampAddedEvent{
        void onStampCreated(String name, String image);
    }
}
