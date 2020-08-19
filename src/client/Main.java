/************************************************
 *                                              *
 *                    MAIN                      *
 *                                              *
 ************************************************
 *
 */
package client;


import server.RegRMInterface;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.Socket;

import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


/**
 * In questa enumerazione vengono implementati i messaggi che il server riceve
 * dal client
 *
 * @class   ClientMSG
 * @author  Luca Canessa (Mat. 516639)
 * @version 1.1
 * @since   1.0
 */
enum ClientMSG
	{
		LOGIN, ///< richiesta di login
		LOGOUT, ///< richiesta di logout
		ADDFRIEND, ///< richiesta di aggiunta di amico alla lista
		GETFRIENDS, ///< richiesta lista amicizie
		GETNFRIENDS, ///< richiesto numero di amici
		STARTCH, ///< richiesta di inizio di una sfida
		ACCEPTEDCH, ///< accettazione della sfida arrivata
		UPDATEINFO, ///< richiesta di aggiornamento dei dati
		GETPOINTS, ///< richiesta punti totalizzati
		GETRANK,  ///< richiesta della classifica
		RMUSER, ///< richiesta rimozione dell'User
		RMFRIEND; ///< richiesta rimozione dell'amico
	}


/**
 * Classe principale che permette di eseguire il client
 *
 * @class   Main
 * @author  Luca Canessa (Mat. 516639)
 * @version 1.6
 * @since   1.0
 */
public class Main
	{
		//            //
		//  VARIABLES //
		//            //
		protected static Registry         reg = null; ///< registro dei serivizi del server
		protected static RegRMInterface   stub = null; ///< interfaccia RMI del server
		private   static Socket           socket = null; ///< socket con cui connettersi al server
		private   static BufferedReader   inBuff = null; ///< buffer di ingresso per la socket
		private   static DataOutputStream outBuff = null; ///< buffer di uscita per la socket
		public    static ConfParser       parser = null; ///< parser della configurazione

		//          //
		//  METHODS //
		//          //

		/**
		 * Metodo principale per l'avvio del client. Tale metodo ha il compito di
		 * settare il registro dei servizi su RMI del server e il servizio
		 * desiderato (iscrizione dell'utente); di inizializzare la socket e i
		 * buffers di comunicazione con il server ed avviare la connessione; di
		 * inizializzare il gestore della grafica; e di aggioranare il parametro di
		 * timeout sul file di configurazione, dopo che lo ha ricevuto dal server.
		 * Tutti i valori necessari per l'inizializzazione vengono prelevati dal
		 * file di configurazione salvato nel filesystem
		 *
		 * @param args  percorso al file di configurazione
		 * @throws IOException nel caso di problemi con RMI
		 */
		public static void main( String[] args ) throws IOException
			{
				if( args.length < 1 )
					{
						System.out.println( "config not exists" );
						System.exit( -1 );
					}

				parser = new ConfParser( args[0] );

				try
					{
						reg = LocateRegistry.getRegistry( parser.getRMIPort() );
						stub = (RegRMInterface) reg.lookup( "ServerRMI" );
					}
				catch( IOException | NotBoundException e )
					{
						System.exit( -1 );
					}

				connect( );
				String timeoutgame = Main.recv( );
				parser.setTimeoutGame( timeoutgame );
				CGUI t = new CGUI( "Word Quizzle Game" );

			}

		/**
		 * Metodo statico usato per individuare il numero di porta su cui
		 * comunicare quando viene usato il protocollo UDP. Numero ricavato
		 * attraverso l'hash code del nickname dell'utente
		 *
		 * @param myname  nickname dell'utente
		 * @return port  numero di porta da utilizzare per la socket UDP
		 */
		public static int getMyPort( String myname )
			{
				int port = myname.hashCode( ) % 65535;
				if( port < 0 )
					{
						port = -port % 65535;
					}

				port = ( port <= 1024) ? port += 1024 : port;

				return port;
			}

		/**
		 * Ha il compito di inviare il messaggio passatogli come parametro al
		 * server attraverso la socket di comunicazione.
		 *
		 * @param toSend  messaggio da inviare al server
		 * @throws IOException
		 */
		public static void send( String toSend ) throws IOException
			{
				outBuff.writeBytes( toSend + "\n" );
			}

		/**
		 * Ha il compito di ricevere dati dal server e trascriverli in una stringa
		 * per poter essere utlilizzati in altre parti del codice
		 *
		 * @return  s String ottenuta leggendo il buffer di ingresso della socket
		 * @throws IOException
		 */
		public static String recv( ) throws IOException
			{
				String s = null;
				s = inBuff.readLine( );

				return s;
			}

		/**
		 * Metodo con il compito di instaurare la connessione con il server usando
		 * il protocollo di trasporto TCP, e di inizializzare i buffer di ingresso
		 * e uscita.
		 */
		public static void connect( )
			{
				try
					{
						socket = new Socket( "localhost", 46058 );
						inBuff = new BufferedReader( new InputStreamReader( socket.getInputStream( ) ) );
						outBuff = new DataOutputStream( socket.getOutputStream( ) );
					}
				catch( IOException e )
					{
						e.printStackTrace( );
					}
			}
	}
