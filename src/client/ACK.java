/************************************************
 *                                              *
 *                    ACK                       *
 *                                              *
 ************************************************
 *
 */
package client;

/**
 * In questo file vengono implementati i messaggi ricevuti dal server a
 * termine delle operazioni richieste
 *
 * @class   ACK
 * @author  Luca Canessa (Mat. 516639)
 * @version 1.0
 * @since   1.0
 */
public enum ACK
	{
		//	USER	//
		PasswordUnmatch, ///< password non corrisponde a quella del User
		UserAlreadyLoggedIn, ///< User già entrato
		NotLogged, ///<< User non loggato
		LoggedIn, ///<User loggato
		LoggedOut, ///<User uscito
		ONLINE, ///<stato dell'User online
		OFFLINE, ///<stato dell'User  offline
		INCHALLENGE, ///<stato dell'User in sfida

		//	USERS	//
		AlreadyRegistered, ///<User già registrato
		EmptyField, ///<campi utente vuoti
		UserRegistered, ///<User registrato
		RegistrationError, ///<problema generale durante la registrazione dell'User
		UserFound, ///<User trovato nel database
		UserNotFound, ///<User non trovato nel database
		UserDeleted, ///<User deregistrato
		UserNotDeleted, ///<User non deregistrato
		
		//	FRIENDS	//
		AlreadyFriends, ///<User(s) già amici
		FriendNotFound, ///<User amico non trovato
		FriendAdded, ///<User registrato come amico
		UserAdded, ///<User inserito nel database
		AlreadyExisting, ///<
		FriendRemoved, ///<User amico rimosso dalla lista degli amici

		//  CHALLENGE //
		Accepted, ///<sfida accettata
		Rejected, ///<sfida rifiutata

		// GENERAL  //
		OperationUnknown, ///<operazione richiesta non riconosciuta
		OK, ///<azione eseguita
		ERROR; ///<errore generale durante l'operazione
	}
