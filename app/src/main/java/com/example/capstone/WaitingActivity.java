package com.example.capstone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WaitingActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Handler mHandler;
    private Runnable mCheckEmailVerificationRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting);

        mAuth = FirebaseAuth.getInstance();

        mHandler = new Handler();
        mCheckEmailVerificationRunnable = new Runnable() {
            @Override
            public void run() {
                checkEmailVerification();
                mHandler.postDelayed(this, 3000); // Repeat every 5 seconds
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.post(mCheckEmailVerificationRunnable); // Start checking on resume
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mCheckEmailVerificationRunnable); // Stop checking on pause
    }

    private void checkEmailVerification() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (user.isEmailVerified()) {
                        // Email verified, navigate to Home page
                        Intent intent = new Intent(WaitingActivity.this, Home.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }
}
