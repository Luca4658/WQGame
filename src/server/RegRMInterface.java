package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RegRMInterface extends Remote
	{
		public ACK RegUser( User usr, Users usrs, Friendships fdb ) throws RemoteException;
	}
