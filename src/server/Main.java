package server;

import java.io.*;

import java.net.*;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sun.misc.Signal;
import sun.misc.SignalHandler;



class DBsWriter implements Runnable
	{
		private Users __udb = null;
		private Friendships __fdb = null;

		DBsWriter( Users UDB, Friendships FDB )
			{
				__fdb = FDB;
				__udb = UDB;
			}

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

public class Main
	{
		public static   ConfParser 	      parser = null;
		private static  Users 			      UDB = null;
		private static  Friendships	      FDB = null;
		private static  ExecutorService   Tpool = null;
		private static  ServerSocket      LISTENER = null;
		private static  ArrayList<String> Dictionary = null;
		private static  Thread            updater = null;
		private static  PrintStream       logfile = null;

		
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
												System.out.println( "............................." );
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
						its += w + "\n";
					}

				for( String w: words_en )
					{
						ens += w + "\n";
					}

				System.out.println( its );
				System.out.println( ens );

				it.write( its );
				it.flush();
				it.close();
				en.write( ens );
				en.flush();
				en.close();

			}


		public static String getWinner( User U1, User U2 )
			{
				while( U1.getStatus( ) != ACK.ONLINE && U2.getStatus() != ACK.ONLINE );

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
						return U1.getID( );
					}
				if( U1.getChalScore( ) < U2.getChalScore() )
					{
						return U2.getID( );
					}
				else
					{
						return "TIE";
					}
			}


		public static void logger( String log )
			{
				SimpleDateFormat dateform = new SimpleDateFormat( "HH:mm:ss:SS dd.MM.yyyy" );

				System.setErr( logfile );

				System.err.println( dateform.format( new Date(  ) ) + "\t:::\t" + log );
			}


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
