package de.bib.dozkue.quiz;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MenuActivity extends Activity {
   TextView tvErgebnis;
   Button btnNeuesSpiel;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_menu);
      getActionBar().setDisplayShowHomeEnabled(true);
      getActionBar().setIcon(R.mipmap.ic_launcher_round);
      getActionBar().setTitle("  Quiz");

      tvErgebnis = findViewById(R.id.tvErgebnis);
      tvErgebnis.setText("");
      btnNeuesSpiel = findViewById(R.id.btnNeuesSpiel);
   }

   public void doNeuesSpiel( View v) {
      Intent intent = new Intent(getApplicationContext(), QuizActivity.class);
      startActivityForResult(intent, 1);
   }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      Bundle ergebnis = data.getExtras();
      int richtig = ergebnis.getInt("richtig");
      int falsch = ergebnis.getInt("falsch");
      tvErgebnis.setText(String.format("Sie haben %d von %d Fragen richtig beantwortet.", richtig, richtig+falsch));
   }

   public void doSpielEnde( View v) {
      finishAffinity();
      System.exit(0);
   }
   public void doFragenLaden( View v) {
      SharedPreferences prefs = getSharedPreferences("IP_Settings", getApplicationContext().MODE_PRIVATE);
      final String ip = prefs.getString("ip", "10.0.2.2");
      final int port = prefs.getInt("port", 61234);

      class doOnNetwork extends AsyncTask<Void, Void, String> {
         @Override
         protected String doInBackground(Void... voids) {
            try {
               FragenListe.ladeFragenVomServer(getApplicationContext(),ip,port);
//               ArrayList<String> list = Netzwerk.getFragen(ip, port);
//               for (int i = 0; i < list.size(); i++)
//                  Log.d("FRAGEN", list.get(i));

            } catch (IOException e) {
               return e.getMessage();
            }
            return null;
         }

         @Override
         protected void onPostExecute(String errorText) {
            if (errorText != null) {
               Toast.makeText(MenuActivity.this, errorText, Toast.LENGTH_LONG).show();
            }
         }
      }

      doOnNetwork nw = new doOnNetwork();
      nw.execute();
   }
}
