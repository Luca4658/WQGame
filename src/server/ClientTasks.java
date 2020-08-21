/************************************************
 *                                              *
 *                  CLIENTTASK                  *
 *                                              *
 ************************************************
 *
 */
package server;

import java.io.*;

import java.net.*;

import java.util.*;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;



/**
 * In questa enumerazione vengono implementati i messaggi che il server riceve
 * dal client
 *
 * @class   ClientMSG
 * @author  Luca Canessa (Mat. 516639)
 * @version 1.1
 * @since   1.0
 */
enum ClientMSG
	{
		LOGIN, ///< richiesta di login
		LOGOUT, ///< richiesta di logout
		ADDFRIEND, ///< richiesta di aggiunta di amico alla lista
		GETFRIENDS, ///< richiesta lista amicizie
		GETNFRIENDS, ///< richiesto numero di amici
		STARTCH, ///< richiesta di inizio di una sfida
		ACCEPTEDCH, ///< accettazione della sfida arrivata
		UPDATEINFO, ///< richiesta di aggiornamento dei dati
		GETPOINTS, ///< richiesta punti totalizzati
		GETRANK,  ///< richiesta della classifica
		RMUSER, ///< richiesta rimozione dell'User
		RMFRIEND; ///< richiesta rimozione dell'amico
	}


/**
 * In questa classe vengono implementati i metodi e le variabili necessari per
 * controllare le operazioni richieste dal client, identificate
 * dall'enumerazione ClientMSG. Per ogni operazione richiesta viene eseguito un
 * task e inviato il risultato della terminazione del task al client
 *
 * @class   ClientTasks
 * @author  Luca Canessa (Mat. 516639)
 * @version 1.6
 * @since   1.0
 */
