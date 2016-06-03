package com.example.fd.sampler;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by FD on 02.06.2016.
 */
public class DataBaseHelper extends SQLiteOpenHelper implements BaseColumns {

    // имя базы данных
    private static final String DATABASE_NAME = "mainbase.db";
    // версия базы данных
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_TABLE_PATTERNS = "patterns";
    public static final String PATTERN_BPM_COLUMN = "bpm";
    public static final String PATTERN_STEP_COLUMN = "steps";
    public static final String PATTERN_NAME_COLUMN = "title";

    public static final String DATABASE_TABLE_TRACKS = "tracks";
    public static final String TRACK_PATTERN_ID_COLUMN = "patt_id";
    public static final String TRACK_TITLE_COLUMN = "title";
    public static final String TRACK_HITS_ARRAY_COLUMN = "hits_array";
    public static final String TRACK_VOLUME_COLUMN = "volume";
    public static final String TRACK_MUTE_COLUMN = "mute";
    public static final String TRACK_PATH_TO_SAMPLE_COLUMN = "instrument";

    private static final String PATTERNS_CREATE_SCRIPT = "CREATE TABLE "
            + DATABASE_TABLE_PATTERNS + " (" + BaseColumns._ID
            + " integer primary key autoincrement, " + PATTERN_NAME_COLUMN
            + " text not null, " + PATTERN_STEP_COLUMN + " integer, " + PATTERN_BPM_COLUMN
            + " integer);";

    private static final String TRACKS_CREATE_SCRIPT = "CREATE TABLE "
            + DATABASE_TABLE_TRACKS + " (" + BaseColumns._ID
            + " integer primary key autoincrement, " + TRACK_TITLE_COLUMN
            + " text not null, " + TRACK_HITS_ARRAY_COLUMN + " text not null, " + TRACK_VOLUME_COLUMN
            + " integer, " + TRACK_MUTE_COLUMN + " numeric, " + TRACK_PATH_TO_SAMPLE_COLUMN + " text, "
            + TRACK_PATTERN_ID_COLUMN + " integer not null);";
    private static final String FIRST_INITIALIZATION_OF_PATTERNS = "INSERT INTO " + DATABASE_TABLE_PATTERNS
            + " VALUES ('1', 'base_pattern', '130', '16'); ";
    private static final String FIRST_INITIALIZATION_OF_TRACKS = "INSERT INTO " + DATABASE_TABLE_TRACKS + " VALUES ('1', 'Kick', '1 5 9 14', '70', '0', 'something', '1'),('2', 'Snare', '5 14', '70', '0', 'something', '1'),('3', 'Hi-Hat', '1 3 5 7 9 11 13 15', '70', '0', 'something', '1');";




    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
    }

    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PATTERNS_CREATE_SCRIPT);
        db.execSQL(TRACKS_CREATE_SCRIPT);
        db.execSQL(FIRST_INITIALIZATION_OF_TRACKS);
        db.execSQL(FIRST_INITIALIZATION_OF_PATTERNS);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Запишем в журнал
        Log.w("SQLite", "Обновляемся с версии " + oldVersion + " на версию " + newVersion);

        // Удаляем старую таблицу и создаём новую
        db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE_PATTERNS);
        db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE_TRACKS);
        // Создаём новую таблицу
        onCreate(db);
    }


}
