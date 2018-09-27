package com.example.quincy.a00994454asn1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchActivity extends AppCompatActivity {

    /**
     * Dummy data
     */
    ArrayList<String> dateData = new ArrayList<String>(Arrays.asList("Mt. Fuji", "Mt. Everestt", "Disneyland", "Disneyworld"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //Initialize Target Button
        final Button button = findViewById(R.id.searchConfirm);

        //Initialize request text box
        final TextView requestText = findViewById(R.id.searchBox);

        //Initialize result text box
        final TextView resultText = findViewById(R.id.resultText);

        //Add listener onto button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String request = requestText.getText().toString();

                //Get requested text and see if it's in data, return something by result
                if (dateData.contains(request)) {
                    resultText.setText("Found something");
                } else {
                    resultText.setText("None found");
                }
            }
        });
    }


}
