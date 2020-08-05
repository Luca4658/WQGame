package server;

import java.io.*;

import java.net.*;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
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
						System.err.println( "DBs updated" );
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

		
		public static void main( String[] args )
			{
				if( args.length < 1 )
					{
						System.err.println( "Missing config path" );
						System.exit( -1 );
					}

				parser = new ConfParser( args[0] );
				UDB = Users.init( );
				FDB = Friendships.init( );
				Dictionary = getDictionary( parser.getDictionaryPath( ) );

				SignalHandler handler = new SignalHandler( )
					{
						@Override
						public void handle( Signal signal )
							{
								System.err.println( "Quitting" );
								if( updater != null )
									{
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
								System.err.println( "Goodbye " );
								System.exit( 0 );
							}
					};

				Signal.handle( new Signal( "HUP" ), handler );
				Signal.handle( new Signal( "INT" ), handler );


				Tpool = Executors.newCachedThreadPool( );

				try
					{
						RegRMImplementation srv = new RegRMImplementation( UDB, FDB );
						RegRMInterface stub = (RegRMInterface) UnicastRemoteObject.exportObject( srv, parser.getRMIPort( ) );
						Registry reg = LocateRegistry.createRegistry( parser.getRMIPort( ) );

						reg.bind( "ServerRMI", stub );
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
					}
				catch( IOException e )
					{
						System.err.println( "problem to create the socket" );
						System.exit( -1 );
					}

				updater = new Thread( new DBsWriter( UDB, FDB ) ); //update dbs
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
								System.err.println( "Problem to accept a new client" );
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
						System.err.println( "Problem to load dictionary" );
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
					}

				return result;
			}


		public static void setWords( String username ) throws IOException, ParseException
			{
				ArrayList<ArrayList<String>> matrix = new ArrayList <ArrayList<String>>(  );
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

				matrix.add( itWords );

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

				matrix.add( words_en );


				System.out.println( username.hashCode( ) );
				FileOutputStream wrt = new FileOutputStream( "/tmp/" + username.hashCode( ) );
				ObjectOutputStream out = new ObjectOutputStream( wrt );

				out.writeObject( matrix );
				out.flush( );
				out.close();
			}


		public static String getWinner( User U1, User U2 )
			{
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
	}
