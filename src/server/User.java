/************************************************
 * 																							*
 * 										USER											*
 *																							*
 ************************************************
 *
 * In questo file vengono implementati i metodi e le variabili 
 * per modellare e gestire la classe 'utente' che rappresenta il
 * client.
 * 
 * @author	Luca Canessa (Mat. 516639)
 * @version	%I%
 * @since		1.0
 */
package server;

import java.io.Serializable;


public class User implements Serializable
	{

		private static final long serialVersionUID = 1L;
		//						//
		//	VARIABLES	//
		//						//
		private String 	__ID; //user id
		private String 	__password; //user password
		private String 	__name; //user name
		private String 	__surname; //user surname		
		private ACK     __status; //current user status
		private int 		__tScore; //total user score
		private int 		__chScore; //last challenge score

		
		
		//					//
		//	METHODS	//
		//					//

		/**
		 * Costruttore a due parametri che rappresentano codice univoco dell'utente
		 * e la sua password. Le altre variabili sono impostate con valore di default
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
		 * Restituisce il nickname dell'utente
		 * 
		 * @return il codice univoco (nickname)
		 */
		public String getID( )
			{
				return this.__ID;
			}
		
		/**
		 * Restituisce lo stato attuale dell'utente come tipo UStatus,
		 * il valore di default è OFFLINE
		 * 
		 * @return lo stato dell'utente
		 */
		public ACK getStatus( )
			{
				return this.__status;
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
