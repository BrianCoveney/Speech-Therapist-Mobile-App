package com.brian.speechtherapistapp.view.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.brian.speechtherapistapp.MainApplication;
import com.brian.speechtherapistapp.R;
import com.brian.speechtherapistapp.presentation.IChildPresenter;
import com.brian.speechtherapistapp.view.IChildView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class CreateChildActivity extends BaseActivity implements IChildView {

    private Calendar calendar = Calendar.getInstance();

    @Inject
    IChildPresenter iChildPresenter;

    @BindView(R.id.et_first_name)
    EditText firstNameEditText;

    @BindView(R.id.et_second_name)
    EditText secondNameEditText;

    @BindView(R.id.et_email)
    EditText emailEditText;

    @BindView(R.id.et_date)
    EditText dateOfBirthEditText;

    @BindView(R.id.btn_save)
    Button saveButton;

    @BindView(R.id.et_password)
    EditText passwordEditText;

    @BindView(R.id.et_password_confirm)
    EditText passwordConfirmEditText;

    private static final int CHILD_ID = 1;

    @Override
    protected int getContentView() {
        return R.layout.activity_create_child;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        ((MainApplication) getApplication()).getPresenterComponent().inject(this);



        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                passwordConfirmListener(saveButton);

                boolean passwordMatch = isPasswordMatching();
                if (passwordMatch == false) {
                    showToast("Your passwords do not match");
                } else {
                    iChildPresenter.saveChild();
                    Intent intentTherapistActivity = new Intent(getApplicationContext(),
                            TherapistMenuActivity.class);
                    startActivity(intentTherapistActivity);

                }
            }
        });

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        iChildPresenter.setView(this);
    }

    @Override
    public int getChildId() {
        return CHILD_ID;
    }

    @Override
    public String getFirstName() {
        return firstNameEditText.getText().toString();
    }

    @Override
    public String getSecondName() {
        return secondNameEditText.getText().toString();
    }

    @Override
    public String getEmail() {
        return emailEditText.getText().toString();
    }

    @Override
    public String getDateOfBirth() {
        return dateOfBirthEditText.getText().toString();
    }

    @Override
    public String getPassword() {
        return passwordEditText.getText().toString();
    }

    @Override
    public void showChildSavedMessage() {
        Toast.makeText(this, R.string.child_saved, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayFirstName(String firstName) {
        firstNameEditText.setText(firstName);
    }

    @Override
    public void displaySecondName(String secondName) {
        secondNameEditText.setText(secondName);
    }


    DatePickerDialog.OnDateSetListener date = (new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int myYear, int myMonthOfYear, int myDayOfMonth) {
            calendar.set(Calendar.YEAR, myYear);
            calendar.set(Calendar.MONTH, myMonthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, myDayOfMonth);

            DateFormat dateFormatDay = new SimpleDateFormat("dd/MM/yyyy");
            String date = dateFormatDay.format(calendar.getTime());
            dateOfBirthEditText.setText(date);
        }
    });


    @OnClick(R.id.et_date)
    public void dateOnClick(View view) {
        new DatePickerDialog(this, date,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private boolean isPasswordMatching() {
        boolean isPassMatch = false;
        String password = passwordEditText.getText().toString();
        String confirmPassword = passwordConfirmEditText.getText().toString();
        if (password.equals(confirmPassword)) {
            isPassMatch = true;
        }
        return isPassMatch;
    }

    private void passwordConfirmListener(final Button button) {
        passwordConfirmEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence c, int i, int i1, int i2) {
                String password = passwordEditText.getText().toString();
                String passConfirm = passwordConfirmEditText.getText().toString();
                if (c.length() > 0 && password.length() > 0) {
                    if(!passConfirm.equals(password)) {
                        button.setEnabled(false);
                        button.setTextAppearance(getApplicationContext(),
                                R.style.AppTheme_RedButton);
                        Drawable backgroundRed = ContextCompat.getDrawable(getApplication(),
                                R.drawable.red_button_background);
                        button.setBackground(backgroundRed);
                    } else {
                        button.setEnabled(true);
                        button.setTextAppearance(getApplicationContext(),
                                R.style.AppTheme_GreenButton);
                        Drawable backgroundGreen = ContextCompat.getDrawable(getApplication(),
                                R.drawable.green_button_background);
                        button.setBackground(backgroundGreen);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

}