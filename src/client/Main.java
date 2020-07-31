package client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import server.RegRMInterface;
import server.User;

public class Main
	{
		public static void main( String[] args )
			{
				User u = new User( "Luca", "TuaSorella" );
				
				try
					{
						Registry reg = LocateRegistry.getRegistry( 21895 );
						RegRMInterface stub = (RegRMInterface) reg.lookup( "ServerRMI" );
						stub.RegUser( u );
					} 
				catch( RemoteException | NotBoundException e )
					{
						e.printStackTrace();
					}
			}
	}
