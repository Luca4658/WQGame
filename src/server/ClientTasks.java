package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.*;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;



enum ClientMSG
	{
		LOGIN,
		LOGOUT,
		ADDFRIEND,
		GETFRIENDS,
		GETNFRIENDS,
		STARTCH,
		UPDATEINFO,
		GETPOINTS,
		GETRANK;
	}



public class ClientTasks implements Runnable
	{
		private User							__me	= null;
		private Users 						__udb = null;
		private Friendships 			__fdb = null;
		private Socket						__cSocket = null;
		private BufferedReader		__inBuff = null;
		private DataOutputStream 	__outBuff = null;
		
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
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				try
					{
						__outBuff = new DataOutputStream( __cSocket.getOutputStream( ) );
					} 
				catch( IOException e )
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}

		
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


		private void send( String msg )
			{
				try
					{
						__outBuff.writeBytes( msg + "\n");
					}
				catch( IOException e )
					{
						System.err.println( "problem to send message" );
					}
			}

		private String recv( )
			{
				try
					{
						return __inBuff.readLine();
					}
				catch( IOException e )
					{
						System.err.println( "problem to receive message" );
					}

				return null;
			}


		private ACK checkFStatus( String Friend )
			{
				User frd = __udb.getUser( Friend );
				if( __fdb.searchFriend( __me, frd ) != ACK.FriendNotFound )
					{
						return frd.getStatus( );
					}
				return ACK.FriendNotFound;
			}


		private ACK sendRequest( )
			{
				return ACK.OK;
			}


		@Override
		public void run( )
			{
				boolean end = false;
				while( !end )
					{
						try
							{
								String opRec = recv( );

								if( opRec == null )
									{
										end = true;
										continue;
									}

								ClientMSG op = ClientMSG.valueOf( opRec );;

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
												ACK ret;
												__me.setOffline( );
												__udb.updateUser( __me );
												ret = ACK.LoggedOut;

												send( ret.name( ) );

											}
										break;
										case ADDFRIEND:
											{
												String name = __inBuff.readLine( );
												User frd  = __udb.getUser( name );
												ACK ret = __fdb.addFriend( __me, frd );

												send( ret.name() );
											}
										break;
										case GETFRIENDS:
											{
												String list = __fdb.getFriends( __me ).toJSONString( );

												send( list);
											}
										break;
										case GETNFRIENDS:
											{
												long n = __fdb.getNFriends( __me );

												send( Long.toString( n )  );
											}
										break;
										case STARTCH:
											{
												String frd = recv( );
												ACK ret = checkFStatus( frd );
												switch( ret )
													{
														case FriendNotFound:
														case OFFLINE:
														case INCHALLENGE:
															{
																send( ret.name( ) );
															}
														continue;
														case ONLINE:
															{
																ACK retReq = sendRequest( );
																if( retReq == ACK.Rejected )
																	{
																		send(  retReq.name( ) );
																		continue;
																	}
																else
																	{
																		User u = __udb.getUser( "pippo" );
																	}
															}
														break;
														default:
															{
																send( ACK.ERROR.name( ) );
																System.err.println( "Unspecified error" );
															}
														break;
													}

											}
										break;
										case UPDATEINFO:
											{
												String name = recv( );
												String sname = recv( );
												String psw = recv( );

												__me.resetName( name );
												__me.resetSurname( sname );
												__me.resetPassword( psw );
												
												ACK ret = __udb.updateUser( __me );
												send( ret.name( ) );
											}
										break;
										case GETPOINTS:
											{
												int score = __me.getTotScore( );
												send( Integer.toString( score ) );
											}
										break;
										case GETRANK:
											{
												HashMap<String, Integer> myrank = personalRank( );
												send( myrank.toString( ) );
											}
										break;
										default:
											{
												ACK ret = ACK.OperationUnknown;
												String ackmsg = ret.name( );
												__outBuff.writeChars( ackmsg );
												__inBuff.close( );
												__outBuff.close( );
												__cSocket.close( );
												end = true;
											}
										break;
									}
							} 
						catch( IOException e )
							{
								try
									{
										__inBuff.close( );
										__outBuff.close( );
										__cSocket.close( );
										end = true;
									} 
								catch( IOException e1 )
									{
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
							}
					}
				try
					{
						__inBuff.close( );
						__outBuff.close( );
						__cSocket.close( );
						end = true;
					}
				catch( IOException e1 )
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			}
	}
