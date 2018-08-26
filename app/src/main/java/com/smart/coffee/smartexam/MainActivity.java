package com.smart.coffee.smartexam;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    private LinkedList<Exam> esami = new LinkedList<>();
    private Boolean THERE_IS_NO_EXAMS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mainActivityToolbar = findViewById(R.id.main_activity_actionbar);
        setSupportActionBar(mainActivityToolbar);
        ActionBar mainActionBar = getSupportActionBar();
        mainActionBar.setTitle("Gestore Esami");

        /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
        File fileEsami = new File(getApplicationContext().getFilesDir(),"esamiFile");
        if(fileEsami.isFile()){ // gli esami non si possono cancellare, se c'è il file allora ci sono esami
            esami = Saver.loadExams(getApplicationContext(), "esamiFile");
            THERE_IS_NO_EXAMS = false;
        }else{
            THERE_IS_NO_EXAMS = true;
        }

        ListView examListView = (ListView) findViewById(R.id.exam_list);
        if(THERE_IS_NO_EXAMS) {
            ViewGroup parentView = (ViewGroup) examListView.getParent();
            parentView.removeView(examListView);
            LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = layoutInflater.inflate(R.layout.void_exam_list , parentView);
            // mostra una pagina tipo 404 -> Empty-state nelle guide del material design

        }else{
            ArrayAdapter<Exam> examArrayAdapter = new ExamAdapter(this, esami);
            examListView.setAdapter(examArrayAdapter);

            // seleziona l'esame premuto dalla lista di esami
            examListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    // chiama la schermata di informazioni mandando l'oggetto
                    Intent editExamIntent = new Intent(MainActivity.this, ExamActivity.class);
                    editExamIntent.putExtra("esameId", esami.get(i).getExamId() );
                    startActivity(editExamIntent);
                }
            });
        }

        // Andare nella pagina per inserire un nuovo esame
        FloatingActionButton newExamButton = findViewById(R.id.new_exam);
        newExamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newExamIntent = new Intent(MainActivity.this, NewExamActivity.class);
                startActivity(newExamIntent);
            }
        });

    }
}
