package server;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Friendships
	{
		//						//
		//	VARIABLES	//
		//						//
		private static String				__PATHDBF = null; //path to friendships db
		private static Friendships	__friends = null; //singleton
		private static JSONObject		__dbFriend = null; //real db
		
		
		
		//					//
		//	METHODS	//
		//					//
		
		private Friendships( )
			{
				JSONParser parser = new JSONParser( );
				
				try
					{
						__dbFriend = (JSONObject) parser.parse( new FileReader( __PATHDBF ) );
					}
				catch( FileNotFoundException e )
					{
						__dbFriend = new JSONObject( );
					}
				catch( IOException e ) 
					{
					// TODO: handle exception
					}
				catch( ParseException e )
					{
						//TODO
					}
			}
		
		
		public static Friendships init( String dbPath )
			{
				if( __friends == null )
					{
						__PATHDBF = dbPath;
						__friends = new Friendships( );
					}
				
				return __friends;
			}
		
		
		@SuppressWarnings( "unchecked" )
		public synchronized ACK searchFriend( User a, User b )
			{
				JSONObject userA = (JSONObject) __dbFriend.get( a.getID( ) );
				JSONArray friendsA = (JSONArray) userA.get( "friends" );
				Iterator<String> friends = friendsA.iterator( );
				
				while( friends.hasNext( ) )
					{
						if( friends.next( ).equals( b.getID( ) ) )
							{
								return ACK.AlreadyFriends; 
							}
					}
				
				return ACK.FriendNotFound;
			}
		
		
		@SuppressWarnings( "unchecked" )
		public synchronized ACK addFriend( User a, User b )
			{
				if( searchFriend( a, b ) == ACK.FriendNotFound )
					{
						JSONObject userA = (JSONObject) __dbFriend.get( a.getID( ) );
						JSONArray friendsA = (JSONArray) userA.get( "friends" );
						
						JSONObject userB = (JSONObject) __dbFriend.get( b.getID( ) );
						JSONArray friendsB = (JSONArray) userB.get( "friends" );
						
						friendsA.add( b.getID( ) );
						friendsB.add( a.getID( ) );

						
						return ACK.FriendAdded;
					}
				
				return ACK.AlreadyFriends;
			}
		
		
		@SuppressWarnings( "unchecked" )
		public synchronized ACK newUser( User a )
			{
				if( __dbFriend.get( a.getID( ) ) == null )
					{
						JSONObject userA = new JSONObject( );
						JSONArray friendsA = new JSONArray( );
						
						userA.put( "friends", friendsA );
						
						__dbFriend.put( a.getID( ), userA );
						
						return ACK.UserAdded;
					}
				
				return ACK.AlreadyExisting;
			}
		
		
		@SuppressWarnings( "unchecked" )
		public synchronized long getNFriends( User a )
			{
				int nF = 0;
				JSONObject userA = (JSONObject) __dbFriend.get( a.getID( ) );
				JSONArray friendsA = (JSONArray) userA.get( "friends" );
				Iterator<String> friends = friendsA.iterator( );
				
				while( friends.hasNext( ) )
					{
						nF++;
						friends.next( );
					}
				
				return nF;
			}
		
		
		@SuppressWarnings( "unchecked" )
		public synchronized ArrayList<String> getFriends( User a )
			{
				ArrayList<String> Afriends = new ArrayList<String>( );
				JSONObject userA = (JSONObject) __dbFriend.get( a.getID( ) );
				JSONArray friendsA = (JSONArray) userA.get( "friends" );
				Iterator<String> friends = friendsA.iterator( );
				
				while( friends.hasNext( ) )
					{
						Afriends.add( friends.next( ) );
					}
				
				return Afriends;
			}
		
		
		public synchronized ACK removeFriend( User a, User b )
			{
				if( searchFriend( a, b ) == ACK.AlreadyFriends )
					{
						JSONObject userA = (JSONObject) __dbFriend.get( a.getID( ) );
						JSONArray friendsA = (JSONArray) userA.get( "friends" );
						
						JSONObject userB = (JSONObject) __dbFriend.get( b.getID( ) );
						JSONArray friendsB = (JSONArray) userB.get( "friends" );
						
						friendsA.remove( b.getID( ) );
						friendsB.remove( a.getID( ) );
						
						return ACK.FriendRemoved;
					}
				
				return ACK.AlreadyFriends;
			}
		
		
		protected synchronized void writeONfile( )
			{
				FileWriter __dbFile = null;
				try
					{
						__dbFile = new FileWriter( __PATHDBF );
					}
				catch (IOException e) 
					{
						// TODO: handle exception
					}
				try
					{
						__dbFile.write( __dbFriend.toJSONString( ) );
					} 
				catch( Exception e )
					{
						// TODO: handle exception
					}
				try
					{
						__dbFile.flush( );
						__dbFile.close( );
					}
				catch (Exception e) 
					{
					// TODO: handle exception
				}
			}
	}
