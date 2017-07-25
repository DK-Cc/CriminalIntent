package com.ckt.cc.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ckt.cc.database.CrimeBaseHelper;
import com.ckt.cc.database.CrimeCursorWrapper;
import com.ckt.cc.database.CrimeDbSchema.CrimeTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Cc on 2017/7/20.
 */

public class CrimeLab {

    private static final CrimeLab CRIME_LAB = new CrimeLab();

    private static Context sContext;
    private static SQLiteDatabase sDatabase;

    private CrimeLab() {
    }

    public static CrimeLab get(Context context) {
        sContext = context.getApplicationContext();
        sDatabase = new CrimeBaseHelper(sContext).getWritableDatabase();

        return CRIME_LAB;
    }

    public void addCrime(Crime crime) {
        ContentValues values = getContentValues(crime);

        sDatabase.insert(CrimeTable.NAME, null, values);
    }

    public List<Crime> getCrimes() {
        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper cursor = queryCrimes(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return crimes;
    }

    public Crime getCrime(UUID id) {
        CrimeCursorWrapper cursor = queryCrimes(CrimeTable.Cols.UUID + " =?", new String[]{id
                .toString()});

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }

    }

    public File getPhotoFile(Crime crime) {
        File filesDir = sContext.getFilesDir();
        return new File(filesDir, crime.getPhotoFilename());
    }

    public void updateCrime(Crime crime) {
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);

        sDatabase.update(CrimeTable.NAME, values, CrimeTable.Cols.UUID + " =?", new
                String[]{uuidString});
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = sDatabase.query(CrimeTable.NAME, null, whereClause, whereArgs, null,
                null, null);

        return new CrimeCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
        values.put(CrimeTable.Cols.SUSPECT, crime.getSuspect());

        return values;
    }

}