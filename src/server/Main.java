package server;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.net.*;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



class ConfParser
	{
		private String __confpath						= null;
		private JSONObject __conf						= null;
		
		
		public ConfParser( String confPath )
			{
				JSONParser parser = new JSONParser( );				
				__confpath = confPath;
				
						
				try
					{
						__conf = (JSONObject)parser.parse( new FileReader( __confpath ) );
					}
				catch( FileNotFoundException e ) 
					{
						System.out.println( "Config file does not exist" );
						System.exit( -1 );
					}
				catch( IOException e )
					{
						System.out.println( "Problem to open config file" );
						System.exit( -1 );
					}
				catch( ParseException e )
					{
						System.out.println( e );
						System.exit( -1 );
					}
			}
		
		
		public String getUsersPath( )
			{
				JSONObject field = (JSONObject) __conf.get( "DB" );
				String path = (String) field.get( "userspath" );
				
				if( path.equals( "null" ) || path.equals( "" ) )
					{
						System.err.println( "Error: Empty Path 'userspath'" );
						System.exit( -1 );
					}
				
				return path;
			}
		
		public String getFriendPath( )
			{
				JSONObject field = (JSONObject) __conf.get( "DB" );
				String path = (String) field.get( "friendpath" );
				
				if( path.equals( "null" ) || path.equals( "" ) )
					{
						System.err.println( "Error: Empty Path 'friendpath'" );
						System.exit( -1 );
					}
				
				return path;
			}
		
		public int getRMIPort( )
			{
				JSONObject field = (JSONObject) __conf.get( "SERVER" );
				String path = (String) field.get( "rmiport" );
				
				if( path.equals( "null" ) || path.equals( "" ) )
					{
						System.err.println( "Error: Empty port RMI" );
						System.exit( -1 );
					}
				
				int port = Integer.valueOf( path );
				
				return port;
			}

		public int getMaxConnections( )
			{
				JSONObject field = (JSONObject) __conf.get( "SERVER" );
				String s = (String)field.get( "maxconn" );
				if( s.equals( "null" ) || s.equals( "" ) )
					{
						System.err.println( "Error: Empty port RMI" );
						System.exit( -1 );
					}

				return Integer.valueOf( s );
			}

		public int getListnerPort( )
			{
				JSONObject field = (JSONObject) __conf.get( "SERVER" );
				String s = (String)field.get( "port" );
				if( s.equals( "null" ) || s.equals( "" ) )
					{
						System.err.println( "Error: Empty port RMI" );
						System.exit( -1 );
					}

				return Integer.valueOf( s );
			}
		
	}




public class Main
	{
		
		private static  ConfParser 	      parser = null;
		private static  Users 			      UDB = null;
		private static  Friendships	      FDB = null;
		private static  ExecutorService   Tpool = null;
		private static  ServerSocket      LISTENER = null;
	
		
		public static void main( String[] args )
			{
				if( args.length < 1 )
					{
						System.err.println( "Missing config path" );
						System.exit( -1 );
					}

				parser = new ConfParser( args[0] );
				UDB = Users.init( parser.getUsersPath( ) );
				FDB = Friendships.init( parser.getFriendPath( ) );

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
	}
