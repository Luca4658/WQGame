package client;

import server.User;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

import java.rmi.RemoteException;

import java.util.Iterator;



class Tasks
  {
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

    public static String sendChRequest( String name )
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
      }
  }
