package com.smart.coffee.smartexam;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.LinkedList;

public class ExamActivity extends AppCompatActivity
        implements InfoExamFragment.fromInfoFragment,
        SessionListExamFragment.fromSessionListExamFragment{

    private Exam esame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        Toolbar toolbar = (Toolbar) findViewById(R.id.exam_act_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Riepilogo Esame");
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String idEsame = intent.getStringExtra("esameId");
        LinkedList<Exam> esami = Saver.loadExams(ExamActivity.this,"esamiFile");

        for (Exam es: esami){       // gioco malato per prendere l'esame giusto dalla lista linkata
            if(es.getExamId().equals(idEsame)){
                esame = es;
            }
        }

        // imposta l'adapter come fornitore per la vista corrente
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter pagerAdapter = new ExamFragmentPagerAdapter(getSupportFragmentManager());

        // seleziona il tablayout e lo aggancia all'adapter
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        // imposta l'adapter al pager e visualizza il fragment 0 (primo)
        viewPager.setAdapter(pagerAdapter);
        if(intent.hasExtra("sessions_please")){
            viewPager.setCurrentItem(1);
        }else{
            viewPager.setCurrentItem(0);
        }
    }

    @Override
    public void updateExamWith(Exam e) {
        this.esame = e;
        LinkedList<Exam> esami = Saver.loadExams(ExamActivity.this,"esamiFile");
        int i=0;
        for (Exam es: esami){
            if(es.getExamId().equals(esame.getExamId())){
                break;
            }
            i++;
        }
        esami.remove(i);
        esami.add(esame);
        Saver.saveExams(ExamActivity.this,"esamiFile", esami);
    }

    @Override
    public Exam getCurrentExam() {
        return esame;
    }

    @Override
    public void updateExamsSessionsWith(Session s) {
        LinkedList<Exam> esami = Saver.loadExams(ExamActivity.this,"esamiFile");
        int i=0;
        for (Exam es: esami){
            if(es.getExamId().equals(esame.getExamId())){
                break;
            }
            i++;
        }
        esami.remove(i);
        esame.addSession(s);
        esami.add(esame);
        Saver.saveExams(ExamActivity.this,"esamiFile",esami);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(ExamActivity.this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
