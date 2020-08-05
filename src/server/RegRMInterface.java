/************************************************
 *                                              *
 *               REGRMINTERFACE                 *
 *                                              *
 ************************************************
 *
 */
package server;

import java.rmi.Remote;
import java.rmi.RemoteException;




/**
 * In questa interfaccia viene usata per poter usare la RMI per registrare gli
 * User.
 *
 * @class   RegRMInterface
 * @author  Luca Canessa (Mat. 516639)
 * @version 1.1
 * @since   1.0
 */
public interface RegRMInterface extends Remote
	{

		/**
		 * Metodo dichiarato per registrare l'utente. Ritorna un valore di ACK
		 *
		 * @param u User da registrare
		 * @return risultato dell'operazione di registrazione
		 * @throws RemoteException
		 */
		public ACK RegUser( User u ) throws RemoteException;
	}
