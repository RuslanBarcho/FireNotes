package radonsoft.firenotes.Helpers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

/**
 * Created by RRCFo on 24.03.2018.
 */

public class NotificationsDialogFragment extends DialogFragment {
    public AlertDialog dialog;
    public interface NotificationSet{

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return dialog;
    }

}
