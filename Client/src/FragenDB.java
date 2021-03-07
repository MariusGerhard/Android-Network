package de.bib.dozkue.quiz;

import android.content.ContentProvider;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FragenDB extends SQLiteOpenHelper {
   private final static String LOG_TAG = "FRAGEN_DB";
   Context context;
   SQLiteDatabase db;

   // Konstruktor
   public FragenDB(Context context) {
      super(context, "QuizFragen.db", null, 1);
      this.context = context;
      this.db = getWritableDatabase();
   }

   //--------------------------------------------------------------------------
   @Override
   public void onCreate(SQLiteDatabase db) {
      this.db = db;
      // FragenDB neu anlegen
      try {
         // Tabelle anlegen
         String sql = "CREATE TABLE FRAGEN (_id INTEGER PRIMARY KEY AUTOINCREMENT, FRAGE TEXT, ANTWORTA TEXT, ANTWORTB TEXT, ANTWORTC TEXT, ANTWORTD TEXT)";
         db.execSQL(sql);
      }
      catch(Exception e) {
         Log.e(LOG_TAG, "onCreate: "+e.getMessage());
      }
      kopiereDatenAusCSVinDB(db);
   }

   //--------------------------------------------------------------------------
   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      // auf Versionswechsel reagieren -- hier nicht notwendig
   }

   //--------------------------------------------------------------------------
   private void kopiereDatenAusCSVinDB(SQLiteDatabase db) {
      // CSV lesen und in Datenbank schreiben
//      SQLiteDatabase db = getWritableDatabase();
      InputStream csvIS = null;
      try {
         csvIS = context.getResources().openRawResource(R.raw.quizfragen);
      } catch (Exception e) {
         Log.e(LOG_TAG, "openRawResource: "+e.getMessage());
         return;
      }

      try ( BufferedReader br = new BufferedReader(new InputStreamReader( csvIS )) ) {
         br.readLine();       // Kopfzeile überspringen

         ArrayList<Frage> fragenListe = new ArrayList<>();
         while ( true ) {
            String s = br.readLine();
            if ( s == null )
               break;
            String[] cols = s.split(";");
            if ( cols.length == 5 ) {
               Frage f = new Frage(cols[0], cols[1], cols[2], cols[3], cols[4]);
               fragenListe.add(f);
            }
         }
         schreibe(fragenListe);
         br.close();
      } catch (Exception e) {
         Log.e(LOG_TAG, "kopiereDatenAusCSVinDB "+e.getMessage());
      }

   }

   //----------------------------------------------------------------------------
   // schreibe Tabelle mit neuen Fragen
   public void schreibe(ArrayList<Frage> fragenListe) {
//      SQLiteDatabase db = getWritableDatabase();
      try {
         db.delete("FRAGEN",null,null);  // alte Tabelleninhalte löschen
         db.execSQL("Delete from sqlite_sequence where name='FRAGEN'" );
      } catch (Exception e ) {
         Log.e(LOG_TAG, "Fehler beim löschen: " + e.getMessage());
      }
      for ( int i=0 ; i<fragenListe.size() ; i++ ) {
         ArrayList antworten = fragenListe.get(i).getAntworten();
         String sql = "INSERT INTO FRAGEN (FRAGE, ANTWORTA, ANTWORTB, ANTWORTC, ANTWORTD) VALUES " +
                 "('" + fragenListe.get(i).getFrage() + "','" + antworten.get(0) + "','"+ antworten.get(1) + "','"+ antworten.get(2) + "','"+ antworten.get(3) + "')";
         try {
            db.execSQL(sql);
         } catch (SQLException e) {
            Log.e(LOG_TAG, "INSERT " + e.getMessage());
         }
      }
   }


   //----------------------------------------------------------------------------
   // Anzahl Einträge in DB ermitteln
   public long anzahlFragen() {
      long n = 0;
      try {
//         SQLiteDatabase db = getReadableDatabase();
         n = DatabaseUtils.queryNumEntries(db, "FRAGEN");
      } catch(Exception e) {
         Log.e(LOG_TAG, "anzahlFrageninDB(): "+e.getMessage());
      }
      return n;
   }


   //----------------------------------------------------------------------------
   //
   // ----------------------------------------------------------------------------
   // lese Frage mit id  aus db
   public Frage leseFrage(long id) {
      Frage f = null;

      Cursor cursor = null;
      String   select  = "_id == "+id;
      String[] spalten = new String[]{ "FRAGE", "ANTWORTA", "ANTWORTB", "ANTWORTC", "ANTWORTD" };
      try {
//         SQLiteDatabase db = getReadableDatabase();
         cursor = db.query("Fragen", spalten, select, null, null, null, null);
      } catch(Exception e) {
         Log.e(LOG_TAG, "query: "+e.getMessage());
      }

      if ( cursor != null ) {
         Log.v(LOG_TAG, "Cursor.count="+cursor.getCount() );
         cursor.moveToNext();
         f = new Frage( cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
      }
      cursor.close();
      return f;
   }


   //----------------------------------------------------------------------------
   // lese n zufällige Fragen aus Tabelle
   public ArrayList<Frage> leseFragenListe(long n) {
      Cursor cursor = null;
      try {
         cursor = db.rawQuery( String.format("select * from FRAGEN order by random() limit %d", n), null );
      } catch (Exception e ) {
         Log.e(LOG_TAG, "Fehler beim löschen: " + e.getMessage());
      }
      ArrayList<Frage> fragenListe = new ArrayList<>();
      if ( cursor != null ) {
         Log.v(LOG_TAG, "Cursor.count="+cursor.getCount() );
         cursor.moveToFirst();
         while (!cursor.isAfterLast()) {
            Frage f = new Frage( cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
            fragenListe.add(f);
            cursor.moveToNext();
         }
         cursor.close();
      }
      return fragenListe;
   }

}
