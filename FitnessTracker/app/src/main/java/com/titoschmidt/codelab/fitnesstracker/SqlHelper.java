package com.titoschmidt.codelab.fitnesstracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SqlHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "fitness_tracker.db";
    private static final int DB_VERSION = 1;
    private static SqlHelper INSTANCE;

    // Se não existe uma instância SqlHelper, cria ela. Senão, retorna a própria instância
    static SqlHelper getInstance(Context context){
        if(INSTANCE==null)
            INSTANCE = new SqlHelper(context);
        return INSTANCE;
    }

    // O construtor deve ser privado a fim de impedir que mais de uma instância seja criada pois somente uma é necessária na app
    private SqlHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    // Cria a tabela (caso não exista) ao executar a app pela primeira vez
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS calc(id INTEGER primary key, type TEXT, response DECIMAL, createdDate DATETIME, modifiedDate DATETIME)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("upgrade", "on Upgrade disparado");
    }

    //Buscar
    Register buscar(long id){
        Register registro = new Register();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM calc WHERE id =?",  new String[]{String.valueOf(id)});

        try{
            cursor.moveToFirst();
            if(cursor.getCount() > 0) {
                registro.setId(cursor.getInt(cursor.getColumnIndex("id")));
                registro.setType(cursor.getString(cursor.getColumnIndex("type")));
                registro.setResponse(cursor.getDouble(cursor.getColumnIndex("response")));
                registro.setCreatedDate(cursor.getString(cursor.getColumnIndex("createdDate")));
            }
        } catch (Exception e) {
            Log.e("SQLite", e.getMessage(), e);
        } finally {
          if(cursor != null && !cursor.isClosed())
              cursor.close();
        }
        return registro;
    }

    // Atualizar
    Boolean atualizar(int id, double response){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("response", response);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm;ss", new Locale("pt","br"));
        String now = sdf.format(new Date());
        values.put("modifiedDate", now);

        try{
            db.update("calc", values, "id = ? ", new String[]{String.valueOf(id)});
            return true;
        } catch (Exception e){
            Log.e("SQLite", e.getMessage(), e);
        } finally {
            if(db.isOpen()){
                db.close();
            }
        }
        return false;
    }

    // Excluir
    Boolean excluir(int id) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.delete("calc", "id = ? ", new String[]{String.valueOf(id)});
            return true;
        } catch (Exception e){
            Log.e("SQLite", e.getMessage(), e);
        } finally {
            if(db.isOpen()){
                db.close();
            }
        }
        return false;
    }

    // Listar
    List<Register> listar(String type){
        List<Register> registers = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM calc WHERE type =?", new String[]{ type });

        try {
            if(cursor.moveToFirst()){
                do{
                    Register register = new Register();
                    register.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    register.setType(cursor.getString(cursor.getColumnIndex("type")));
                    register.setResponse(cursor.getDouble(cursor.getColumnIndex("response")));
                    register.setCreatedDate(cursor.getString(cursor.getColumnIndex("createdDate")));
                    register.setModifiedDate(cursor.getString(cursor.getColumnIndex("modifiedDate")));
                    registers.add(register);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("SQLite", e.getMessage(), e);
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return registers;
    }

    // Inserir
    long addItem(String type, double response){
        // getWritableDatabase -> inserir, atualizar, excluir
        // getReadableDataBase -> consultar
        SQLiteDatabase db = getWritableDatabase();
        // Se ocorrer algum erro o valor 0 impede que o dado seja salvo no banco
        long calcId = 0;
        try{
            db.beginTransaction();

            ContentValues values = new ContentValues();
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm;ss", new Locale("pt","br"));
            String createdDate = sdf.format(new Date());

            values.put("type",type);
            values.put("response", response);
            values.put("createdDate", createdDate);

            calcId = db.insertOrThrow("calc", null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("SQLite", e.getMessage(), e);
        } finally {
            if(db.isOpen()){
                db.endTransaction();
            }
        }
        return calcId;
    }
}
