/************************************************
 *                                              *
 *                  CONFPARSER                  *
 *                                              *
 ************************************************
 *
 */
package client;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;



/**
 * In questa classe viene gestito il parser del file di configurazione dal
 * quale si estraggono i valori di default del client. Di default il file
 * di configurazione è all'interno della cartella conf.
 * Il file è composto da tre oggetti principali:
 * - CLIENT:  ci sono tutti i valori che servono per le configurazioni di base
 *            del server
 * - GAME:  ci sono tutti i valori utili per settare le sfide tra gli utenti
 *
 *  !!! NECCESSARIO CHE NESSUN VALORE DELLE CHIAVI IN QUESTO FILE SIA VUOTO !!!
 *  controlla che tutti i campi siano consistenti appena l'oggetto viene creato
 *
 * @class   ConfParser
 * @author  Luca Canessa (Mat. 516639)
 * @version 1.2
 * @since   1.0
 */
public class ConfParser
  {
    //						//
    //	VARIABLES	//
    //						//
    private JSONObject __conf = null; ///< configurazione completa del server
    private String     path = null; ///< percorso verso il file di configurazione


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
        path = confPath;
        JSONParser parser = new JSONParser( );

        try
          {
            __conf = (JSONObject)parser.parse( new FileReader( confPath ) );
          }
        catch( FileNotFoundException e )
          {
            System.err.println( "Config file does not exist" );
            System.exit( -1 );
          }
        catch( IOException e )
          {
            System.err.println( "Problem to open config file" );
            System.exit( -1 );
          }
        catch( ParseException e )
          {
            System.out.println( e );
            System.exit( -1 );
          }

        getRMIPort( );
        getRMIName( );
        getListnerPort( );
        getTimeoutGame( );
        getTimeoutReply( );
      }


  /**
   * Cerca all'interno della configurazione il campo 'rmiport', estraendo il suo
   * valore. Controlla poi se tale valore è utilizzabile, in caso negativo esce
   * restituendo un errore.
   *
   * @return  il numero di porta da utilizzare per la 'RegRMI'
   */
    public int getRMIPort( )
      {
        String path = (String) __conf.get( "rmiport" );

        if( path.equals( "null" ) || path.equals( "" ) )
          {
            System.err.println( "Error: Empty port RMI" );
            System.exit( -1 );
          }

        int port = Integer.valueOf( path );

        return port;
      }

  /**
   * Cerca all'interno della configurazione il campo 'rminame', estraendo il suo
   * valore. Controlla poi se tale valore è utilizzabile, in caso negativo esce
   * restituendo un errore.
   *
   * @return  il nome del server RMI
   */
    public String getRMIName( )
      {
        String name = (String) __conf.get( "rmiport" );

        if( name.equals( "null" ) || name.equals( "" ) )
          {
            System.err.println( "Error: Empty port RMI" );
            System.exit( -1 );
          }

        return name;
      }

    /**
     * Cerca all'interno della configurazione il campo 'port', estraendo il suo
     * valore. Controlla poi se tale valore è utilizzabile, in caso negativo
     * esce restituendo un errore.
     *
     * @return  il numero di porta da utilizzare per la socket listener su cui
     *          il server accetta le connessioni in entrata
     */
    public int getListnerPort( )
      {
        String s = (String)__conf.get( "port" );
        if( s.equals( "null" ) || s.equals( "" ) )
          {
            System.err.println( "Error: Empty port" );
            System.exit( -1 );
          }

        return Integer.valueOf( s );
      }

    /**
     * Cerca all'interno della configurazione il campo 'timeoutgame', estraendo
     * il suo valore. Controllapoi se tale valore è utilizzabile, in caso
     * negativo esce restituendo un errore.
     *
     * @return  il tempo massimo per completare una sfida
     */
    public long getTimeoutGame( )
      {
        String s = (String)__conf.get( "timeoutgame" );
        if( s.equals( "null" ) || s.equals( "" ) )
          {
            System.err.println( "Error: Empty words" );
            System.exit( -1 );
          }

        return Long.valueOf( s );
      }

    /**
     * Cerca all'interno della configurazione il campo 'timeoutreq', estraendo
     * il suo valore. Controlla poi se tale valore è utilizzabile, in caso
     * negativo esce restituendo un errore.
     *
     * @return  il tempo massimo per accettare una richiesa di sfida
     */
    public long getTimeoutReply( )
      {
        String s = (String)__conf.get( "timeoutreq" );
        if( s.equals( "null" ) || s.equals( "" ) )
          {
            System.err.println( "Error: Empty words" );
            System.exit( -1 );
          }

        return Long.valueOf( s );
      }

    /**
     * Cerca all'interno della configurazione il campo 'rminame', estraendo il suo
     * valore. Controlla poi se tale valore è utilizzabile, in caso negativo esce
     * restituendo un errore.
     *
     * @return  il nome del server RMI
     */
    public void setTimeoutGame( String time )
      {
        __conf.put( "timeoutgame", time );
        updateConf( );
      }

    private void updateConf( )
      {
        FileWriter __confFile = null;
        try
          {
            __confFile = new FileWriter( path );
          }
        catch (IOException e)
          {
            // TODO: handle exception
          }
        try
          {
            __confFile.write( __conf.toJSONString( ) );
          }
        catch( Exception e )
          {
            // TODO: handle exception
          }
        try
          {
            __confFile.flush( );
            __confFile.close( );
          }
        catch (Exception e)
          {
            // TODO: handle exception
          }
      }
  }
