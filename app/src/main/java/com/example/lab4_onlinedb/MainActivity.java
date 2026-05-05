package com.example.lab4_onlinedb;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText editTextName;
    Button buttonAdd;
    DatabaseReference databaseCourses;
    ArrayList<Course> courseList;
    ListView listViewCourses;
    CourseList courseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FirebaseApp.initializeApp(this);
        databaseCourses = FirebaseDatabase.getInstance().getReference("courses");
        editTextName = findViewById(R.id.editTextName);
        buttonAdd = findViewById(R.id.buttonAddData);
        listViewCourses = findViewById(R.id.ListViewCourses);
        courseList = new ArrayList<Course>();
        courseAdapter = new CourseList(MainActivity.this, courseList);
        listViewCourses.setAdapter(courseAdapter);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCourse();
            }
        });
    }

    private void addCourse() {
        //get listtname and convert to string from editextname
        String name = editTextName.getText().toString().trim();

        //check if the name is not empty
        if (!TextUtils.isEmpty(name)) {
            //if exist push data to firebase database
            //store inside id in database
            //every time data stored the id will be unique
            String id = databaseCourses.push().getKey();
            //store
            Course list = new Course(id, name);
            //store list inside unique id
            databaseCourses.child(id).setValue(list);
            Toast.makeText(this, "Data added", Toast.LENGTH_LONG).show();

        } else {
            //if the name is empty
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter data", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
        databaseCourses.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous artist list
                courseList.clear();

                Toast.makeText(MainActivity.this, "Found " + dataSnapshot.getChildrenCount() + " items", Toast.LENGTH_SHORT).show();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting course
                    Course course = postSnapshot.getValue(Course.class);
                    //adding course to the list
                    courseList.add(course);
                }

                //creating adapter
                //courseAdapter = new CourseList(MainActivity.this, courseList);
                //attaching adapter to the listview
                //listViewCourses.setAdapter(courseAdapter);

                courseAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}