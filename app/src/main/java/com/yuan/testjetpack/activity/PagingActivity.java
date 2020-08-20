package com.yuan.testjetpack.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.yuan.testjetpack.R;
import com.yuan.testjetpack.adapter.MyPagedAdapter;
import com.yuan.testjetpack.db.Student;
import com.yuan.testjetpack.db.StudentDao;
import com.yuan.testjetpack.db.StudentDataBase;

public class PagingActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Button buttonPopulate, buttonClear;
    StudentDao studentDao;
    StudentDataBase studentsDatabase;
    MyPagedAdapter pagedAdapter;
    LiveData<PagedList<Student>> allStudentsLivePaged;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paging);
        recyclerView = findViewById(R.id.recyclerView);
        buttonPopulate = findViewById(R.id.buttonPopulate);
        buttonClear = findViewById(R.id.buttonClear);
        pagedAdapter = new MyPagedAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(pagedAdapter);
        studentsDatabase = StudentDataBase.getInstance(this);
        studentDao = studentsDatabase.getStudentDao();
        allStudentsLivePaged = new LivePagedListBuilder<>(studentDao.getAllStudents(), 30).build();
        allStudentsLivePaged.observe(this, new Observer<PagedList<Student>>() {
            @Override
            public void onChanged(final PagedList<Student> students) {
                pagedAdapter.submitList(students);
                students.addWeakCallback(null, new PagedList.Callback() {
                    @Override
                    public void onChanged(int position, int count) {
                        Log.d("myLog", "onChanged: " + students);
                    }

                    @Override
                    public void onInserted(int position, int count) {

                    }

                    @Override
                    public void onRemoved(int position, int count) {

                    }
                });
            }
        });
        buttonPopulate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Student[] students = new Student[1000];
                for (int i = 0; i < students.length; i++) {
                    Student student = new Student();
                    student.setStudentNumber(i);
                    students[i] = student;
                }
                new InsertAsyncTask(studentDao).execute(students);
            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ClearAsyncTask(studentDao).execute();
            }
        });

    }

    static class InsertAsyncTask extends AsyncTask<Student, Void, Void> {

        StudentDao studentDao;
        public InsertAsyncTask(StudentDao studentDao) {
            this.studentDao = studentDao;
        }

        @Override
        protected Void doInBackground(Student... students) {
            studentDao.insertStudent(students);
            return null;
        }
    }

    static class ClearAsyncTask extends AsyncTask<Void, Void, Void> {
        StudentDao studentDao;
        public ClearAsyncTask(StudentDao studentDao) {
            this.studentDao = studentDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            studentDao.deleteAllStudents();
            return null;
        }
    }
}