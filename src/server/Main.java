package server;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
		
	}




public class Main
	{
		public static void main( String[] args )
			{
				
				ConfParser parser = new ConfParser( args[0] );
				
				System.out.println( parser.getUsersPath( ) );
				Users DB = Users.init( parser.getUsersPath( ) );
				Friendships FDB = Friendships.init( parser.getFriendPath( ) );
	
				User a = new User( "Luca1", "Password1" );
				User b = new User( "Luca2", "Password2", "LucaC", null );
				User c = new User( "Luca3", "Password3" );
				
				DB.insertUser( a, FDB );
				DB.insertUser( b, FDB );
				DB.insertUser( c, FDB );
				
				b.resetPassword( "Passwd2" );
				a.resetName( "LucaD" );
				
				DB.updateUser( a );
				DB.updateUser( b );
				
				FDB.addFriend( a, b );
				FDB.addFriend( a, c );
				
				DB.writeONfile( );
				FDB.writeONfile( );
				
				User l = DB.getUser( "Luca1" );
				User k = DB.getUser( "Luca2" );
				
				System.out.println( FDB.getNFriends( a ) + " " + FDB.getNFriends( b ) + " " + FDB.getNFriends( c ) );
				
				
				for( String f:FDB.getFriends( a ) )
					{
						System.out.print( f + "\t" );
					}
				
				FDB.removeFriend( a, c );
				
				FDB.writeONfile( );
			
				
				System.out.println( "\n" + l.getName( ) + "\t" + k.getPassword( ) );
			}
	}
