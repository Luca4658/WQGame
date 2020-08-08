package client;

import server.RegRMInterface;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;



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




public class Main
	{
		protected static Registry         reg = null;
		protected static RegRMInterface   stub = null;
		private   static Socket           socket = null;
		private   static BufferedReader   inBuff = null;
		private   static DataOutputStream outBuff = null;

		public static void main( String[] args ) throws IOException
			{
				try
					{
						reg = LocateRegistry.getRegistry( 21895 );
						stub = (RegRMInterface) reg.lookup( "ServerRMI" );
					}
				catch( IOException | NotBoundException e )
					{
						System.exit( -1 );
					}

				connect( );
				CGUI t = new CGUI( "Word Quizzle Game" );
			}


		public static void send( String toSend ) throws IOException
			{
				outBuff.writeBytes( toSend + "\n" );
			}

		public static String recv( ) throws IOException
			{
				return inBuff.readLine( );
			}

		public static void connect( )
			{
				try
					{
						socket = new Socket( "localhost", 46058 );
						inBuff = new BufferedReader( new InputStreamReader( socket.getInputStream( ) ) );
						outBuff = new DataOutputStream( socket.getOutputStream( ) );
					}
				catch( IOException e )
					{
						e.printStackTrace( );
					}
			}
	}
