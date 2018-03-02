package radonsoft.firenotes.Helpers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;

public class DateDialogFragment extends DialogFragment {
    public Calendar dateAndTime = Calendar.getInstance();
    public DatePickerDialog dialog;

    public interface YesNoListener {
        void onSetDate(Calendar time);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        dateAndTime = (Calendar) getArguments().getSerializable("ACTIVITY_CALENDAR");

        if (savedInstanceState != null){
            dateAndTime = (Calendar) savedInstanceState.getSerializable("CALENDAR_");
            Log.i("MSG", "DATA RESTORED");
        }

        DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                setDate();
                ((YesNoListener) getActivity()).onSetDate(dateAndTime);
            }
        };
        dialog = new DatePickerDialog(getActivity(), d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        return dialog;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        setDate();
        outState.putSerializable("CALENDAR_", dateAndTime);
        Log.i("MSG", "DATA SAVED");
        super.onSaveInstanceState(outState);
    }

    public void setDate(){
        dateAndTime.set(Calendar.YEAR, dialog.getDatePicker().getYear());
        dateAndTime.set(Calendar.MONTH, dialog.getDatePicker().getMonth());
        dateAndTime.set(Calendar.DAY_OF_MONTH, dialog.getDatePicker().getDayOfMonth());
    }
}
