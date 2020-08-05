/************************************************
 * 																							*
 * 										USER											*
 *																							*
 ************************************************
 *
 */
package server;

import java.io.Serializable;



/**
 * In questo file vengono implementati i metodi e le variabili necessari
 * per modellare e gestire la classe 'User' che rappresenta il
 * client. L'User è caratterizzato da proprietà che possono essere modificate
 * una volta che l'User è creato. L'User è identificato UNIVOCAMENTE dal suo
 * nickname che lo distingue da tutti gli altri User
 *
 * @class   User
 * @author	Luca Canessa (Mat. 516639)
 * @version	1.4
 * @since		1.0
 */
public class User implements Serializable
	{
		//						//
		//	VARIABLES	//
		//						//
		private static final long serialVersionUID = 1L; ///< variabile necessaria per rendere l'oggetto serializzabile
		private String 	__ID; ///< nickname dell'utente - ID del database
		private String 	__password; ///< password dell'utente
		private String 	__name; ///< nome dell'utente
		private String 	__surname; ///< cognome dell'utente
		private ACK     __status; ///< stato sul server dell'utente
		private int 		__tScore; ///< numero di punti totalizzati dall'utente
		private int 		__chScore; ///< numero di punti ottenuti durante l'ultima parita dall'utente



		//					//
		//	METHODS	//
		//					//

		/**
		 * Costruttore a due parametri che rappresentano codice univoco dell'utente
		 * e la sua password. Le altre variabili sono impostate con valore di default.
		 * 
		 * @param Username	codice univoco (nickname) dell'utente
		 * @param Password	password di accesso dell'utente
		 */
		public User( String Username, String Password )
			{
				this.__ID = Username;
				this.__password = Password;
				this.__name = null;
				this.__surname = null;
				this.__status = ACK.OFFLINE;
				this.__tScore = 0;
				this.__chScore = 0;
			}
		
		/**
		 * Costuttore a quattro parametri che rappresentano rispettivamente
		 * il codice univoco dell'utente, la sua password, il nome e il cognome
		 * le altre variabili sono settate al valore di default
		 * 
		 * @param Username	codice univoco (nickname) dell'utente
		 * @param Password	password di accesso dell'utente
		 * @param Name 			nome dell'utente
		 * @param Surname		cognome dell'utente
		 */
		public User( String Username, String Password, String Name, String Surname )
			{
				this.__ID = Username;
				this.__password = Password;
				this.__name = Name;
				this.__surname = Surname;
				this.__status = ACK.OFFLINE;
				this.__tScore = 0;
				this.__chScore = 0;
			}
		
		/**
		 * Restituisce il nickname dell'User
		 * 
		 * @return  il codice univoco (nickname) come String
		 */
		public String getID( )
			{
				return __ID;
			}
		
		/**
		 * Restituisce lo stato attuale dell'utente come tipo UStatus,
		 * il valore di default è OFFLINE
		 * 
		 * @return lo stato dell'utente
		 */
		public ACK getStatus( )
			{
				return __status;
			}
		
		/**
		 * Restutuisce il nome dell'utente se è stato impostato, null altrimenti
		 * 
		 * @return il nome dell'utente
		 */
		public String getName( )
			{
				return this.__name;
			}
		
		/**
		 * Restituisce il cognome dell'utente se impostato, null altrimenti
		 * 
		 * @return il cognome dell'utente
		 */
		public String getSurname( )
			{
				return this.__surname;
			}
		
		/**
		 * Restituisce la password dell'utente
		 * 
		 * @return la password dell'utente
		 */
		protected String getPassword( )
			{
				return this.__password;
			}
		
		/**
		 * Restituisce il valore del punteggio attuale totale dell'utente
		 * 
		 * @return punteggio totale
		 */
		public int getTotScore( )
			{
				return this.__tScore;
			}
		
		/**
		 * Restituisce il valore del punteggio guadagnato durante l'ultima partita
		 * 
		 * @return punteggio della partita
		 */
		public int getChalScore( )
			{
				return this.__chScore;
			}
		
		/**
		 * Imposta lo stato dell'utente come ONLINE
		 */
		public void setOnline( )
			{
				this.__status = ACK.ONLINE;
			}
		
		/**
		 * Imposta lo stato dell'utente come OFFLINE
		 */
		public void setOffline( )
			{
				this.__status = ACK.OFFLINE;
			}
		
		/**
		 * Imposta lo stato dell'utente come INCHALLENGE 
		 */
		public void setInChallenge( )
			{
				this.__status = ACK.INCHALLENGE;
			}
		
		/**
		 * Imposta il valore dei punti guadagnati aggiornando il valore
		 * il punteggio totale dell'utente
		 * 
		 * @param Score	valore punteggio
		 */
		public void setTScore( int Score )
			{
				this.__tScore += Score;
			}
		
		/**
		 * Imposta il valore dei punti guadagnati nell'ultima partita
		 * 
		 * @param Score	valore ottenuto dall'ultima partita
		 */
		public void setCScore( int Score )
			{
				this.__chScore = Score;
			}
		
		/**
		 * Imposta il nome dell'utente
		 * 
		 * @param Name	nome dell'utente
		 */
		public void resetName( String Name )
			{
				this.__name = Name;
			}
		
		/**
		 * Imposta il cognome dell'utente
		 * 
		 * @param Surname	cognome dell'utente
		 */
		public void resetSurname( String Surname )
			{
				this.__surname = Surname;
			}
		
		/**
		 * Reimposta password
		 * 
		 * @param Password	nuova password
		 */
		public void resetPassword( String Password )
			{
				this.__password = Password;
			}
				
		/**
		 * Compara la password passatagli con quella impostata dall'utente
		 * e ne ritorna valore
		 * 
		 * @param Password	password da controllare
		 * @return	true se le password coincidono, false altrimenti
		 */
		public boolean pswIsEqual( String Password )
			{
				return this.__password.equals( Password );
			}
	}