public class ClientTasks implements Runnable
	{
		//            //
		//  VARIABLES //
		//            //
		private boolean           __end = false; ///< variabile usata per determinare la terminazione del ciclo principale 'while' nel metodo 'run'
		private User							__me	= null; ///< User che si collega
		private Users 						__udb = null; ///< databse degli User
		private Friendships 			__fdb = null; ///< database delle amicizie
		private Socket						__cSocket = null; ///< socket di comunicazione con il client
		private BufferedReader		__inBuff = null; ///< buffer su cui scrive il client per comunicare
		private DataOutputStream 	__outBuff = null; ///< buffer su cui scrive il Thread (l'oggetto ClientTask) in esecuzione per comunucare con il client


		//          //
		//  METHODS //
		//          //

		/**
		 * Costruttore del gestore dei task dell'User, si occupa di inizializzare
		 * le variabili
		 *
		 * @param udb     database degli utenti
		 * @param fdb     database delle amicizie
		 * @param socket  socket su cui comunicare con il client
		 */
		public ClientTasks( Users udb, Friendships fdb, Socket socket )
			{
				__udb = udb;
				__fdb = fdb;
				__cSocket = socket;
				try
					{
						__inBuff = new BufferedReader( new InputStreamReader( __cSocket.getInputStream( ) ) );
					} 
				catch( IOException e )
					{
						Main.logger( "Problem to create the input buffer" );
					}
				try
					{
						__outBuff = new DataOutputStream( __cSocket.getOutputStream( ) );
					} 
				catch( IOException e )
					{
						Main.logger( "Problem to create the output buffer" );
					}
			}

		/**
		 * Genera la classifica personale del User estrapolando gli amici e i loro
		 * punti dalla classifica generale del gioco e restituendola ordinata per
		 * il valore dei punti.
		 *
		 * @return il JSON con la classifica rispetto all'User
		 */
		private JSONObject personalRank( )
			{
				HashMap<String, Integer> ranking = __udb.getRanking( );
				HashMap<String, Integer> myranking = new HashMap<String, Integer>( );
				JSONArray friendlist = __fdb.getFriends( __me );
				
				for( int i = 0; i < friendlist.size( ); i++ )
					{
						String friend = (String) friendlist.get( i );
						
						myranking.put( friend, ranking.get( friend ) );
					}
				
				myranking.put( __me.getID( ), __me.getTotScore( ) );
				
				
				List<Entry<String,Integer>> list = new ArrayList<HashMap.Entry<String,Integer>>( myranking.entrySet( ) );

				//reordering of personal ranking
				Collections.sort( list, new Comparator<Map.Entry<String, Integer> >() 
					{ 
						public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2)
							{ 
								return ( o1.getValue( ) ).compareTo( o2.getValue( ) ); 
							}
					});
				
				JSONObject rank = new JSONObject( );
				JSONObject usrrk = new JSONObject( );
				
				for( Map.Entry<String, Integer> el : list )
					{
						usrrk.put( el.getKey( ), el.getValue( ) );
					}

				rank.put( "ranking", usrrk );

				return rank;				
			}

		/**
		 * Scrive un messaggio sul buffer in uscita da mandare al client come
		 * sequenza di Bytes (more DataOutputStream.writeBytes())
		 *
		 * @param msg messaggio da inviare al client
		 */
		private void send( String msg )
			{
				try
					{
						__outBuff.writeBytes( msg + "\n"); //importante terminazione linea
						__outBuff.flush( );
					}
				catch( IOException e )
					{
						Main.logger( "problem to send message" );
					}
			}

		/**
		 * Aspetta di ricevere dati dal client sul buffer di ingresso come tipo
		 * String (more BufferedReader.readLine())
		 *
		 * @return stringa di dati ricevuti dal Client
		 */
		private String recv( )
			{
				try
					{
						return __inBuff.readLine();
					}
				catch( IOException e )
					{
						Main.logger( "problem to receive message" );
						__end = true;
					}

				return null;
			}

		/**
		 * Controlla lo stato di attività dell'amico ritornando un messaggio di ACK
		 *
		 * @param Friend stringa del nickname dell'amico da controllare
		 * @return  ONLINE se l'User ha effettuato il login al gioco
		 *          OFFLINE se l'User non è presente al gioco
		 *          INCHALLENGE se l'User sta già effettuando una sfida
		 *          FriendNotFound se l'User non è tra le amicizie
		 */
		private ACK checkFStatus( String Friend )
			{
				User frd = __udb.getUser( Friend );
				if( __fdb.searchFriend( __me, frd ) != ACK.FriendNotFound )
					{
						return frd.getStatus( );
					}
				return ACK.FriendNotFound;
			}

		/**
		 * Invia una richiesta di sfida di gioco al friend con nickname
		 * 'friendName' utilizzando il protocollo UDP e indirizzando il pacchetto
		 * verso la porta di destinazione identificata dal codice hash del nickname
		 * dell'amico. (Nickname univoco quindi bassa possibilità di collisioni).
		 * Attende la risposta del friend fino allo scadere del Timeout, poi
		 * considera la sfida rifiutata. Tempo di Timeout preso dal file di
		 * configurazione. Restituisce un valore di ACK
		 *
		 * @param friendName  nome dell'amico che si vuole sfidare
		 * @return  ACK.Accepted se l'amico accetta la sfida
		 *          ACK.Rejected se l'amico rifiuta la sfida o se scade il timeout
		 *          ACK.ERROR se si presentano errori sulla socket UDP
		 */
		private ACK sendRequest( String friendName )
			{
				DatagramSocket udpsockclient = null;
				DatagramPacket udppk = null;
				byte[] buffers = null;
				byte[] bufferr = null;
				InetAddress add = null;
				String isAcc = null;
				try
					{
						udpsockclient = new DatagramSocket(  );

						// send //
						buffers = __me.getID( ).getBytes( );
						add = InetAddress.getByName( "localhost" );
						udppk = new DatagramPacket( buffers, buffers.length, add, Main.getUniPort( friendName ) );
						udpsockclient.send( udppk );

						// receive //
						bufferr = new byte[1024];
						udppk = new DatagramPacket( bufferr, bufferr.length );
						udpsockclient.setSoTimeout( (int) Main.parser.getTimeoutReply( ) );
						udpsockclient.receive( udppk );
						isAcc = new String( udppk.getData(), 0, udppk.getLength( ) );
					}
				catch( SocketException e )
					{
						Main.logger( e.toString( ) );
						return ACK.ERROR;
					}
				catch( SocketTimeoutException e )
					{
						Main.logger( "Friend gave the timeout" );
						isAcc = ACK.Rejected.name( );
					}
				catch( IOException e )
					{
						Main.logger( e.toString( ) );
						return ACK.ERROR;
					}

				if( isAcc.equals( ACK.Accepted.name() ) )
					{
						return ACK.Accepted;
					}

				return ACK.Rejected;
			}

		/**
		 * Metodo che gestisce l'inizio e la fine di ogni sfida aggiornando lo
		 * stato dell'User in INCHALLENGE e preparando lista di parole da inviare
		 * prima dell'avvio della sfida e dopo aver avviato la sfida attende che
		 * questa sia finita per aggiornare i punti guadagnati all'User e decretare
		 * il vincitore della sfida. Terminata la sfida elimina i file su disco che
		 * sono serviti per la condivisione delle parole.
		 *
		 * @param nickname  Stringa da usare per decodificare la sequenza di parole
		 *                  da usare
		 * @param rival Stringa per determinare il nome dell'avversario
		 */
		private void Game( String nickname, String rival )
			{
				ArrayList<ArrayList<String>> words = Main.getWords( nickname );

				Challenge challenge = null;
				Thread cThread = null;

				__me.setInChallenge( );
				__udb.updateUser( __me );


				challenge = new Challenge(  __inBuff, __outBuff, words, __me, __udb, Thread.currentThread( ) );
				cThread = new Thread( challenge );
				cThread.start( );


				try
					{
						Thread.sleep( Main.parser.getTimeoutGame() * 3 );
						Main.logger( "Challenge ended for " + __me.getID() );

					}
				catch( InterruptedException e )
					{
						Main.logger( "Challenge ended for " + __me.getID() );
					}

				__me = __udb.getUser( __me.getID( ) );

				String winner = Main.getWinner( __me, __udb.getUser( rival ), __udb );
				send( winner );

				if( winner.equals( __me.getID( ) ) )
					{
						__me.setCScore( __me.getChalScore( ) + Main.parser.getExtraPoints( ) );
						__me.setTScore( __me.getChalScore( ) );
					}
				else
					{
						__me.setTScore( __me.getChalScore( ) );
					}

				File f = new File( "/tmp/it" + nickname.hashCode() );
				f.delete();
				f = new File( "/tmp/en" + nickname.hashCode() );
				f.delete();
				__udb.updateUser( __me );
			}

		/**
		 * Metodo, messo in esecuzione da un thread, utile alla gestione del client
		 * collegatosi. Permette di ricevere come primo pacchetto l'operazione da
		 * eseguire, che è determinata attraverso la classe Enum ClientMSG. Per
		 * ogni operazione richiesta esegue compiti differenti, mandando sempre al
		 * client come risposta l'esito dell'operazione.
		 * Le operazioni consentite dal client User sono possibili se e solo se
		 * è stata effettuata come prima operazione quella di LOGIN, in caso
		 * contrario non è possibile procedere.
		 * Le operazioni consentite al User sono:
		 * - LOGIN: si salva aggiornano le variabili dell'User in base a chi ha
		 *          chiesto l'operazione
		 * - LOGOUT: si resettano le variabili dell'User
		 * - ADDFRIEND: si aggiunge l'User richiesto nella lista delle amicizie se
		 *              e solo se questo esiste
		 * - GETFRIENDS: si restituisce la lista delle amicizie dell'User
		 * - GETNFRIENDS: si restituisce il numero di amici dell'User
		 * - STARTCH: si invia una richiesta di sfida ad un altro User se possibile.
		 *            Se questo accetta si avvia
		 * - ACCEPTEDCH: si avvia la sfida che è stata appena accettata
		 * - UPDATEINFO: si aggiornano le informazioni dell'User sul DB
		 * - GETPOINTS: si invia al client il numero di punti accumulati durante
		 *              tutto il gioco
		 * - GETRANK: si invia al client la ranking personale del gioco in cui vi
		 *            sono solo gli amici del User e l'User stesso
		 */
		@Override
		public void run( )
			{
				Main.logger( "Client " + __cSocket.toString( ) + " connected" );
				__end = false;
				send( String.valueOf( Main.parser.getTimeoutGame() ) );
				while( !__end || !Thread.currentThread( ).isInterrupted( ) )
					{
						try
							{
								String opRec = recv( );

								if( opRec == null )
									{
										__end = true;
										break;
									}
								ClientMSG op = null;
								try
									{
										 op = ClientMSG.valueOf( opRec );
									}
								catch( Exception e )
									{
										Main.logger( "Unknown Operation requested" );
										send( ACK.OperationUnknown.name( ) );
										continue;
									}

								switch( op )
									{
										case LOGIN:
											{
												ACK ret = ACK.ERROR;
												String nickname = recv( );
												String psw = recv( );

												try
													{
														__me = __udb.getUser( nickname );

														ret = ACK.PasswordUnmatch;

														if( __me.pswIsEqual( psw ) )
															{
																if( __me.getStatus( ) != ACK.OFFLINE )
																	{
																		ret = ACK.UserAlreadyLoggedIn;
																	}
																else
																	{
																		__me.setOnline( );
																		__udb.updateUser( __me );
																		ret = ACK.LoggedIn;
																		Main.logger( __me.getID( ) + " logged-in");
																	}
															}
													}
												catch( Exception e )
													{
														ret = ACK.UserNotFound;
													}

												send( ret.name() );
											}
										break;
										case LOGOUT:
											{
												if( __me == null )
													{
														send( ACK.NotLogged.name( ) );
														continue;
													}
												ACK ret;
												__me.setOffline( );
												__udb.updateUser( __me );
												ret = ACK.LoggedOut;
												Main.logger( __me.getID( ) + " logged-out");
												__me = null;

												send( ret.name( ) );

											}
										break;
										case ADDFRIEND:
											{
												if( __me == null )
													{
														send( ACK.NotLogged.name( ) );
														continue;
													}
												String name = __inBuff.readLine( );
												User frd  = __udb.getUser( name );
												ACK ret = __fdb.addFriend( __me, frd );

												send( ret.name() );
											}
										break;
										case GETFRIENDS:
											{
												if( __me == null )
													{
														send( ACK.NotLogged.name( ) );
														continue;
													}
												String list = __fdb.getFriends( __me ).toJSONString( );

												send( list);
											}
										break;
										case GETNFRIENDS:
											{
												if( __me == null )
													{
														send( ACK.NotLogged.name( ) );
														continue;
													}
												long n = __fdb.getNFriends( __me );

												send( Long.toString( n )  );
											}
										break;
										case STARTCH:
											{
												if( __me == null )
													{
														send( ACK.NotLogged.name( ) );
														continue;
													}

												String frd = recv( );
												Main.logger( __me.getID( ) + " asked to start a new challenge with " + frd );
												ACK ret = checkFStatus( frd );
												switch( ret )
													{
														case FriendNotFound:
														case OFFLINE:
														case INCHALLENGE:
															{
																Main.logger( __me.getID( ) + " cannot start the challenge with " + frd );
																send( ret.name( ) );
															}
														break;
														case ONLINE:
															{
																ACK retReq = sendRequest( frd );
																if( retReq == ACK.Rejected )
																	{
																		send(  retReq.name( ) );
																		Main.logger( frd + " has rejected the challenge with " + __me.getID( ) );
																		continue;
																	}
																else if( retReq == ACK.Accepted )//accepted
																	{
																		send( retReq.name( ) );
																		try
																			{
																				Main.setWords( __me.getID( ) );
																			}
																		catch( ParseException e )
																			{
																				Main.logger( "Problem to parse online JSON" );
																			}

																		Main.logger( __me.getID( ) + " has started the challenge with " + frd );
																		Thread.sleep( 1000 );
																		Game( __me.getID( ), frd );
																	}
																else
																	{
																		send(  retReq.name( ) );
																		continue;
																	}
															}
														break;
														default:
															{
																send( ACK.ERROR.name( ) );
																Main.logger( "Unspecified error" );
															}
														break;
													}

											}
										break;
										case ACCEPTEDCH:
											{
												if( __me == null )
													{
														send( ACK.NotLogged.name( ) );
														continue;
													}
												String rival = recv( );
												Main.logger( __me.getID( ) + " has accepted the challenge with " + rival );
												Game( rival, rival );
											}
										break;
										case UPDATEINFO:
											{
												if( __me == null )
													{
														send( ACK.NotLogged.name( ) );
														continue;
													}
												String name = recv( );
												String sname = recv( );
												String psw = recv( );

												if( !name.equals( "" ) )
													{
														__me.resetName( name );
													}
												if( !sname.equals( "" ) )
													{
														__me.resetSurname( sname );
													}
												if( !psw.equals( "" ) )
													{
														__me.resetPassword( psw );
													}

												ACK ret = __udb.updateUser( __me );
												send( ret.name( ) );
											}
										break;
										case GETPOINTS:
											{
												if( __me == null )
													{
														send( ACK.NotLogged.name( ) );
														continue;
													}
												int score = __me.getTotScore( );
												send( Integer.toString( score ) );
											}
										break;
										case GETRANK:
											{
												if( __me == null )
													{
														send( ACK.NotLogged.name( ) );
														continue;
													}
												HashMap<String, Integer> myrank = personalRank( );
												send( myrank.toString( ) );
											}
										break;
										case RMFRIEND:
											{
												if( __me == null )
													{
														send( ACK.NotLogged.name( ) );
														continue;
													}
												String friend = recv( );
												Main.logger( __me.getID( ) + " has required to remove " + friend + " from own friendships" );
												ACK ret = __fdb.removeFriend( __me, __udb.getUser( friend ) );
												send( ret.name( ) );
											}
										break;
										case RMUSER:
											{
												if( __me == null )
													{
														send( ACK.NotLogged.name( ) );
														continue;
													}
												Main.logger( __me.getID( ) + " has required the deletion" );

												ACK ret = __udb.deleteUser( __me.getID( ), __fdb );
												send( ret.name( ) );
												__me = null;
											}
										break;
										default:
											{
												ACK ret = ACK.OperationUnknown;
												String ackmsg = ret.name( );
												Main.logger( "Client " + __cSocket.toString( ) + " disconnected" );
												__outBuff.writeChars( ackmsg );
												__inBuff.close( );
												__outBuff.close( );
												__cSocket.close( );
												__end = true;
											}
										break;
									}
							} 
						catch( IOException | InterruptedException e )
							{
								try
									{
										Main.logger( "Client " + __cSocket.toString( ) + " disconnected" );
										__inBuff.close( );
										__outBuff.close( );
										__cSocket.close( );
										__end = true;
									} 
								catch( IOException e1 )
									{
										e1.printStackTrace();
									}
							}
					}
				try
					{
						if( __me != null )
							{
								Main.logger( "Client " + __me.getID() + " disconnected" );
								__me.setOffline();
								__udb.updateUser( __me );
							}
						__inBuff.close( );
						__outBuff.close( );
						__cSocket.close( );
						__end = true;
					}
				catch( IOException e1 )
					{
						e1.printStackTrace();
					}
			}
	}
