package de.bib.dozkue.quiz;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class FragenListe {

//   private static ArrayList<Frage> liste = null;
//   public static Frage wuerfelFrage() {
//      if ( liste == null ) {
//         liste = new ArrayList<Frage>();
//         liste.add( new Frage("Was ist nach Douglas Adams Roman 'Per Anhalter durch die Galaxis' die Antwort auf die Frage aller Fragen?", "42","4711", "17", "0815"));
//         liste.add( new Frage("Wie lange dauerte der dreizigjährige Krieg?","30 Jahre", "28 Jahre", "117 Jahre", "6 Tage"));
//         liste.add( new Frage("Welches Spielt spielt man ohne Schläger", "Dart", "Golf", "Polo", "Tennis"));
//         liste.add( new Frage("Wie lautet die vierte Wurzel aus 81", "3", "9", "7", "3.14"));
//         liste.add( new Frage("Wie bestellt James Bond seinen Martini", "geschüttelt", "gerührt", "auf Eis", "alkoholfrei"));
//         liste.add( new Frage("Welches ist ein wichtiger Nährstoff der Milch?", "Kalzium", "Natrium", "Silizium", "Magnesium"));
//         liste.add( new Frage("Eine Rosine ist eine getrocknete ...", "Weintraube", "Kaper", "Dattel", "Zwetschge"));
//         liste.add( new Frage("Welcher Venezianer reiste als einer der ersten Europäer nach Asien?", "marco Polo", "Paulo Passat", "Eros Jetta", "Silvio Golf" ));
//         liste.add( new Frage("Welcher dieser Flüsse mündet in das Tote Meer?", "Jordan", "Euphrat", "Tigris", "Nil" ));
//         liste.add( new Frage("Wo wurde Wolfgang Amadeus Mozart geboren?", "Salzburg", "Linz", "München", "Wien" ));
//         liste.add( new Frage("Auf der kanadischen Flagge befindet sich das Blatt eines ...", "Ahorn", "Kastanie", "Eiche", "Buche" ));
//         liste.add( new Frage("Die Haupstatt von australien ist ...?", "Canberra", "Sydney", "Perth", "Melbourne" ));
//         liste.add( new Frage("Durch Pudapest fließt die ...", "Donau", "Moldau", "Oder", "Wolga" ));
//         liste.add( new Frage("Welches Tier gehört nicht zu der Familie der Kamele?", "Maultier", "Alpaka", "Lama", "Trampeltier" ));
//         liste.add( new Frage("In einer Schulklasse sind 18 Jungen und 9 Mädchen. Wie viele Prozent der Klasse sind weiblich?", "33%", "40%", "67%", "27%" ));
//         liste.add( new Frage("Wie groß ist die zweite Potenz von 14?", "196", "154", "140", "28" ));
//         liste.add( new Frage("Was ist keine Primzahl?", "63", "43", "53", "23" ));
//         liste.add( new Frage("Wie viele Monde hat die Venus?", "0", "1", "2", "3" ));
//         liste.add( new Frage("Wie viele Atome enthält ein Wassermolekühl?", "3", "2", "1", "3" ));
//         liste.add( new Frage("der Buchstabe 'Z' ist das Symbol für die ...", "Ganzen Zahlen", "Rationale Zahlen", "Natürliche Zahlen", "Reelle Zahlen" ));
////         liste.add( new Frage("?", "", "", "", "" ));
//      }
//
//      int n = (int)(Math.random() * liste.size());
//      return liste.get(n);
//   }


   public static ArrayList<Frage> wuerfelFragenListe(Context context, long n) {
      FragenDB db = new FragenDB( context );
      long m = db.anzahlFragen();
      if ( m<n )
         n = m;
      return db.leseFragenListe(n);
   }

   public static void ladeFragenVomServer(Context context, String ip, int port) throws IOException {
      ArrayList<String> list = Netzwerk.getFragen(ip, port);
      for (int i = 0; i < list.size(); i++)
         Log.d("FRAGEN", list.get(i));

      ArrayList<Frage> fragenListe = new ArrayList<>();
      for (int i = 0; i < list.size(); i++) {
         String[] s = list.get(i).split(";");
         if ( s.length == 5 ) {
            Frage f = new Frage( s[0], s[1], s[2], s[3], s[4] );
            fragenListe.add(f);
         }
         FragenDB db = new FragenDB( context );
         db.schreibe(fragenListe);
      }
   }
}
