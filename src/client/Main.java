package client;

import server.RegRMInterface;

import java.io.IOException;
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
		protected static Registry        reg = null;
		protected static RegRMInterface  stub = null;

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

				CGUI t = new CGUI( "Word Quizzle Game" );

//
//				DataOutputStream mout = null;
//				BufferedReader min = null;
//
//				try
//					{
//						Socket me = new Socket( "localhost", 46058 );
//						mout = new DataOutputStream( me.getOutputStream( ) );
//						min = new BufferedReader( new InputStreamReader( me.getInputStream( ) ) );
//					}
//				catch( IOException e )
//					{
//						e.printStackTrace( );
//					}
//
//
//				try
//					{
//						mout.writeBytes( ClientMSG.LOGIN.name() + "\n");
//						mout.writeBytes( u.getID( ) + "\n");
//						mout.writeBytes( "Password" + "\n" );
//
//
//						System.out.println( min.readLine( ) );
//
//						mout.writeBytes( ClientMSG.GETFRIENDS.name( ) + "\n" );
//						System.out.println( min.readLine( ) );
//
//						mout.writeBytes( ClientMSG.GETNFRIENDS.name( ) + "\n" );
//						System.out.println( min.readLine( ) );
//
////						Thread.sleep( 10000 );
//
//						mout.writeBytes( ClientMSG.STARTCH.name( ) + "\n" );
//						System.out.println( "Write Friend" );
//						String frd = br.readLine( );
//						System.out.println( frd );
//						mout.writeBytes( frd + "\n" );
//						System.out.println( min.readLine( ) );
//
//
//
//
//
//						mout.writeBytes( ClientMSG.GETPOINTS.name( ) + "\n" );
//						System.out.println( min.readLine( ) );
//
//						mout.writeBytes( ClientMSG.GETRANK.name( ) + "\n" );
//						System.out.println( min.readLine( ) );
//
//						mout.writeBytes( "ILBUDELLODITUMA" + "\n" );
//						System.out.println( min.readLine( ) );
//
//
//
//						mout.writeBytes( ClientMSG.LOGOUT.name( ) + "\n" );
//						System.out.println( min.readLine( ) );
//
//						Thread.sleep( 100 );
//
//					}
//				catch( IOException | InterruptedException /*| InterruptedException*/ e )
//					{
//						e.printStackTrace( );
//					}


			}
	}
