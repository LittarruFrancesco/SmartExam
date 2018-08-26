package com.smart.coffee.smartexam;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;
import java.util.GregorianCalendar;
import java.util.LinkedList;


public class Exam implements Parcelable, java.io.Serializable{

    private final String examId;
    private String examName;
    private int pageNumber = 0;
    private int pagesCompleted = 0;
    private GregorianCalendar examDate = null;
    private String note = "";
    private LinkedList<Session> sessions;
    private int notification = 1;

    public Exam(){
        this.examId = ""+(new Timestamp(System.currentTimeMillis())).getTime();
        this.sessions = new LinkedList<>();
    }

    public Exam(String examName, GregorianCalendar examDate, Integer pageNumber){
        this.examId = ""+(new Timestamp(System.currentTimeMillis())).getTime();
        this.examDate = examDate;
        this.examName = examName;
        this.pageNumber = pageNumber;
        this.sessions = new LinkedList<>();
    }

    public Exam(String examName){
        this.examId = ""+(new Timestamp(System.currentTimeMillis())).getTime();
        this.examName = examName;
        this.pageNumber = 0;
        this.sessions = new LinkedList<>();
    }

    public String getExamId() {
        return examId;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public GregorianCalendar getExamDate() {
        return examDate;
    }

    public void setExamDate(GregorianCalendar examDate) {
        this.examDate = examDate;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void addToPagesCompleted(int sessionPages){
        this.pagesCompleted += sessionPages;
    }

    public int getPagesCompleted(){
        return pagesCompleted;
    }

    public void addNote(String note){
        this.note = note;
    }

    public String getNote(){
        return note;
    }

    public LinkedList<Session> getSessions(){
        return sessions;
    }

    public void updateSession(String id, Session newSession){
        int i=0;
        for(Session s: sessions){
            if(s.getSession_id().equals(id)){
                break;
            }i++;
        }
        sessions.remove(i);
        sessions.add(i,newSession);
    }

    public Session getSessionById(String id){
        LinkedList<Session> sessioni = this.getSessions();
        for(Session s: sessioni){
            if(s.getSession_id().equals(id)){
                return s;
            }
        }
        return null;
    }

    public void addSession(Session session){
        this.sessions.add(session);
    }

    public Boolean hasSessions(){
        return sessions!=null && sessions.size()>0;
    }

    public void setNotification(Boolean b){
        if(b){
            notification=1;
        }else{
            notification=0;
        }
    }

    public Boolean hasNotificationOn(){
        return notification == 1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //Memorizzo gli elementi della mia classe in un Parcel, in questo modo potrò ricostruirla
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(examId);
        parcel.writeString(examName);
        parcel.writeInt(pageNumber);
        parcel.writeInt(pagesCompleted);
        parcel.writeSerializable(examDate);
        parcel.writeString(note);
        parcel.writeSerializable(sessions);
        parcel.writeInt(notification);
    }

    public static final Creator<Exam> CREATOR
            = new Creator<Exam>() {
        public Exam createFromParcel(Parcel in) {
            return new Exam(in);
        }

        public Exam[] newArray(int size) {
            return new Exam[size];
        }
    };


    private Exam(Parcel in) {
        examId = in.readString();
        examName = in.readString();
        pageNumber = in.readInt();
        pagesCompleted = in.readInt();
        examDate = (GregorianCalendar) in.readSerializable();
        note = in.readString();
        sessions = (LinkedList<Session>) in.readSerializable();
        notification = in.readInt();
    }

    public static Exam findExamById(LinkedList<Exam> list, String id) {
        for (Exam e : list) {
            if (e.getExamId().equals(id)) {
                return e;
            }
        }
        return null;
    }
}
