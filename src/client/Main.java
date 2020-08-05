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
		LOGIN,
		LOGOUT,
		ADDFRIEND,
		GETFRIENDS,
		GETNFRIENDS,
		SENDCH,
		ACCEPTEDCH,
		UPDATEINFO,
		GETPOINTS,
		GETRANK;
	}



public class Main
	{
		public static void main( String[] args )
			{

				Scanner usrname = new Scanner( System.in );
				User u = new User( "Luca3", "TuaSorella" );
				
				try
					{
						Registry reg = LocateRegistry.getRegistry( 21895 );
						RegRMInterface stub = (RegRMInterface) reg.lookup( "ServerRMI" );
						System.err.println( stub.RegUser( u ) );
					}
				catch( RemoteException | NotBoundException e )
					{
						e.printStackTrace();
					}

				DataOutputStream mout = null;
				BufferedReader min = null;
//
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
//


//
//				String msg = ClientMSG.ACCEPTEDCH.name() + "\n";
//				try
//					{
//						mout.writeBytes( msg );
//						mout.writeBytes( "Luca1" + "\n" );
//						mout.writeBytes( "Password\n" );
//
//						System.out.println( min.readLine( ) );
//
//						mout.writeBytes( ClientMSG.GETNFRIENDS.name() + "\n" );
//
//						System.out.println( min.readLine( ) );
//
//
//						mout.writeBytes( ClientMSG.GETFRIENDS.name() + "\n" );
//
//						System.out.println( min.readLine( ) );
//
//						mout.writeBytes( ClientMSG.UPDATEINFO.name() + "\n" );
//						mout.writeBytes(  "Luca" + "\n" );
//						mout.writeBytes(  "Cicco" + "\n" );
//						mout.writeBytes(  "TuaSorella" + "\n" );
//
//						System.out.println( min.readLine( ) );
//
//						mout.writeBytes( ClientMSG.GETRANK.name( ) + "\n" );
//						System.out.println( min.readLine() );
//
//						Thread.sleep( 1000 );
//					}
//				catch( IOException /*| InterruptedException*/ e )
//					{
//						e.printStackTrace( );
//					}


			}
	}
