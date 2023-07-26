package com.example.homemaintanenceserviceapp.Worker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.homemaintanenceserviceapp.R;
import com.example.homemaintanenceserviceapp.databinding.ActivityPhoneNumVerificationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneNumVerification extends AppCompatActivity {
    ActivityPhoneNumVerificationBinding binding;
    EditText mPhoneNumber, mVerificationCode;
    Button mSendCode, mVerifyCode;

    FirebaseAuth mAuth;

    String mVerificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhoneNumVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.phoneverifytoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mAuth = FirebaseAuth.getInstance();

        mPhoneNumber = findViewById(R.id.phone_number_edit_text);
        mVerificationCode = findViewById(R.id.verification_code_edit_text);
        mSendCode = findViewById(R.id.send_verification_code_button);
        mVerifyCode = findViewById(R.id.verify_code_button);

        mSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = mPhoneNumber.getText().toString().trim();
                if(phoneNumber.isEmpty() || phoneNumber.length() < 10) {
                    mPhoneNumber.setError("Valid phone number is required.");
                    mPhoneNumber.requestFocus();
                    return;
                }

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+92" + phoneNumber,
                        60,
                        TimeUnit.SECONDS,
                        PhoneNumVerification.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(PhoneNumVerification.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(verificationId, forceResendingToken);
                                mVerificationId = verificationId;
                            }
                        });
            }
        });

        mVerifyCode.setOnClickListener(v -> {
            String verificationCode = mVerificationCode.getText().toString().trim();
            if(verificationCode.isEmpty()) {
                mVerificationCode.setError("Verification code required.");
                mVerificationCode.requestFocus();
                return;
            }

            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode);
            signInWithPhoneAuthCredential(credential);
        });

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(PhoneNumVerification.this, "Verification successful.", Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(PhoneNumVerification.this, workerSignUp.class);
                            intent.putExtra("PhoneNum",  credential.getProvider());
                            startActivity(intent);
                        } else {
                            Toast.makeText(PhoneNumVerification.this, "Verification failed.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
   /* private ActivityPhoneNumVerificationBinding binding;
    private EditText mPhoneNumberInput;
    private Button mVerifyPhoneNumberButton;
    private EditText mVerificationCodeInput;
    private Button mVerifyCodeButton;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPhoneNumVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.phoneverifytoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mPhoneNumberInput = findViewById(R.id.phone_number_input);
        mVerifyPhoneNumberButton = findViewById(R.id.verify_phone_number_button);
        mVerificationCodeInput = findViewById(R.id.verification_code_input);
        mVerifyCodeButton = findViewById(R.id.submit_verification_code_button);
        mVerifyCodeButton.setEnabled(false);

        mVerifyPhoneNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = mPhoneNumberInput.getText().toString();

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phoneNumber,
                        60,
                        TimeUnit.SECONDS,
                        PhoneNumVerification.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                                mVerificationCodeInput.setText(phoneAuthCredential.getSmsCode());
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(FirebaseException e) {
                                Toast.makeText(PhoneNumVerification.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                                super.onCodeSent(verificationId, token);
                                mVerificationId = verificationId;
                                mResendToken = token;
                                mVerifyPhoneNumberButton.setEnabled(false);
                                mVerifyCodeButton.setEnabled(true);
                                mPhoneNumberInput.setEnabled(false);
                            }
                        });
            }
        });

        mVerifyCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String verificationCode = mVerificationCodeInput.getText().toString();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode);
                signInWithPhoneAuthCredential(credential);
            }
        });

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, go to signup as worker page
                            Intent intent = new Intent(PhoneNumVerification.this, workerSignUp.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Sign in failed, display a message and update UI
                            Toast.makeText(PhoneNumVerification.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}*/