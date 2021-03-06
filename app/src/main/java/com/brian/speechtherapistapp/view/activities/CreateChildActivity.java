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

import com.brian.speechtherapistapp.MainApplication;
import com.brian.speechtherapistapp.R;
import com.brian.speechtherapistapp.presentation.IChildPresenter;
import com.brian.speechtherapistapp.view.IChildView;
import com.brian.speechtherapistapp.view.activities.base.BaseActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CreateChildActivity extends BaseActivity implements IChildView {

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

    private Calendar calendar = Calendar.getInstance();
    private static final int CHILD_ID = 0;
    private static final String LOG_TAG = CreateChildActivity.class.getSimpleName();
    private static final String VALID_TEXT = "[a-zA-z]+([ '-][a-zA-Z]+)*";
    private static final String VALID_EMAIL_REGEX =
            "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainApplication) getApplication()).getPresenterComponent().inject(this);
        getLayoutInflater().inflate(R.layout.activity_create_child, frameLayout);
        ButterKnife.bind(this);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean validFieldsSet = isRequiredFieldsSet();
                boolean validStringInput = isInputString();
                boolean validEmail = isEmailValid();

                if (validFieldsSet && validStringInput && validEmail) {
                    passwordConfirmListener(saveButton);
                    boolean passwordMatch = isPasswordMatching();

                    if (passwordMatch) {

                        // MongoDB Driver call
                        // iChildPresenter.saveChild();

                        // REST Api call
                        iChildPresenter.createUser();

                        Intent intentTherapistActivity = new Intent(getApplicationContext(),
                                HomeActivity.class);
                        startActivity(intentTherapistActivity);
                    }
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
        showToast(firstNameEditText.getText().toString() +" saved!");
    }

    @Override
    public void displayFirstName(String firstName) {
        firstNameEditText.setText(firstName);
    }



    @Override
    public void displaySecondName(String secondName) {
        secondNameEditText.setText(secondName);
    }

    @Override
    public void displayEmail(String email) {
        emailEditText.setText(email);
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
                    if (!passConfirm.equals(password)) {
                        displayButtonAsInputError(button);
                        showToast("Your passwords do not match");
                    } else {
                        displayButtonAsNonInputError(button);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private boolean isRequiredFieldsSet() {
        if (firstNameEditText.getText().toString().length() == 0) {
            showToast("You need to enter your first name!");
            return false;
        }
        else if (secondNameEditText.getText().toString().length() == 0) {
            showToast("You need to enter your second name!");
            return false;
        }
        else if (emailEditText.getText().toString().length() == 0) {
            showToast("You need to enter your email address!");
            return false;
        }
        else if (passwordEditText.getText().toString().length() == 0) {
            showToast("You need to enter your password!");
            return false;
        }
        else if (passwordConfirmEditText.getText().toString().length() == 0) {
            showToast("You need to enter your password confirmation!");
            return false;
        }
        return true;
    }

    private boolean isInputString() {
        if (!firstNameEditText.getText().toString().matches(VALID_TEXT)) {
            showToast("Doesn't look like a valid first name!");
            return false;
        }
        else if (!secondNameEditText.getText().toString().matches(VALID_TEXT)) {
            showToast("Doesn't look like a valid second name!");
            return false;
        }
        return true;
    }

    public boolean isEmailValid() {
        String email = emailEditText.getText().toString();
        if(email != null) {
            Pattern pattern = Pattern.compile(VALID_EMAIL_REGEX);
            Matcher matcher = pattern.matcher(email);
            if(matcher.matches()) {
                return true;
            }
            showToast("Doesn't look like a valid email!");
        }
        return false;
    }

    private void displayButtonAsInputError(final Button button) {
        button.setEnabled(false);
        button.setTextAppearance(getApplicationContext(),
                R.style.AppTheme_RedButton);
        Drawable backgroundRed = ContextCompat.getDrawable(getApplication(),
                R.drawable.red_button_background);
        button.setBackground(backgroundRed);
    }

    private void displayButtonAsNonInputError(final Button button) {
        button.setEnabled(true);
        button.setTextAppearance(getApplicationContext(),
                R.style.AppTheme_GreenButton);
        Drawable backgroundGreen = ContextCompat.getDrawable(getApplication(),
                R.drawable.green_button_background);
        button.setBackground(backgroundGreen);
    }

    @OnClick(R.id.btn_clear_form)
    public void onButtonClearClick(View view) {
        switch (view.getId()){
            case R.id.btn_clear_form:
                firstNameEditText.setText("");
                secondNameEditText.setText("");
                emailEditText.setText("");
                passwordEditText.setText("");
                passwordConfirmEditText.setText("");
        }
    }
}