package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;



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
								switch( __inBuff.read( ) )
									{
										case( 1 ):
											{
											
											}
										
										break;

									default:
										break;
									}
							} catch( IOException e )
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					}
			}
	}
