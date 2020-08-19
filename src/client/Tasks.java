/************************************************
 *                                              *
 *                    TASKS                     *
 *                                              *
 ************************************************
 *
 */
package client;

import server.User;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

import java.rmi.RemoteException;

import java.util.Iterator;



/**
 * Classe con metodi statici necessari per il compimento dei tasks richiesti
 * dal client
 *
 * @class   Tasks
 * @author  Luca Canessa (Mat. 516639)
 * @version 1.2
 * @since   1.0
 */
class Tasks
  {
    /**
     * Metodo con il compito di richiedere al server di effettuare il login
     * all'utente, inviandogli il suo nickname e la password corrispondente.
     * Ritorna la rispota del server
     *
     * @param nickname  nickname dell'utente
     * @param Password  password dell'utente
     * @return  rec risposta del server all'esecuzione dell'operazione
     */
    public static String login( String nickname, String Password )
      {
        String rec = null;
        try
          {
            Main.send( ClientMSG.LOGIN.name( ) );
            Main.send( nickname );
            Main.send( Password );
            rec = Main.recv();
          }
        catch( IOException e )
          {
            e.printStackTrace( );
          }

        return rec;
      }

    /**
     * Richiesta di registrazione di un utente attraverso il RemoteMethodInvocation.
     * Crea popup di sistema con messaggio del risultato dell'operazione
     * (nickname e password OBBLIGATORI)
     *
     * @param nickin  nickname dell'utente
     * @param passin  password dell'utente
     * @param name    nome dell'utente
     * @param surname cognome dell'utente
     */
    public static void reg( String nickin, String passin, String name, String surname )
      {
        if( name.equals( "" ) )
          {
            name = null;
          }
        if( surname.equals( "" ) )
          {
            surname = null;
          }


        if( nickin.equals( "" ) || passin.equals( "" ) )
          {
            CGUI.messagepopup( "Empty Field" );
          }
        else
          {
            User me = new User( nickin, passin, name, surname );
            try
              {
                CGUI.messagepopup( Main.stub.RegUser( me ).name( )  );
              }
            catch( RemoteException remoteException )
              {
                remoteException.printStackTrace( );
              }
          }
      }

    /**
     * Metodo per la richiesta di aggiunta di un amico nella propria lista
     *
     * @param name  nickname dell'amico
     * @return ret  risultato dell'operazione
     */
    public static String addFriend( String name )
      {
        String ret = null;
        try
          {
            Main.send( ClientMSG.ADDFRIEND.name( )  );
            Main.send( name );
            ret = Main.recv();
          }
        catch( IOException e )
          {
            e.printStackTrace( );
          }
        return ret;
      }

    /**
     * Metodo che richiede al server la rimozione di un amico dalla propria lista
     *
     * @param name  nickname dell'amico
     * @return ret  risultato dell'operazione
     */
    public static String rmFriend( String name )
      {
        String ret = null;
        try
          {
            Main.send( ClientMSG.RMFRIEND.name( )  );
            Main.send( name );
            ret = Main.recv();
          }
        catch( IOException e )
          {
            e.printStackTrace( );
          }
        return ret;
      }

    /**
     * Richiede al server la lista dei propri amici
     *
     * @return una array con i nickname degli amici
     */
    public static String[] friendlist( )
      {
        try
          {
            Main.send( ClientMSG.GETFRIENDS.name( ) );
          }
        catch( IOException e )
          {
            e.printStackTrace( );
          }
        String r = null;
        try
          {
            r = Main.recv();
          }
        catch( IOException e )
          {
            e.printStackTrace( );
          }
        JSONParser p = new JSONParser();
        JSONArray myfrd = null;
        try
          {
            myfrd = (JSONArray) p.parse( r );
          }
        catch( ParseException e )
          {
            e.printStackTrace( );
          }

        String[] list = new String[ myfrd.size() ];
        for( int i = 0; i < myfrd.size(); i++ )
          {
            list[i] =  (String)myfrd.get( i );
          }

        return list;
      }

    /**
     * Richiede al server la ranking list personale.
     *
     * @return array con i nomi e il punteggio degli amici
     */
    public static String[] getRank( )
      {
        String rec = null;
        try
          {
            Main.send( ClientMSG.GETRANK.name( ) );
            rec = Main.recv( );
          }
        catch( IOException e )
          {
            e.printStackTrace( );
          }

        JSONParser parse = new JSONParser();
        JSONObject rank = null;
        try
          {
            rank = (JSONObject)parse.parse( rec );
          }
        catch( ParseException e )
          {
            e.printStackTrace( );
          }
        JSONObject pr = (JSONObject) rank.get( "ranking" );
        Iterator <String> friend = pr.keySet().iterator();

        String[] msg = new String[ pr.size( ) ];

        int i = 0;
        while( friend.hasNext( )  )
          {
            String name = friend.next();
            long point = (long)pr.get( name );
            msg[i] = name + "  " +  point;
            i++;
          }

        return msg;
      }

    /**
     * Richiede al server di effettuare il logout per l'utente
     *
     * @return rec  il risultato dell'operazione
     */
    public static String logout( )
      {
        String rec = null;
        try
          {
            Main.send( ClientMSG.LOGOUT.name( ) );
            rec = Main.recv( );
          }
        catch( IOException e )
          {
            e.printStackTrace( );
          }

        return rec;
      }

    /**
     * Richiede al server la rimozione dell'utente dal gioco
     *
     * @return rec  risultato dell'operazione
     */
    public static String rmUser( )
      {
        String rec = null;
        try
          {
            Main.send( ClientMSG.RMUSER.name( ) );
            rec = Main.recv( );
          }
        catch( IOException e )
          {
            e.printStackTrace( );
          }

        return rec;
      }

    /**
     * Richiede al server l'aggiornamento del profilo personale.
     * Stampa il risultato dell'operazione su un popup di sistema
     *
     * @param name      nome dell'utente
     * @param surname   cognome dell'utente
     * @param password  password dell'utente
     * @return rec  risultato dell'operazione
     */
    public static String updateProfile( String name, String surname, String password )
      {
        String rec = null;
        try
          {
            Main.send( ClientMSG.UPDATEINFO.name( ) );
            Main.send( name );
            Main.send( surname );
            Main.send( password );
            rec  = Main.recv( );
          }
        catch( IOException e )
          {
            e.printStackTrace( );
          }
        return rec;
      }

/*    public static String sendChRequest( String name )
      {
        String rec = null;
        try
          {
            Main.send( ClientMSG.STARTCH.name( ) );
            Main.send( name );
            rec = Main.recv( );
          }
        catch( IOException e )
          {
            e.printStackTrace( );
          }
        return rec;
      }*/

    /**
     * Richiede al server il punteggio totalizzato fino a quel momento
     *
     * @return point  il numero di punti guadagnati
     */
    public static String getPoints( )
      {
        String point = null;
        try
          {
            Main.send( ClientMSG.GETPOINTS.name() );
            point = Main.recv();
          }
        catch( IOException e )
          {
            e.printStackTrace( );
          }
        return point;
      }
  }
