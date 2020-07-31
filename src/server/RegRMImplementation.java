package server;

import java.rmi.RemoteException;
import java.rmi.server.*;


public class RegRMImplementation extends RemoteServer implements RegRMInterface 
	{
		private static final long serialVersionUID = 1L;
		private Users __udb;
		private Friendships __fdb;
		
		public RegRMImplementation( Users usrs, Friendships frd )
			{
				__udb = usrs;
				__fdb = frd;
			}
		
		
		@Override
		public ACK RegUser( User u ) throws RemoteException
			{
				return __udb.insertUser( u, __fdb );
			}
	}
