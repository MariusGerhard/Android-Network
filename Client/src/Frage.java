package de.bib.dozkue.quiz;

import java.util.ArrayList;
import java.util.Collections;

public class Frage {
   private String frage;
   private ArrayList<String> antworten;
   private int richtig;

   public Frage(String frage, String antwortA, String antwortB, String antwortC, String antwortD ) {
      this.frage = frage;
      this.antworten = new ArrayList<String>();
      this.antworten.add(antwortA);
      this.antworten.add(antwortB);
      this.antworten.add(antwortC);
      this.antworten.add(antwortD);
      this.richtig = 0;
   }

   public void mischeAntworten() {
      String r = antworten.get(richtig);
      Collections.shuffle(antworten);
      for ( int i=0; i<antworten.size() ; i++) {
         if ( antworten.get(i).equals(r)) {
            richtig=i;
            break;
         }
      }
   }

   public String getFrage() {
      return frage;
   }

   public ArrayList<String> getAntworten() {
      return antworten;
   }

   public int getRichtig() {
      return richtig;
   }
}
