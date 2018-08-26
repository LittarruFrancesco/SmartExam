package com.smart.coffee.smartexam;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

public final class Saver {


    public static final Boolean saveExams(Context context, String path, LinkedList<Exam> esami){
        FileOutputStream fout = null;
        ObjectOutputStream oos = null;
        try {
            fout = context.openFileOutput(path, 0);
            oos = new ObjectOutputStream(fout);
            oos.writeObject(esami);
        }catch(FileNotFoundException e){
            Log.i("save","1");
        }catch(IOException e){
            Log.i("save","2");
        }finally {
            try{
                if(fout != null)fout.close();   // se lancia allora oos non era aperto
                if(oos != null)oos.close();
            }catch(IOException e){
                Log.i("save","3");
            }
        }
        return true;
    }

    public static final LinkedList<Exam> loadExams(Context context, String path){
        File f = new File(context.getFilesDir(),path);
        LinkedList<Exam> listaEsami = new LinkedList<>();
        ObjectInputStream in = null;
        FileInputStream fis = null;
        try {
            fis = context.openFileInput(path);
            in = new ObjectInputStream(fis);
            listaEsami = (LinkedList<Exam>) in.readObject();
        }catch(FileNotFoundException e){
            Log.i("load","FileNotFound load");
        }catch(IOException e){
            Log.i("load","IOLoad");
        }catch(ClassNotFoundException e){
            Log.i("load","Class load");
        }finally {
            try {
                if(in != null)in.close();
                if(fis != null)fis.close();
            }catch (IOException e){
                Log.i("load","IO load close");
            }catch (NullPointerException e){
                Log.i("load","NULL load");
            }
        }
        return listaEsami;
    }
}
