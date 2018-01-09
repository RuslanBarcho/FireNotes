package radonsoft.firenotes.Helpers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by RRCFo on 04.01.2018.
 */

public class DateDialogFragment extends DialogFragment {
    public Calendar dateAndTime = Calendar.getInstance();

    public Calendar getDateAndTime() {
        return dateAndTime;
    }

    public interface YesNoListener {
        void onYes();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateAndTime.set(Calendar.YEAR, year);
                dateAndTime.set(Calendar.MONTH, monthOfYear);
                dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                ((YesNoListener) getActivity()).onYes();
            }
        };

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        return dialog;
    }

}
