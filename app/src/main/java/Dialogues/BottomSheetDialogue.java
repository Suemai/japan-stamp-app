package Dialogues;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.test.stampmap.R;

public class BottomSheetDialogue extends BottomSheetDialogFragment {

    @Override
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance){
    View v = layoutInflater.inflate(R.layout.filter_drawer, container,false);

    CheckBox aichi_checkB = v.findViewById(R.id.aichi_checkBox);
    Button akita_button = v.findViewById(R.id.akita_button);

    aichi_checkB.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(aichi_checkB.getContext(),
                    "Aichi selected",
                    Toast.LENGTH_SHORT).show();
            dismiss();
        }
    });

    akita_button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(akita_button.getContext(),
                    "Akita pressed",
                    Toast.LENGTH_SHORT).show();
            dismiss();
        }
    });
    return v;

}
}


