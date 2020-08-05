/************************************************
 * 																							*
 * 										USERS											*
 * 																							*
 ************************************************
 *
 */
package server;

import java.io.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



/**
 * In questo file vengono implementati i metodi e
 * le variabili per modellare il database degli utenti
 * usando come modello dell'utente la classe 'user'
 *
 * @class   Users
 * @author	Luca Canessa (Mat. 516639)
 * @version	1.5
 * @since		1.0
 */
@SuppressWarnings( "serial" )
public class Users implements Serializable
	{
		//						//
		//	VARIABLES	//
		//						//
		private static String				__PATHDBU = null; ///< path al file del db utent
		private int 								__size = -1; ///< numero di utenti iscritti
		private static Users 				__users = null; ///< singleton
		private JSONParser 					__parser = null; ///< file parser
		private static JSONObject 	__dbUser = null; ///< il db reale
		private FileWriter 					__dbFile = null; ///< scrittore del db sul file '__PATHDBU'
		
		
		//					//
		//	METHODS	//
		//					//
		
		/**
		 * Costruttore del databse degli utenti. Implementato come singoletto per
		 * evitare problemi di copie inconsistenti e/o ridondanze.
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
						System.err.println( "sono qui" );

						e.printStackTrace();
					}
			}

		/**
		 * Inizializzatore del database. Si interfaccia direttamente
		 * con il costruttore della classe
		 * 
		 * @return oggetto che rappresenta il database
		 */
		public static Users init( )
			{
				if( __users == null )
					{
						__PATHDBU = Main.parser.getUsersPath( );
						__users = new Users( );
					}
				
				return __users;
			}
		
		/**
		 * Restituisce il numero di oggetti User all'interno del database
		 *
		 * @return il numero degli utenti iscritti
		 */
		public synchronized int getNusers( )
			{
				return __size;
			}

		/**
		 * Inserisce l'utente all'interno del database estrapolando i dati
		 * dall'oggetto User passatogli restituendo un valore ACK
		 * 
		 * @param usr utente da inserire
		 * @return  UserRegistered se l'User è stato iscritto
		 *          UserFound se l'User è già presente
		 */
		@SuppressWarnings( "unchecked" )
		public synchronized ACK insertUser( User usr, Friendships f )
			{
				if( searchUser( usr.getID( ) ) == ACK.UserNotFound )
					{
						JSONObject u = new JSONObject( );
						u.put( "password", usr.getPassword( ) );
						u.put( "name", usr.getName( ) );
						u.put( "surname", usr.getSurname( ) );
						u.put( "TScore", ((Integer)usr.getTotScore( )).toString( ) );
						u.put( "LatestScore", ((Integer)usr.getChalScore( )).toString() );
						u.put( "STATUS", usr.getStatus( ).toString( ) );
						
						__dbUser.put( usr.getID( ), u );
						
						__size++;
						
						if( f.newUser( usr ) != ACK.UserAdded )
							{
								System.err.println( "proviamo qui" );
							}
						
						return ACK.UserRegistered; 
					}
				
				return ACK.UserFound;	
			}
		
		/**
		 * Restituisce l'oggetto User se presente all'interno del db
		 *
		 * @param nickname stringa del nickname dell'User da estrapolare
		 * @return l'oggetto User se presente, null altrimenti
		 */
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
						newU.setTScore( Integer.valueOf( (String) tmpU.get( "TScore") ) );
						newU.setCScore(  Integer.valueOf( (String) tmpU.get( "LatestScore") ) );
					}
				
				return newU;
			}

		/**
		 * Aggiorana tutti i valori dell'User nel database e ritorna l'esito
		 * dell'operazione come valore ACK
		 *
		 * @param usr ogetto User di cui si vogliono aggiornare i valori
		 * @return  OK se l'operazione è riuscita
		 *          UserNotFound se l'operazione non è terminata
		 */
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
						u.put( "TScore", ((Integer)usr.getTotScore( )).toString( ) );
						u.put( "LatestScore", ((Integer)usr.getChalScore( )).toString() );
						u.put( "STATUS", usr.getStatus( ).toString( ) );
						
						return ACK.OK;
					}
				
				return ACK.UserNotFound;
			}

		/**
		 * Cerca all'interno del db se esiste l'User con il nickname 'nickname'
		 *
		 * @param nickname stringa del nickname dell'User da cercare
		 * @return  UserFound se l'User è all'interno del db
		 *          UserNotFound se l'User non è stato trovato
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
		 * Elimina un User dal db (se possibile)
		 *
		 * @param nickname stringa del nickname dell'User da rimuovere
		 * @return  UserDeleted se l'operazione è riuscita
		 *          UserNotDeleted se l'operazione non ha terminato con successo
		 */
		public synchronized ACK deleteUser( String nickname, Friendships f )
			{
				if( searchUser( nickname ) == ACK.UserFound ) //if user is into the DB then it will removed
					{
						f.removeUser( __users.getUser( nickname ) );
						__dbUser.remove( nickname );
						__size = __dbUser.size( );
						
						return ACK.UserDeleted;
					}
				
				return ACK.UserNotDeleted;
			}

		/**
		 * Restituisce la ranking list del gioco ordinata per valore di punti in
		 * ordine decrescente di tutti gli utenti
		 *
		 * @return  una HashMap con i nickname e i punti degli utenti
		 *          una HashMap vuota nel caso non vi siano utenti
		 */
		@SuppressWarnings( "unchecked" )
		protected HashMap<String, Integer> getRanking( )
			{
				HashMap<String, Integer> tempR = new HashMap<String, Integer>( );
				Iterator<String> user = __dbUser.keySet( ).iterator( );
				
				while( user.hasNext( ) )
					{
						String sUsr = user.next( );
						JSONObject field = (JSONObject) __dbUser.get( sUsr );

						String p =  (String) field.get( "TScore" );
						int score = Integer.valueOf( p );
						
						tempR.put( sUsr, score );
					}
				
				
				List<Entry<String,Integer>> list = new ArrayList<HashMap.Entry<String,Integer>>( tempR.entrySet( ) );
				
				Collections.sort( list, new Comparator<Map.Entry<String, Integer> >() 
							{ 
								public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2)
									{ 
										return (o1.getValue()).compareTo(o2.getValue()); 
									}
							});
				
				HashMap<String, Integer> rank = new LinkedHashMap<String, Integer>( );
				
				for( Map.Entry<String, Integer> el : list )
					{
						rank.put( el.getKey( ), el.getValue( ) );
					}
				
				return rank;
			}

		/**
		 * Scrive sul file il db degli utenti
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