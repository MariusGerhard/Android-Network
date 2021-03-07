package de.bib.dozkue.quiz;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class QuizActivity extends Activity {
   TextView tvFrage;
   Button[] btnAntworten;
   TextView tvSpielstand;

   Frage aktuelleFrage;
   ArrayList<Frage> fragenListe;
   int spielstandRichtig;
   int spielstandFalsch;
   int anzahlFragen;

   int btnColorDefault;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_quiz);
      getActionBar().setDisplayShowHomeEnabled(true);
      getActionBar().setIcon(R.mipmap.ic_launcher_round);
      getActionBar().setTitle("  Quiz");

      tvFrage   = findViewById(R.id.tvFrage);
      btnAntworten = new Button[4];
      btnAntworten[0] = findViewById(R.id.btnAntwortA);
      btnAntworten[1] = findViewById(R.id.btnAntwortB);
      btnAntworten[2] = findViewById(R.id.btnAntwortC);
      btnAntworten[3] = findViewById(R.id.btnAntwortD);
      tvSpielstand = findViewById(R.id.tvSpielstand);

      neuesSpiel();
   }

   private void neuesSpiel() {
      spielstandRichtig = 0;
      spielstandFalsch = 0;
      anzahlFragen = 3;
      tvSpielstand.setText("");
      fragenListe = FragenListe.wuerfelFragenListe(getApplicationContext(), anzahlFragen);
      neueFrage();
   }

   private void neueFrage() {
      aktuelleFrage = fragenListe.get(spielstandRichtig+spielstandFalsch);
      tvFrage.setText(aktuelleFrage.getFrage());
      aktuelleFrage.mischeAntworten();
      for ( int i=0 ; i<btnAntworten.length ; i++ ) {
         btnAntworten[i].setText(aktuelleFrage.getAntworten().get(i));
         btnAntworten[i].setBackgroundColor(Color.LTGRAY);;
      }
   }

   int wait = 0;
   public void doAntwort(View v) {
      if ( wait != 0 )
         return;

      Button b = (Button)(v);
      String a = b.getText().toString();

      if ( aktuelleFrage.getAntworten().get(aktuelleFrage.getRichtig()).equals(a) ) {
         spielstandRichtig++;
         v.setBackgroundColor(Color.GREEN);
         wait = 2000;
      }
      else {
         spielstandFalsch++;
         v.setBackgroundColor(Color.RED);
         btnAntworten[aktuelleFrage.getRichtig()].setBackgroundColor(Color.GREEN);
         wait = 5000;

      }
      tvSpielstand.setText( String.format("%d richtig, %d falsch von %d.",spielstandRichtig,spielstandFalsch,anzahlFragen));

      AsyncTask<Integer,Void,Void> at = new AsyncTask<Integer,Void,Void>() {
         @Override
         protected void onPostExecute(Void aVoid) {
            wait = 0;
            if ( spielstandRichtig + spielstandFalsch >= anzahlFragen ) {
               Intent ergebnis  = new Intent();
               ergebnis .putExtra("richtig", spielstandRichtig );
               ergebnis .putExtra("falsch", spielstandFalsch );
               setResult(Activity.RESULT_OK, ergebnis );
               finish();
            }
            else {
               neueFrage();
            }
         }

         @Override
         protected Void doInBackground(Integer... integers) {
            try {
               Thread.sleep(integers[0]);
            } catch (Exception e) {};

            return null;
         }
      };
      at.execute( wait );
   }
}
