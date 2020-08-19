/************************************************
 *                                              *
 *                     MAIN                     *
 *                                              *
 ************************************************
 *
 */
package server;

import java.io.*;

import java.net.*;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import java.text.SimpleDateFormat;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sun.misc.Signal;
import sun.misc.SignalHandler;



/**
 * In questa classe viene implementato l'eseguibile di un thread utile per
 * l'aggioramento dei database sui rispettivi files json salvati su disco.
 * Il thread messo in esecuzione aggiornerà i files allo scadere di un
 * determinato tempo, settato nel file di configurazione.
 *
 * @class   DBsWriter
 * @author  Luca Canessa (Mat. 516639)
 * @version 1.0
 * @since   1.0
 */
class DBsWriter implements Runnable
	{
		//            //
		//  VARIABLES //
		//            //
		private Users       __udb = null; ///< database degli User
		private Friendships __fdb = null; ///< database delle amicizie tra gli User

		//          //
		//  METHODS //
		//          //

		/**
		 * Costruttore dello scrittore su disco dei database
		 *
		 * @param UDB database degli utentu
		 * @param FDB database delle amicizie degli utenti
		 */
		DBsWriter( Users UDB, Friendships FDB )
			{
				__fdb = FDB;
				__udb = UDB;
			}

		/**
		 * Scrive su file i database attraverso i rispettivi metodi predisposti per
		 * tale funzione, al termine della scrittura si mette in 'sleep' fino allo
		 * scadere del tempo che è stato impostato nel file di configuazione
		 */
		@Override
		public void run( )
			{
				long time = Main.parser.getTimeUpdater( );
				while( !Thread.currentThread().isInterrupted( ) )
					{
						__udb.writeONfile( );
						__fdb.writeONfile( );
						Main.logger( "DBs updated" );
						try
							{
								Thread.sleep( time );
							}
						catch( InterruptedException e )
							{
								__udb.writeONfile( );
								__fdb.writeONfile( );
							}
					}
			}
	}


/**
 * Classe principale di esecuzione del server. Tale classe rappresenta il
 * server in toto.
 *
 * @class   Main
 * @author  Luca Canessa (Mat. 516639)
 * @version 1.8
 * @since   1.0
 */
