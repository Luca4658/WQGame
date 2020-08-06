package client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import server.RegRMInterface;
import server.User;


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
		public static void main( String[] args ) throws IOException
			{
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				System.out.println( "Enter nickname" );
				String nick = new String( br.readLine( ) );
				User u = new User( nick, "Password" );
				
//				try
//					{
//						Registry reg = LocateRegistry.getRegistry( 21895 );
//						RegRMInterface stub = (RegRMInterface) reg.lookup( "ServerRMI" );
//						System.err.println( stub.RegUser( u ) );
//					}
//				catch( RemoteException | NotBoundException e )
//					{
//						e.printStackTrace();
//					}

				DataOutputStream mout = null;
				BufferedReader min = null;

				try
					{
						Socket me = new Socket( "localhost", 46058 );
						mout = new DataOutputStream( me.getOutputStream( ) );
						min = new BufferedReader( new InputStreamReader( me.getInputStream( ) ) );
					}
				catch( IOException e )
					{
						e.printStackTrace( );
					}


				try
					{
						mout.writeBytes( ClientMSG.LOGIN.name() + "\n");
						mout.writeBytes( u.getID( ) + "\n");
						mout.writeBytes( "Password" + "\n" );


						System.out.println( min.readLine( ) );

						mout.writeBytes( ClientMSG.GETFRIENDS.name( ) + "\n" );
						System.out.println( min.readLine( ) );

						mout.writeBytes( ClientMSG.GETNFRIENDS.name( ) + "\n" );
						System.out.println( min.readLine( ) );

//						Thread.sleep( 10000 );

						mout.writeBytes( ClientMSG.STARTCH.name( ) + "\n" );
						System.out.println( "Write Friend" );
						String frd = br.readLine( );
						System.out.println( frd );
						mout.writeBytes( frd + "\n" );
						System.out.println( min.readLine( ) );





						mout.writeBytes( ClientMSG.GETPOINTS.name( ) + "\n" );
						System.out.println( min.readLine( ) );

						mout.writeBytes( ClientMSG.GETRANK.name( ) + "\n" );
						System.out.println( min.readLine( ) );

						mout.writeBytes( "ILBUDELLODITUMA" + "\n" );
						System.out.println( min.readLine( ) );



						mout.writeBytes( ClientMSG.LOGOUT.name( ) + "\n" );
						System.out.println( min.readLine( ) );

						Thread.sleep( 100 );

					}
				catch( IOException | InterruptedException /*| InterruptedException*/ e )
					{
						e.printStackTrace( );
					}


			}
	}
