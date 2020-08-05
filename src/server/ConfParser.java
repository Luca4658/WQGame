/************************************************
 *                                              *
 *                  CONFPARSER                  *
 *                                              *
 ************************************************
 *
 */
package server;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;



/**
 * In questa classe viene gestito il parser del file di configurazione dal
 * quale si estraggono i valori di default del server. Di default il file
 * di configurazione è all'interno della cartella conf.
 * Il file è composto da tre oggetti principali:
 * - SERVER:  ci sono tutti i valori che servono per le configurazioni di base
 *            del server
 * - GAME:  ci sono tutti i valori utili per settare le sfide tra gli utenti
 * - DB:  ci sono tutti i valori necessari per impostare i database degli
 *        utenti e delle amicizie tra gli User
 *
 *  !!! NECCESSARIO CHE NESSUN VALORE DELLE CHIAVI IN QUESTO FILE SIA VUOTO !!!
 *  controlla che tutti i campi siano consistenti appena l'oggetto viene creato
 *
 * @class   ConfParser
 * @author  Luca Canessa (Mat. 516639)
 * @version 2.0
 * @since   1.0
 */
public class ConfParser
  {
    //						//
    //	VARIABLES	//
    //						//
    private JSONObject __conf = null; ///< configurazione completa del server


    //					//
    //	METHODS	//
    //					//

    /**
     * Costruttore del parser. Prendendo come parametro la path del file JSON
     * su cui sono salvate le configurazioni, si preoccupa di salvare l'intero
     * file in una struttura JSONObject per poter essere consultata
     *
     * @param confPath  path del file di configurazione
     */
    public ConfParser( String confPath )
      {
        JSONParser parser = new JSONParser( );

        try
          {
            __conf = (JSONObject)parser.parse( new FileReader( confPath ) );
          }
        catch( FileNotFoundException e )
          {
            System.out.println( "Config file does not exist" );
            System.exit( -1 );
          }
        catch( IOException e )
          {
            System.out.println( "Problem to open config file" );
            System.exit( -1 );
          }
        catch( ParseException e )
          {
            System.out.println( e );
            System.exit( -1 );
          }
        getCorrectPoints( );
        getDictionaryPath( );
        getExtraPoints( );
        getFriendPath( );
        getListnerPort( );
        getNWords( );
        getRMIPort( );
        getTimeoutGame( );
        getTimeoutReply( );
        getTimeUpdater( );
        getUsersPath( );
        getWrongPoints( );
      }

    /**
     * Cerca all'interno della configurazione il campo 'userpath',
     * estrapolandolo dall'oggetto 'DB', estraendo il suo valore. Controlla poi
     * se tale valore è utilizzabile, in caso negativo esce restituendo un errore.
     *
     * @return  il percorso al DB degli utenti
     */
    public String getUsersPath( )
      {
        JSONObject field = (JSONObject) __conf.get( "DB" );
        String path = (String) field.get( "userspath" );

        if( path.equals( "null" ) || path.equals( "" ) )
          {
            System.err.println( "Error: Empty Path 'userspath'" );
            System.exit( -1 );
          }

        return path;
      }

    /**
     * Cerca all'interno della configurazione il campo 'friendpath',
     * estrapolandolo dall'oggetto 'DB', estraendo il suo valore. Controlla poi
     * se tale valore è utilizzabile, in caso negativo esce restituendo un errore.
     *
     * @return  il percorso al DB delle amicizie degli utenti
     */
    public String getFriendPath( )
      {
        JSONObject field = (JSONObject) __conf.get( "DB" );
        String path = (String) field.get( "friendpath" );

        if( path.equals( "null" ) || path.equals( "" ) )
          {
            System.err.println( "Error: Empty Path 'friendpath'" );
            System.exit( -1 );
          }

        return path;
      }

    /**
     * Cerca all'interno della configurazione il campo 'dictionary',
     * estrapolandolo dall'oggetto 'SERVER', estraendo il suo valore. Controlla
     * poi se tale valore è utilizzabile, in caso negativo esce restituendo un
     * errore.
     *
     * @return  il percorso al dizionario delle parole italiane
     */
    public String getDictionaryPath( )
      {
        JSONObject field = (JSONObject) __conf.get( "SERVER" );
        String path = (String) field.get( "dictionary" );

        if( path.equals( "null" ) || path.equals( "" ) )
          {
            System.err.println( "Error: Empty Path 'dictionary'" );
            System.exit( -1 );
          }

        return path;
      }

  /**
   * Cerca all'interno della configurazione il campo 'rmiport',
   * estrapolandolo dall'oggetto 'SERVER', estraendo il suo valore. Controlla
   * poi se tale valore è utilizzabile, in caso negativo esce restituendo un
   * errore.
   *
   * @return  il numero di porta da utilizzare per la 'RegRMI'
   */
    public int getRMIPort( )
      {
        JSONObject field = (JSONObject) __conf.get( "SERVER" );
        String path = (String) field.get( "rmiport" );

        if( path.equals( "null" ) || path.equals( "" ) )
          {
            System.err.println( "Error: Empty port RMI" );
            System.exit( -1 );
          }

        int port = Integer.valueOf( path );

        return port;
      }

    /**
     * Cerca all'interno della configurazione il campo 'port',
     * estrapolandolo dall'oggetto 'SERVER', estraendo il suo valore. Controlla
     * poi se tale valore è utilizzabile, in caso negativo esce restituendo un
     * errore.
     *
     * @return  il numero di porta da utilizzare per la socket listener su cui
     *          il server accetta le connessioni in entrata
     */
    public int getListnerPort( )
      {
        JSONObject field = (JSONObject) __conf.get( "SERVER" );
        String s = (String)field.get( "port" );
        if( s.equals( "null" ) || s.equals( "" ) )
          {
            System.err.println( "Error: Empty port" );
            System.exit( -1 );
          }

        return Integer.valueOf( s );
      }

    /**
     * Cerca all'interno della configurazione il campo 'correct',
     * estrapolandolo dall'oggetto 'GAME', estraendo il suo valore. Controlla
     * poi se tale valore è utilizzabile, in caso negativo esce restituendo un
     * errore.
     *
     * @return  il numero di punti da assegnare ad ogni risposta corretta
     *          durante una sfida
     */
    public int getCorrectPoints( )
      {
        JSONObject field = (JSONObject) __conf.get( "GAME" );
        String s = (String)field.get( "correct" );
        if( s.equals( "null" ) || s.equals( "" ) )
          {
            System.err.println( "Error: Empty correct" );
            System.exit( -1 );
          }

        return Integer.valueOf( s );
      }

    /**
     * Cerca all'interno della configurazione il campo 'wrong',
     * estrapolandolo dall'oggetto 'GAME', estraendo il suo valore. Controlla
     * poi se tale valore è utilizzabile, in caso negativo esce restituendo un
     * errore.
     *
     * @return  il numero di punti che devono essere sottratti ad ogni risposta
     *          sbagliata durante una sfida
     */
    public int getWrongPoints( )
      {
        JSONObject field = (JSONObject) __conf.get( "GAME" );
        String s = (String)field.get( "wrong" );
        if( s.equals( "null" ) || s.equals( "" ) )
          {
            System.err.println( "Error: Empty wrong" );
            System.exit( -1 );
          }

        return Integer.valueOf( s );
      }

    /**
     * Cerca all'interno della configurazione il campo 'extra',
     * estrapolandolo dall'oggetto 'GAME', estraendo il suo valore. Controlla
     * poi se tale valore è utilizzabile, in caso negativo esce restituendo un
     * errore.
     *
     * @return  il numero dei punti extra da assegnare al vincitore di una
     *          partita
     */
    public int getExtraPoints( )
      {
        JSONObject field = (JSONObject) __conf.get( "GAME" );
        String s = (String)field.get( "extra" );
        if( s.equals( "null" ) || s.equals( "" ) )
          {
            System.err.println( "Error: Empty extra" );
            System.exit( -1 );
          }

        return Integer.valueOf( s );
      }

    /**
     * Cerca all'interno della configurazione il campo 'words',
     * estrapolandolo dall'oggetto 'GAME', estraendo il suo valore. Controlla poi
     * se tale valore è utilizzabile, in caso negativo esce restituendo un errore.
     *
     * @return  il numero di parole che devono essere inviate agli sfidanti per
     *          una partita
     */
    public int getNWords( )
      {
        JSONObject field = (JSONObject) __conf.get( "GAME" );
        String s = (String)field.get( "words" );
        if( s.equals( "null" ) || s.equals( "" ) )
          {
            System.err.println( "Error: Empty words" );
            System.exit( -1 );
          }

        return Integer.valueOf( s );
      }

    /**
     * Cerca all'interno della configurazione il campo 'timeoutgame',
     * estrapolandolo dall'oggetto 'GAME', estraendo il suo valore. Controlla
     * poi se tale valore è utilizzabile, in caso negativo esce restituendo un
     * errore.
     *
     * @return  il tempo massimo per completare una sfida
     */
    public long getTimeoutGame( )
      {
        JSONObject field = (JSONObject) __conf.get( "GAME" );
        String s = (String)field.get( "timeoutgame" );
        if( s.equals( "null" ) || s.equals( "" ) )
          {
            System.err.println( "Error: Empty words" );
            System.exit( -1 );
          }

        return Long.valueOf( s );
      }

    /**
     * Cerca all'interno della configurazione il campo 'timeoutreq',
     * estrapolandolo dall'oggetto 'SERVER', estraendo il suo valore. Controlla poi
     * se tale valore è utilizzabile, in caso negativo esce restituendo un errore.
     *
     * @return  il tempo massimo per accettare una richiesa di sfida
     */
    public long getTimeoutReply( )
      {
        JSONObject field = (JSONObject) __conf.get( "SERVER" );
        String s = (String)field.get( "timeoutreq" );
        if( s.equals( "null" ) || s.equals( "" ) )
          {
            System.err.println( "Error: Empty words" );
            System.exit( -1 );
          }

        return Long.valueOf( s );
      }

    /**
     * Cerca all'interno della configurazione il campo 'updaterdb',
     * estrapolandolo dall'oggetto 'SERVER', estraendo il suo valore. Controlla poi
     * se tale valore è utilizzabile, in caso negativo esce restituendo un errore.
     *
     * @return  ogni quanti millisecondi il database viene aggiornato su file
     */
    public long getTimeUpdater( )
      {
        JSONObject field = (JSONObject) __conf.get( "SERVER" );
        String s = (String)field.get( "updaterdb" );
        if( s.equals( "null" ) || s.equals( "" ) )
          {
            System.err.println( "Error: Empty words" );
            System.exit( -1 );
          }

        return Long.valueOf( s );
      }

  }
