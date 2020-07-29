package server;

import java.io.*;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


@SuppressWarnings( "serial" )
public class Users extends JSONObject
	{
		private String							__PATHDBU = ".data/DB.json";
		private int 								__size = -1;
		private static Users 				__users = null;
		private JSONParser 					__parser = null;
		private static JSONObject 	__dbUser = null;
		private FileWriter 					__dbFile = null;
		
		
		/**
		 * 
		 */
		private Users( )
			{
				__parser = new JSONParser( );
				
				try
					{
						__dbUser = (JSONObject)__parser.parse( new FileReader( __PATHDBU ) );//parsing dbUser db from file
						__size = __dbUser.size( );
					} 
				catch( FileNotFoundException e ) //if file not exists, it's created
					{
						__dbUser = new JSONObject( );
					} 
				catch( IOException e )
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				catch( ParseException e )
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		
		
		/**
		 * 
		 * @return
		 */
		public synchronized static Users init( )
			{
				if( __users == null )
					{
						__users = new Users( );
					}
				return __users;
			}
		
		
		/**
		 * 
		 * @return
		 */
		public synchronized int getNusers( )
			{
				return __size;
			}
		
		
		
		@SuppressWarnings( "unchecked" )
		public synchronized ACK insertUser( User usr )
			{
				if( searchUser( usr.getID( ) ) == ACK.UserNotFound )
					{
						JSONObject u = new JSONObject( );
						u.put( "password", usr.getPassword( ) );
						u.put( "name", usr.getName( ) );
						u.put( "surname", usr.getSurname( ) );
						u.put( "TScore", usr.getTotScore( ) );
						u.put( "LatestScore", usr.getChalScore( ) );
						u.put( "STATUS", usr.getStatus( ) );
						
						__dbUser.put( usr.getID( ), u );
						
						return ACK.UserRegistered; 
					}
				
				return ACK.UserFound;	
			}
		
		
		
		public synchronized User getUser( String nickname )
			{
				JSONObject tmpU = null;
				User newU = null;
				
				if( ( tmpU = (JSONObject) __dbUser.get( nickname ) ) != null )
					{
						String password = (String) tmpU.get( "password" );
						String name = (String) tmpU.get( "name" );
						String sname = (String) tmpU.get( "surname" );
						
						if( name == "null" )
							{
								name = null;
							}
						if( sname == "null" )
							{
								sname = null;
							}
					
						newU = new User( nickname, password, name, sname );
						newU.setTScore( (int) tmpU.get( "TScore") );
						newU.setCScore( (int) tmpU.get( "LatestScore") );
					}
				
				return newU;
			}
		
		
		@SuppressWarnings( "unchecked" )
		public synchronized ACK updateUser( User usr )
			{
				if( searchUser( usr.getID( ) ) == ACK.UserFound )
					{
						JSONObject u = (JSONObject) __dbUser.get( usr.getID( ) );
						
						u.remove( "password" );
						u.remove( "name" );
						u.remove( "surname" );
						u.remove( "TScore" );
						u.remove( "LatestScore" );
						u.remove( "STATUS" );
						
						u.put( "password", usr.getPassword( ) );
						u.put( "name", usr.getName( ) );
						u.put( "surname", usr.getSurname( ) );
						u.put( "TScore", usr.getTotScore( ) );
						u.put( "LatestScore", usr.getChalScore( ) );
						u.put( "STATUS", usr.getStatus( ) );
						
						return ACK.OK;
					}
				
				return ACK.UserNotFound;
			}

		
		/**
		 * 
		 * @param nickname
		 * @return
		 */
		public synchronized ACK searchUser( String nickname )
			{
				if( __dbUser.get( nickname ) != null ) //checks if in the DB exists a jsonobject with key equals to nickname value
					{
						return ACK.UserFound;
					}
				
				return ACK.UserNotFound;
			}
		
		
		/**
		 * 
		 * @param nickname
		 * @return
		 */
		public synchronized ACK deleteUser( String nickname )
			{
				if( searchUser( nickname ) == ACK.UserFound ) //if user is into the DB then it will removed
					{
						__dbUser.remove( nickname );
						__size = __dbUser.size( );
						
						return ACK.UserDeleted;
					}
				
				return ACK.UserNotDeleted;
			}
		
		/**
		 * 
		 */
		protected synchronized void writeONfile( )
			{
				try
					{
						__dbFile = new FileWriter( __PATHDBU );
					}
				catch (IOException e) 
					{
						// TODO: handle exception
					}
				try
					{
						__dbFile.write( __dbUser.toJSONString( ) );
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