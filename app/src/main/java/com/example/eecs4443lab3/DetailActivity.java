package com.example.eecs4443lab3;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get and display data from Intent
        TextView header = findViewById(R.id.detail_header);
        header.setText(getIntent().getStringExtra("taskName"));

        TextView deadline = findViewById(R.id.detail_deadline);
        deadline.setText(getString(R.string.task_deadline, getIntent().getStringExtra("deadline")));

        TextView desc = findViewById(R.id.detail_desc);
        desc.setText(getIntent().getStringExtra("desc"));
    }
}