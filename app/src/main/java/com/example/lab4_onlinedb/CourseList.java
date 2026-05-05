package com.example.lab4_onlinedb;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CourseList extends ArrayAdapter<Course> {
    private final Activity context;
    List<Course> courses;

    //Create constructor
    public CourseList(Activity context, List<Course> courses) {
        super(context, R.layout.list_layout, courses);
        this.context = context;
        this.courses = courses;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);

        TextView textViewName = listViewItem.findViewById(R.id.textViewName);

        Course course = courses.get(position);
        textViewName.setText(course.getCourseName());

        return listViewItem;
    }
}