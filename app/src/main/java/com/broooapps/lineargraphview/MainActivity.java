package com.broooapps.lineargraphview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.broooapps.lineargraphview2.DataModel;
import com.broooapps.lineargraphview2.LinearGraphView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearGraphView lgv = findViewById(R.id.lgv);

        List<DataModel> dm = new ArrayList<>();

        dm.add(new DataModel("One", "#123123", 200));
        dm.add(new DataModel("two", "#abcdef", 200));
        dm.add(new DataModel("three", "#cdebad", 200));
        dm.add(new DataModel("four",  "#abbcde", 200));

        lgv.setData(dm, 999);
    }
}
