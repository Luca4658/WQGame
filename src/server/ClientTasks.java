package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;


enum ClientMSG
	{
		LOGIN,
		LOGOUT,
		ADDFRIEND,
		GETFRIENDS,
		GETNFRIENDS,
		SENDCH,
		CHSTARTED,
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
		
		@Override
		public void run( )
			{
				boolean end = false;
				while( !end )
					{
						try
							{
								String opRec = __inBuff.readLine( );
								ClientMSG op = ClientMSG.valueOf( opRec );
								
								switch( op )
									{
										case LOGIN:
											{
												ACK ret;
												String nickname = __inBuff.readLine( );
												String psw = __inBuff.readLine( );
												try
													{
														__me = __udb.getUser( nickname );
														if( __me.pswIsEqual( psw ) )
															{
																if( __me.getStatus( ) != UStatus.OFFLINE )
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
														ret = ACK.PasswordUnmatch;
													} 
												catch( Exception e )
													{
														ret = ACK.UserNotFound;
													}
												
												String ackmsg = ret.name( );
												__outBuff.writeChars( ackmsg );
											}
										break;
										case LOGOUT:
											{
												ACK ret;
												__me.setOffline( );
												__udb.updateUser( __me );
												ret = ACK.LoggedOut;
												String ackmsg = ret.name( );
												__outBuff.writeChars( ackmsg );
											}
										break;
										case ADDFRIEND:
											{
												String name = __inBuff.readLine( );
												User frd  = __udb.getUser( name );
												ACK result = __fdb.addFriend( __me, frd );
												String ackmsg = result.name( );
												
												__outBuff.writeChars( ackmsg );
											}
										break;
										case GETFRIENDS:
											{
												String list = __fdb.getFriends( __me ).toJSONString( );
												
												__outBuff.writeChars( list );
											}
										break;
										case GETNFRIENDS:
											{
												long n = __fdb.getNFriends( __me );
												
												__outBuff.writeLong( n );
											}
										break;
										case CHSTARTED:
											{
												
											}
										break;
										case SENDCH:
											{
											
											}
										break;
										case UPDATEINFO:
											{
												String name = __inBuff.readLine( );
												String sname = __inBuff.readLine( );
												String psw = __inBuff.readLine( );
												
												__me.resetName( name );
												__me.resetSurname( sname );
												__me.resetPassword( psw );
												
												ACK ret = __udb.updateUser( __me );
												
												String ackmsg = ret.name( );
												
												__outBuff.writeChars( ackmsg );
											}
										break;
										case GETPOINTS:
											{
												int score = __me.getTotScore( );
												
												__outBuff.write( score );
											}
										break;
										case GETRANK:
											{
												HashMap<String, Integer> ranking = __udb.getRanking( );
												HashMap<String, Integer> myranking = new HashMap<String, Integer>( );
												JSONArray friendlist = __fdb.getFriends( __me );
												
												for( int i = 0; i < friendlist.size( ); i++ )
													{
														String friend = (String) friendlist.get( i );
														
														myranking.put( friend, ranking.get( friend ) );
													}
												
												myranking.toString( );
												
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
			}
	}
