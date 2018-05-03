package receivers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.brian.speechtherapistapp.R;
import com.brian.speechtherapistapp.models.Child;

public class EmailDialog extends DialogFragment {

    private Child child;
    private static final String LOG_TAG = EmailDialog.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        child = getArguments().getParcelable("child_object");
        Log.i(LOG_TAG, "Child's first name: " + child.getFirstName());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Email Child Details")
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");

                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                        // Add a list of known emails
                        intent.putExtra(Intent.EXTRA_EMAIL, getResources().getStringArray(R.array.intent_email));
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Child Details");

                        StringBuilder sb =new StringBuilder(getString(R.string.share_message1));
                        sb.append("\n\nFirst Name:         " + child.getFirstName());
                        sb.append("\nSecond Name:   " + child.getSecondName());
                        sb.append("\nEmail Name:       " + child.getEmail());
                        sb.append("\nWord:                   " + child.getWordSaid());
                        sb.append("\n\n" + getString(R.string.share_message2));
                        String emailContent = sb.toString();

                        intent.putExtra(Intent.EXTRA_TEXT, emailContent);

                        //extra check to be sure there is something out there that is capable of dealing with the implicit intent
                        if(intent.resolveActivity(getActivity().getPackageManager()) != null){
                            //always present the user with all apps that can perform the action
                            startActivity(Intent.createChooser(intent, "Sharing is caring!"));
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }

}

