/************************************************
 *                                              *
 *                  FRIENDSHIPS                 *
 *                                              *
 ************************************************
 *
 */
package server;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



/**
 * In questa classe viene gestito il database delle amicizie tra gli utenti.
 * Questo database viene recuperato, se esistente, da un file JSON salvato
 * sulla macchina, di default nella cartella '.data'.
 * Tale file JSON è composto da una serie di oggetti che rappresentano l'User
 * registrato a gioco. Ogni oggetto è composto da un JSONArray contenente
 * tutte le amicizie di quell'User.
 *
 * @class   Friendships
 * @author  Luca Canessa (Mat. 516639)
 * @version 1.3
 * @since   1.0
 */
public class Friendships
	{
		//						//
		//	VARIABLES	//
		//						//
		private static String				__PATHDBF = null; ///< path del db delle amicizie
		private static Friendships	__friends = null; ///< singleton
		private static JSONObject		__dbFriend = null; ///< struttra dati del db in memoria
		
		
		
		//					//
		//	METHODS	//
		//					//

		/**
		 * Costruttore del database delle amicizie tra utenti registrati al gioco.
		 * Il compito è di aprire e copiare il db in una struttura dati JSON se
		 * esistente, altrimenti di crearlo. Costruttore privato perché oggetto
		 * implementato come singoletto.
		 */
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

		/**
		 * Metodo che deve essere chiamato per istanziare il db delle amicizie.
		 * Metodo necessario per creare un singleton e avere la possibilità di
		 * operare solo su un oggetto senza avere problemi di ridondanze di dati
		 * e/o inconsistenze
		 *
		 * @return  l'oggetto contenente il db
		 */
		public static Friendships init( )
			{
				if( __friends == null )
					{
						__PATHDBF = Main.parser.getFriendPath( );
						__friends = new Friendships( );
					}
				
				return __friends;
			}

		/**
		 * Cerca all'interno del db delle amicizie se due User sono amici tra loro
		 * restituendo un valore di ACK al termine
		 * @param a User
		 * @param b User
		 * @return  AlreadyFriends se i due utenti sono già amici
		 *          FriendNotFound se i due utenti ancora non sono amici
		 */
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

		/**
		 * Aggiunge l'User a alla lista delle amicizie dell'User b e viceversa se
		 * già non presenti. Ritorna un valore di ACK
		 *
		 * @param a User
 		 * @param b User
		 * @return  FriendAdded se aggiunti alle liste
		 *          AlreadyFriends se non aggiunti
		 */
		@SuppressWarnings( "unchecked" )
		public synchronized ACK addFriend( User a, User b )
			{
				if( !a.getID( ).equals( b.getID( ) ) && searchFriend( a, b ) == ACK.FriendNotFound )
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

		/**
		 * Metodo chiamato alla creazione di un User per poterlo inserire anche
		 * nel database delle amicizie. Restituisce un valore di ACK.
		 *
		 * @param a User da aggiungere al db
		 * @return  UserAdded se l'utente è stato inserito
		 *          AlreadyExisting se l'utente era già presente
		 */
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

		/**
		 * Restituisce il numero di amici di un User
		 *
		 * @param a User di cui sapere il numero di amici
		 * @return  un numero > 0 se amici presenti, 0 se nessun amico presente
		 */
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

		/**
		 * Restituisce un JSONArray contenente il tutti gli amici dell'User
		 *
		 * @param a User che richiede la lista delle amicizie
		 * @return lista degli amici (JSONArray)
		 */
		@SuppressWarnings( "unchecked" )
		public synchronized JSONArray getFriends( User a )
			{
				JSONObject userA = (JSONObject) __dbFriend.get( a.getID( ) );
				JSONArray friendsA = (JSONArray) userA.get( "friends" );
				Iterator<String> friends = friendsA.iterator( );
				
				JSONArray frdtoret = new JSONArray( );
				while( friends.hasNext( ) )
					{
						frdtoret.add( friends.next( ) );
					}
				
				return frdtoret;
			}

		/**
		 * Rimuove gli User dalla reciproca lista delli amici. Restituisce un
		 * valore di ACK
		 *
		 * @param a User
		 * @param b User
		 * @return  FriendRemoved se gli User sono stati rimossi
		 *          AlreadyFriend se non sono stati rimossi
		 */
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

		/**
		 * Rimuove l'User dalla lista di tutti i suoi amici e poi lo cancella dal
		 * database
		 *
		 * @param a User da eliminare
		 */
		public synchronized void removeUser( User a )
			{
				JSONArray frds = getFriends( a );
				while( frds.size( ) > 0 )
					{
						JSONObject userB = (JSONObject) __dbFriend.get( frds.get( 0 ) );
						JSONArray friendsB = (JSONArray) userB.get( "friends" );

						friendsB.remove( a.getID( ) );
						frds.remove( frds.get( 0 ) );
					}

				__dbFriend.remove( a.getID( ) );
			}

		/**
		 * Metodo utilizzato per scrivere il db delle amicizie su file
		 */
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
