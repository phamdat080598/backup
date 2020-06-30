package com.lg.appeditorone;

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

    static final String AUTHORITY = "com.lg.appcontentprovider.MyContentProvider";
    static final String STUDENTS_TABLE = "Students";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + STUDENTS_TABLE);

    public static final String COLUMN_ID = "StudentID";
    public static final String COLUMN_NAME = "StudentName";

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
        values.put(COLUMN_ID,
                studentID.getText().toString());
        values.put(COLUMN_NAME,
                studentName.getText().toString());

        Uri uri = getContentResolver().insert(
                CONTENT_URI, values);
        Toast.makeText(this,uri.toString(),Toast.LENGTH_SHORT).show();
    }
    public void loadStudents(View view) {
        Cursor c = getContentResolver().query(CONTENT_URI, null, null, null, null);

        if(c!=null) {
            if (c.moveToFirst()) {
                do {
                    Log.d("MainLoadStudents", c.getString(c.getColumnIndex(COLUMN_ID)) + ", " + c.getString(c.getColumnIndex(COLUMN_NAME)));
                    datalist.append(c.getString(c.getColumnIndex(COLUMN_ID)) + ", " + c.getString(c.getColumnIndex(COLUMN_NAME))+"\n");
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
