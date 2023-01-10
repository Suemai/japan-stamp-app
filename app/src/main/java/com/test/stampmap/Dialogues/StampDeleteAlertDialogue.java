package com.test.stampmap.Dialogues;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import com.test.stampmap.Stamp.Stamp;
import com.test.stampmap.Stamp.StampCollection;
import com.test.stampmap.Stamp.StampSet;
import org.jetbrains.annotations.Nullable;

public class StampDeleteAlertDialogue extends AlertDialog.Builder {

    public StampDeleteAlertDialogue(Context context, StampSet stampSet, @Nullable StampDeletedCallback callback) {
        super(context);
        boolean hasDefaults = stampSet.getStamps().stream().filter(Stamp::getIsCustom).count() != stampSet.getStamps().size();
        setTitle(hasDefaults ? "You cannot delete a StampSet containing built-in stamps!" :
                "Are you sure you would like to delete the Stampset '" + stampSet.getName() + "'?");
        if (!hasDefaults)
            setPositiveButton("YES", (dialog, l) -> {
                StampCollection.getInstance().deleteStampSet(stampSet);
                if (callback != null) callback.onStampDeleted();
            });

        setNegativeButton(hasDefaults ? "OK" : "NO", (dialog, which) -> dialog.dismiss());
        show();
    }

    public StampDeleteAlertDialogue(Context context, Stamp stamp, @Nullable StampDeletedCallback callback){
        super(context);
        boolean isDefault = !stamp.getIsCustom();
        setTitle(isDefault ? "You cannot delete a built-in stamp!" :
                "Are you sure you would like to delete the Stamp '" + stamp.getName() + "'?");
        if (!isDefault)
            setPositiveButton("YES", (dialog, l) -> {
                StampCollection.getInstance().deleteCustomStamp(stamp);
                if (callback != null) callback.onStampDeleted();
            });

        setNegativeButton(isDefault ? "OK" : "NO", (dialog, which) -> dialog.dismiss());
        show();
    }

    public interface StampDeletedCallback {
        void onStampDeleted();
    }
}