public class Main
	{
		public static   ConfParser 	      parser = null; ///< parser del file di configurazione
		private static  Users 			      UDB = null; ///< database degli User
		private static  Friendships	      FDB = null; ///< database delle amicizie tra gli User
		private static  ExecutorService   Tpool = null; ///< thread pool usato per i thread che gestiscono le richieste dei clients
		private static  ServerSocket      LISTENER = null; ///< socket predisposta per l'ascolto dei clients che vogliono collegarsi e per la loro accettazione
		private static  ArrayList<String> Dictionary = null; ///< dizionario delle parole italiane disponibili
		private static  Thread            updater = null; ///< thread per l'esecuzione del DBsWriter
		private static  PrintStream       logfile = null; ///< scrittore su file di un log del server

		/**
		 * Metodo principale di esecuzione del server. All'avvio del server
		 * controlla che gli venga passato il percorso al file di configurazione
		 * del server; inizializza il parser di configurazione; inizializza lo
		 * scrittore su file del log del server; inizializza i database 'users' e
		 * 'friendship'; inizializza il dizionario delle parole italiane;
		 * inizializza un handler dei segnali di terminazione, in modo che il
		 * server, quando viene chiuso, abbia sempre i dati consistenti e la
		 * situazione degli User aggiornata; inizializza il threadpool per la
		 * gestione dei tasks dei clients; inizializza la socket di ascolto per
		 * le richieste di connessione; inizializza e setta RMI per poter essere
		 * usato dai client; inizia il ciclo infinito che lo mette in ascolto di
		 * richieste di connessione
		 *
		 * @param args percorso al file di configurazione
		 */
		public static void main( String[] args )
			{
				if( args.length < 1 )
					{
						System.err.println( "Missing config path" );
						System.exit( -1 );
					}
				parser = new ConfParser( args[0] );
				try
					{
						logfile = new PrintStream( new FileOutputStream( parser.getLogfilePath( ), true ) );
					}
				catch( FileNotFoundException e )
					{
						e.printStackTrace( );
					}
				logger( "Staring" );
				UDB = Users.init( );
				FDB = Friendships.init( );
				Dictionary = getDictionary( parser.getDictionaryPath( ) );


				SignalHandler handler = new SignalHandler( )
					{
						@Override
						public void handle( Signal signal )
							{
								Main.logger( "Quitting" );
								if( updater != null )
									{
										UDB.shutdown( );
										updater.interrupt( );
										Tpool.shutdown( );
										while( !Tpool.isShutdown( ) )
											{
												Main.logger( "............................." );
											}
										try
											{
												Thread.sleep( 1000 );
											}
										catch( InterruptedException e )
											{
												e.printStackTrace( );
											}

									}
								Main.logger( "Goodbye\n\n\n" );
								System.exit( 0 );
							}
					};

				Signal.handle( new Signal( "HUP" ), handler );
				Signal.handle( new Signal( "INT" ), handler );


				Tpool = Executors.newCachedThreadPool( );
				Main.logger( "Threadpool created" );


				try
					{
						RegRMImplementation srv = new RegRMImplementation( UDB, FDB );
						RegRMInterface stub = (RegRMInterface) UnicastRemoteObject.exportObject( srv, parser.getRMIPort( ) );
						Registry reg = LocateRegistry.createRegistry( parser.getRMIPort( ) );

						reg.bind( "ServerRMI", stub );

						Main.logger( "RMI initialized" );
					}
				catch( RemoteException | AlreadyBoundException e )
					{
						e.printStackTrace();
						System.exit( -1 );
					}

				try
					{
						InetAddress myAddress = InetAddress.getByName( null );
						LISTENER = new ServerSocket( parser.getListnerPort( ), 0, myAddress ); //100 is the max connection in queue
						Main.logger( "Listener socket ready" );
					}
				catch( IOException e )
					{
						Main.logger( "problem to create the socket" );
						System.exit( -1 );
					}

				logger( "Server started" );

				updater = new Thread( new DBsWriter( UDB, FDB ) ); //update dbs
				updater.start( );

				while( true )
					{
						Socket newCliet = null;
						ClientTasks cTask = null;
						try
							{
								newCliet = LISTENER.accept( );
								cTask = new ClientTasks( UDB, FDB, newCliet );
								Tpool.execute( cTask );
							}
						catch( IOException e )
							{
								Main.logger( "Problem to accept a new client" );
							}
					}
			}

		/**
		 * Metodo statico predisposto per l'analisi e la scrittura in memoria del
		 * dizionario delle parole italiane. Legge una parola per riga e la
		 * inserisce in un 'ArrayList', saltando le righe commentate usando il
		 * carattere '#'. Restituisce il dizionario salvato in memoria
		 *
		 * @param filepath  percorso al dizionario salvato su disco
		 * @return result   ArrayList di stringhe contenente il dizionario
		 *                  null in caso di errore
		 */
		private static ArrayList<String> getDictionary(String filepath)
			{
				ArrayList<String> result = new ArrayList <String>( );

				File input = new File(filepath);
				Scanner scanner = null;
				try
					{
						scanner = new Scanner(new FileReader(input));
					}
				catch( FileNotFoundException e )
					{
						Main.logger( "Problem to load dictionary" );
						System.exit( -1 );
					}
				try
					{
						while( scanner.hasNextLine( ) )
						{
							String line = scanner.nextLine();
							if( !line.startsWith( "#" ) )
							{
								result.add(line);
							}
						}
					}
				finally
					{
						scanner.close();
						Main.logger( "Dictionary loaded" );
					}

				return result;
			}

		/**
		 * Estrazione dal file salvato su filesystem dei sets di parole necessarie
		 * per avviare la sfida, contrassegnati dal hash code del nickname del
		 * giocatore sfidante. Entrambi i set vengono salvati in un 'ArrayList'
		 * contenente due 'ArrayList' con ripsettivamente le parole italiane e
		 * quelle inglesi
		 *
		 * @param nickname  nickname del giocatore sfidante usato per la creazione
		 *                  dei file su disco
		 * @return  words 'ArrayList' di 'ArrayList' in cui vi sono le parole da
		 *          inviare ai giocatori e le loro traduzioni
		 */
		public static ArrayList<ArrayList<String>> getWords( String nickname )
			{
				BufferedReader it = null;
				BufferedReader en = null;
				ArrayList <ArrayList <String>> words = new ArrayList <ArrayList <String>>( );
				synchronized( Main.class )
					{
						try
							{
								it = new BufferedReader( new FileReader( "/tmp/it" + nickname.hashCode( ) ) );
								en = new BufferedReader( new FileReader( "/tmp/en" + nickname.hashCode( ) ) );
							}
						catch( FileNotFoundException e )
							{
								e.printStackTrace( );
							}

						String itW;
						String enW;
						try
							{
								ArrayList <String> itA = new ArrayList <String>( );
								while( ( itW = it.readLine( ) ) != null )
									{
										itA.add( itW );
									}
								ArrayList <String> enA = new ArrayList <String>( );
								while( ( enW = en.readLine( ) ) != null )
									{
										enA.add( enW );
									}

								it.close( );
								en.close( );

								words.add( itA );
								words.add( enA );
							}
						catch( IOException e )
							{
								e.printStackTrace( );
							}
					}
				return words;
			}

		/**
		 * Metodo statico predisposto per la selezione random di un numero di parole
		 * dal dizionario e la loro traduzione, per poter essere condivise con gli
		 * utenti che le hanno richieste. Estrapola quindi un numero di parole
		 * (numero impostato nel file di configurazione) dal dizionario, scelte a
		 * caso e controllando che non siano mai uguali. Queste parole vengono
		 * salvate in un 'ArrayList'. Per ogni parole nel 'ArrayList' viene eseguita
		 * la traduzione usando il servizio esterno 'mymemory.translated.net'
		 * attraverso le API che mette a disposizione. Tale servizio restituisce un
		 * JSON che viene analizzato e dal quale viene estratta la traduzione (in
		 * inglese) della parole richiesta. Tutte le parole tradotte vengono salvate
		 * in un altro 'ArrayList'. Entrambi gli 'ArrayList' contenenti le parole,
		 * vengono trascritti su un file nella cartella '/tmp' del filesystem,
		 * utilizzando come nome del file il codice hash del nickname del
		 * richiedente del set di parole.
		 *
		 * @param sender  nickname dell'User che richiede il set di parole
 		 */
		public synchronized static void setWords( String sender ) throws IOException, ParseException
			{
				Random rand = new Random(  );
				int maxW = parser.getNWords( );
				ArrayList<String> itWords = new ArrayList <String>(  );
				for( int i = 0; i < maxW; i++ )
					{
						int iw = rand.nextInt( Dictionary.size( ) );
						String w = Dictionary.get( iw );
						if( itWords.contains( w ) )
							{
								i--;
							}
						else
							{
								itWords.add( w );
							}
					}

				ArrayList<String> words_en = new ArrayList<String>( );
				for( String wordit:itWords )
					{
						String url = "https://api.mymemory.translated.net/get?q=" + wordit + "&langpair=it|en";
						JSONParser ret_tras = new JSONParser( );
						@SuppressWarnings( "resource" )
						JSONObject tmp = (JSONObject)ret_tras.parse( new Scanner( new URL( url ).openStream( ), "UTF-8" ).useDelimiter( "\\A" ).next( ));
						JSONObject rd =  (JSONObject) tmp.get( "responseData" );
						words_en.add( (String)rd.get( "translatedText" ) );
					}

				FileWriter it = new FileWriter( "/tmp/it" + sender.hashCode(), false );
				FileWriter en = new FileWriter( "/tmp/en" + sender.hashCode(), false );

				String its = "";
				String ens = "";
				for( String w: itWords )
					{
						its += w.toLowerCase() + "\n";
					}
				for( String w: words_en )
					{
						ens += w.toLowerCase() + "\n";
					}

				it.write( its );
				it.flush();
				it.close();
				en.write( ens );
				en.flush();
				en.close();
			}

		/**
		 * Aspetta che i giocatori abbiano terminato la partita per poter
		 * controllare i loro rispettivi punti guadagnati e decretare il vincitore
		 * del match
		 *
		 * @param U1  User 1 appartenente alla sfida in analisi
		 * @param U2  User 2 appartenente alla sfida in analisi
		 * @param udb database degli utenti
		 * @return  il nickname del vincitore della sfida
		 *          'TIE' nel caso in cui il match termini in parità
		 */
		public static String getWinner( User U1, User U2, Users udb )
			{
				while( !U1.getStatus( ).name().equals( ACK.ONLINE.name() ) || !U2.getStatus( ).name().equals( ACK.ONLINE.name() ) )
					{
						U1 = udb.getUser( U1.getID() );
						U2 = udb.getUser( U2.getID() );
					}

				try
					{
						Thread.sleep( 500 );
					}
				catch( InterruptedException e )
					{
						e.printStackTrace( );
					}

				if( U1.getChalScore( ) > U2.getChalScore() )
					{
						Main.logger( U1.getID() + " won" );
						return U1.getID( );
					}
				if( U1.getChalScore( ) < U2.getChalScore() )
					{
						Main.logger( U2.getID() + " won" );
						return U2.getID( );
					}
				else
					{
						Main.logger( U1.getID() + " & " + U2.getID() + " tie the match" );
						return "TIE";
					}
			}

		/**
		 * Metodo predisposto per il settaggio del messaggio da scrivere nel file
		 * di log. Viene impostata l'ora, i minuti, i secondi e i millisecondi del
		 * giorno nel mese nell'anno in cui si scrive il messaggio sul file.
		 *
		 * @param log   messaggio da scrivere su file
		 */
		public static void logger( String log )
			{
				SimpleDateFormat dateform = new SimpleDateFormat( "HH:mm:ss:SS dd.MM.yyyy" );

				System.setErr( logfile );

				System.err.println( dateform.format( new Date(  ) ) + "\t:::\t" + log );
			}

		/**
		 * Metodo statico utile per la creazione della porta univoca corrispondente
		 * al client. Per la sua creazione viene usato l'hash code del suo nome, e
		 * modificato opportunamente per essere usato.
		 *
		 * @param nickname  nickname dell'utente di cui si vuole conoscere la porta
		 *                  di comunicazione
		 * @return port   un numero compreso tra 1024 e 65535 corrispondente alla
		 *                porta del client
		 */
		public static int getUniPort( String nickname )
			{
				int port = nickname.hashCode( ) % 65535;
				if( port < 0 )
					{
						port = -port % 65535;
					}

				return ( port = ( port <= 1024) ? port += 1024 : port );
			}
	}
