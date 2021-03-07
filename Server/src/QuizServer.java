import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class QuizServer
{
	public static void main(String[] args) throws IOException
	{
		ServerSocket ss = new ServerSocket(61234,0,InetAddress.getByName("127.0.0.1"));
		System.out.printf("IP-Adresse des Systems: %s%n", InetAddress.getLocalHost());
		System.out.println("Server gestartet mit:");
		System.out.printf("   IP-Adresse: %s%n", ss.getInetAddress().getHostAddress().toString());
		System.out.printf("   Port: %d%n", ss.getLocalPort());
		
		while ( true ) {
			System.out.println("Warte auf Client...");
			
			Socket so = ss.accept();
			try ( BufferedReader in  = new BufferedReader(new InputStreamReader(so.getInputStream(),"UTF-8"));
					PrintWriter    out = new PrintWriter( new BufferedWriter(new OutputStreamWriter(so.getOutputStream(),"UTF-8"))); )
			{
				while ( true )
				{
					String command = in.readLine();
					if ( command == null )
						break;
					System.out.printf( "Kommando '%s' empfangen.%n", command );
					
					if ( command.toUpperCase().equals("GET FRAGEN") ) {
						out.println("Was ist eine �Grasm�cke�?;Ein Singvogel;Ein Heuschrecke;Eine M�cke;Eine Blume"); 
						out.println("Wo findet man eine �Hypotenuse�?;In einem rechtwinkligen Dreieck;In der griechischen Mythologie;Im Turbolader eines Motors;Im menchlichen K�rper"); 
						out.println("Wie viele L�sungen hat x�=2?;Zwei;Eine;unendlich viele;keine"); 
						out.println("Die Hauptstatt der Schweiz ist ...;keine;Genf;Bern;Z�rich"); 
						out.println("Welcher Fluss ist der l�ngste?;Wolga;Rhein;Donau;Rhone"); 
						out.println("Welche Stadt liegt am n�rdlichsten?;Prag;Paris;N�rnberg;Stuttgart"); 
						out.println("Welches ist der k�rzeste Fluss?;Pader;Lippe;Alme;Stever"); 
						out.println("Was ist ein �Abakus�?;Eine Rechenhilfe;Ein Zauberspruch;Eine Stechpalme;Ein Rohrverbindungsst�ck"); 
						out.println("Was ist ein Abakus?;Eine Rechenhilfe;Ein Zauberspruch;Eine Stechpalme;Ein Rohrverbindungsst�ck"); 
						out.println("Ein Diamand besteht aus?;Kohlenstoff;Phosphor;Lithium;Cobalt"); 
						out.println("Ein �Palatschinken� ist ...;ein Pfannkuchen;aus Rindfleisch;eine K�sesorte;ein Gem�seauflauf"); 
						out.flush();
						break;
					}
					// keine anderen Kommandos implementiert
				}
				so.close();
			}
			catch (IOException e) { e.printStackTrace(); }
		}
	}
}
