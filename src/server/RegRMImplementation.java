/************************************************
 *                                              *
 *            REGRMIMPLEMENTATION              *
 *                                              *
 ************************************************
 *
 */
package server;

import java.rmi.RemoteException;
import java.rmi.server.*;



/**
 * In questa classe viene implementata e gestita la Remote Method Invocation
 * usata dai client per effettuare la registrazione dell'User.
 * Utilizza il metodo inserUser della classe Users per inserire l'utente
 *
 * @class   RegRMImplementation
 * @author  Luca Canessa (Mat. 516639)
 * @version 1.1
 * @since   1.0
 */
public class RegRMImplementation extends RemoteServer implements RegRMInterface 
	{
		private static final long serialVersionUID = 1L; ///< variabile necessaria per la serializzazione dell'oggetto
		private Users       __udb; ///< database degli utenti
		private Friendships __fdb; ///< database delle amicizie

		/**
		 * Costruttore della RMI necessaria per permettere la registrazione di un utente
		 *
		 * @param usrs  database degli User
		 * @param frd    database delle Friendships
		 */
		public RegRMImplementation( Users usrs, Friendships frd )
			{
				__udb = usrs;
				__fdb = frd;
			}

		/**
		 * Implementazione del metodo usato per registrare l'User.
		 * Tale metodo si basa sul metodo all'interno della classe Users
		 * predisposto per questo utilizzo. Ritorna un valore di ACK.
		 *
		 * @param u User da inserire del database
		 * @return ACK (dipende da Users.insertUser())
		 * @throws RemoteException
		 */
		@Override
		public ACK RegUser( User u ) throws RemoteException
			{
				return __udb.insertUser( u, __fdb );
			}
	}
