package com.lg.appcontentprovider;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView datalist;
    EditText studentID;
    EditText studentName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datalist = findViewById(R.id.txtData);
        studentID = findViewById(R.id.edStudentID);
        studentName = findViewById(R.id.edStudentName);
    }

    public void addStudent(View view) {
        ContentValues values = new ContentValues();
        values.put(DatabaseStudent.COLUMN_ID,
                studentID.getText().toString());
        values.put(DatabaseStudent.COLUMN_NAME,
                studentName.getText().toString());

        Uri uri = getContentResolver().insert(
                MyContentProvider.CONTENT_URI, values);
        Toast.makeText(this,uri.toString(),Toast.LENGTH_SHORT).show();
    }
    public void loadStudents(View view) {
        Cursor c = getContentResolver().query(MyContentProvider.CONTENT_URI, null, null, null, null);

        if(c!=null) {
            if (c.moveToFirst()) {
                do {
                    Log.d("MainLoadStudents", c.getString(c.getColumnIndex(DatabaseStudent.COLUMN_ID)) + ", " + c.getString(c.getColumnIndex(DatabaseStudent.COLUMN_NAME)));
                } while (c.moveToNext());
            }
        }
    }
    public void findFirstStudent(View view) {

    }
    public void findAllStudent(View view) {

    }
    public void deleteStudent(View view) {

    }
    public void updateStudent(View view) {

    }
}
