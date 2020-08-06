/************************************************
 *                                              *
 *                   CHALLENGE                  *
 *                                              *
 ************************************************/

package server;

import org.json.simple.JSONArray;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;

import java.util.ArrayList;



/**
 * Classe che modella l'oggetto Challenge di un User per gestire la sua sfida
 * implementando metodi e variabili utili per controllare e gestire la sfida
 * tra i client inviato e ricevendo le parole rispettivamente in italiano e in inglese
 *
 * @class   Challenge
 * @author  Luca Canessa (Mat. 516639)
 * @version 1.1
 * @since   1.0
 */
class Challenge implements Runnable
  {
    //            //
    //  VARIABLES //
    //            //
    private Users                         __udb = null; ///<database degli utenti
    private DataOutputStream              __outBuff = null; ///<stampa sulla socket
    private BufferedReader                __inBuff = null; ///<legge sulla socket
    private ArrayList<ArrayList<String>>  __words = null; ///<matrice con parole italiane e parole inglesi
    private User                          __user = null; ///<User che sta giocando
    private Thread                        __usrT = null; ///<thread principale del client


    //          //
    //  METHODS //
    //          //
    /**
     * Costruttore della sfida tra due utenti. Gestisce la sfida per ogni
     * utente se viene lanciato da thread, inviando le parole italiane e
     * ricevendo quelle tradotte dal client. Al termine della sfida aggiorna
     * i parametri dell'utente con i punti guadagnati e invia al proprio client
     * il vincitore della sfida
     *
     * @param inputb  buffer su cui può ricevere i dati dal client
     * @param outputb buffer su cui può inviare i dati al client
     * @param Words   matrice con le parole e le rispettive traduzioni
     * @param usr     User che sta giocando attualmente
     * @param udb     Database
     * @param T       Thread principale del client
     */
    public Challenge( BufferedReader inputb, DataOutputStream outputb, ArrayList<ArrayList<String>> Words, User usr, Users udb, Thread T )
      {
        __inBuff = inputb;
        __outBuff = outputb;
        __words = Words;
        __user = usr;
        __udb = udb;
        __usrT = T;
      }

    /**
     * Rimane in attesa sulla socket per ricevere i dati dal client
     *
     * @return  il buffer (String) contenente i dati
     */
    private String recv( )
      {
        try
          {
            return __inBuff.readLine( );
          }
        catch( IOException e )
          {
            Main.logger( "Problem to recv MSG" );
          }

        return null;
      }

    /**
     * Invia al client attraverso la socket i dati
     *
     * @param str stringa contenente i dati da mandare al client
     */
    private void send( String str )
      {
        try
          {
            __outBuff.writeBytes( str + "\n" ); //importante '\n' perché il client riceva correttamente
          }
        catch( IOException e )
          {
            Main.logger( "Problem to send MSG" );
          }
      }

    /**
     * Metodo che mette in esecuzione il thread che gesitsce il gioco di un
     * client. Invia una parola italaian alla volta e ne riceve la
     * corrispettiva traduzione del User dal client, ne controlla la
     * correttezza e ne aggiorna i punti, sommando un valore se corretto o
     * levando dal toale della partita un altro valore. Tali valori di partita
     * sono presi dal file di configurazione. Quando termina aggiorna i punti
     * della partita del User e alza un interrupt per il thread principale che
     * lo avvisa della terminazione del gioco.
     */
    @Override
    public void run( )
      {
        String wordRec = null;
        JSONArray storeWord = null;
        boolean isFinished = false;
        int score = 0;

        Main.logger( "Start to send words to " + __user.getID( ) );

        for( int iWord = 0; ( iWord < __words.get(0).size( ) ) && !isFinished; iWord++ )
          {
            send( __words.get(0).get( iWord ) );
            wordRec = recv( );

            if( wordRec.equals( "**end**" ) )
              {
                isFinished = true;
                Main.logger( __user.getID() + " ended game" );
                continue;
              }
            else
              {
                if( wordRec.equals(  __words.get(1).get( iWord ) ) )
                  {
                    score += Main.parser.getCorrectPoints( );
                  }
                else
                  {
                    score -= Main.parser.getWrongPoints( );
                  }
              }
          }

        __user.setCScore( score );
        __udb.updateUser( __user );

        if( !isFinished )
          {
            Main.logger( __user.getID() + " was interrupted" );
            __usrT.interrupt( );
          }
      }
  }
