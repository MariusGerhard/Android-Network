package de.bib.dozkue.quiz;

import android.util.Log;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

public class Netzwerk {
   private static final String LOG_TAG = "NETZWERK";

   public static ArrayList<String> getFragen(String ip, int port ) throws IOException {
      Socket so = null;
      try {
         so = new Socket();
         so.connect(new InetSocketAddress(InetAddress.getByName(ip), port ),1000);
      } catch ( Exception e ) {
         Log.e( LOG_TAG, "Gerät nicht erreichbar: "+e.getMessage() );
         throw new IOException( "Gerät nicht erreichbar\n"+e.getMessage() );
      }

      Log.i( LOG_TAG, String.format("Client: verbunden mit %s:%d%n", so.getInetAddress(), so.getPort()));

      ArrayList<String> liste = new ArrayList<String>();
      try (BufferedReader in  = new BufferedReader( new InputStreamReader(so.getInputStream(),"UTF-8"));
           PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(so.getOutputStream(),"UTF-8"))); )
      {
         out.println("GET FRAGEN");
         out.flush();
         Log.i(LOG_TAG, "Anfrage gesendet");

         while ( true ) {
            String s = in.readLine();
            if ( s == null )
               break;
            liste.add(s);
         }
      }
      catch (Exception e) {
         Log.e( LOG_TAG, e.getMessage() );
         throw new IOException( "Lesefehler: " + e.getMessage() );
      }
      try {
         so.close();
      } catch (Exception e) {}
      return liste;
   }
}

