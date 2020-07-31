package server;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

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
		
	}




public class Main
	{
		
		private static ConfParser 	parser = null;
		private static	Users 			UDB = null;
		private static	Friendships	FDB = null;
	
		
		public static void main( String[] args )
			{
				parser = new ConfParser( args[0] );
				UDB = Users.init( parser.getUsersPath( ) );
				FDB = Friendships.init( parser.getFriendPath( ) );

				
//				try
//					{
//						RegRMImplementation srv = new RegRMImplementation( UDB, FDB );
//						RegRMInterface stub = (RegRMInterface) UnicastRemoteObject.exportObject( srv, parser.getRMIPort( ) );
//						Registry reg = LocateRegistry.createRegistry( parser.getRMIPort( ) );
//						
//						reg.bind( "ServerRMI", stub );
//					} 
//				catch( RemoteException | AlreadyBoundException e )
//					{
//						e.printStackTrace();
//						System.exit( -1 );
//					}
//				
//				while( true )
//					{
//						UDB.writeONfile( );
//						FDB.writeONfile( );
//						try
//							{
//								Thread.sleep( 2000 );
//							} catch( InterruptedException e )
//							{
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//					}
				
				UDB.insertUser( new User( "Luca1", "Password" ), FDB );
				UDB.insertUser( new User( "Luca2", "Password" ), FDB );
				UDB.insertUser( new User( "Luca3", "Password" ), FDB );
				UDB.insertUser( new User( "Luca4", "Password" ), FDB );
				UDB.insertUser( (new User( "Luca5", "Password" )), FDB );
				
				System.out.println( UDB.getRanking( ) );
				
				FDB.writeONfile( );
				UDB.writeONfile( );
				
				for( int i = 0; i < 5; i++ )
					{
						int punti = (int) ( Math.random( ) *( 60 - 1 ) );
						String l = "Luca" + (i+1);
						if( l.equals( "Luca2" ) )
							{
								FDB.addFriend( UDB.getUser( l ), UDB.getUser( "Luca2" ) );
								FDB.addFriend( UDB.getUser( l ), UDB.getUser( "Luca3" ) );
								FDB.addFriend( UDB.getUser( l ), UDB.getUser( "Luca4" ) );
								FDB.addFriend( UDB.getUser( l ), UDB.getUser( "Luca5" ) );
							}
						User u = UDB.getUser( l );
						u.setTScore( punti );
						UDB.updateUser( u );
					}
				
				FDB.writeONfile( );
				UDB.writeONfile( );
				
				HashMap<String, Integer> ranking = UDB.getRanking( );
				HashMap<String, Integer> myranking = new HashMap<String, Integer>( );
				JSONArray friendlist = FDB.getFriends( UDB.getUser( "Luca2" ) );
				
				for( int i = 0; i < friendlist.size( ); i++ )
					{
						String friend = (String) friendlist.get( i );
						
						myranking.put( friend, ranking.get( friend ) );
					}
				
				
				
				
				System.out.println( UDB.getRanking( ) + "mh\n" + myranking.toString( ) );		
				
			}
	}
