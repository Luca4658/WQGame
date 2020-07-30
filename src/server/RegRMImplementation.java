package server;

import java.rmi.RemoteException;
import java.rmi.server.*;


public class RegRMImplementation extends RemoteServer implements RegRMInterface 
	{
		@Override
		public ACK RegUser( User usr, Users usrs, Friendships fdb ) throws RemoteException
			{
				return usrs.insertUser( usr, fdb );
			}
	}
