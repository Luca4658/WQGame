package server;

import org.json.simple.JSONArray;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;



class Challenge implements Runnable
  {
    private BufferedReader      __inBuff = null;
    private DataOutputStream    __outBuff = null;
    private ArrayList<String>   __italian = null;
    private ArrayList<String>   __english = null;
    private User                __user = null;
    private Users               __udb = null;
    private Thread              __usrT = null;
    private int[]               __gameSettings = null; //0: correct word points
                                                      //1: wrong word points

    public Challenge( BufferedReader inputb, DataOutputStream outputb, ArrayList<String> itWords, ArrayList<String> enWords, User usr, Users udb, Thread T, int[] settings )
      {
        __inBuff = inputb;
        __outBuff = outputb;
        __italian = itWords;
        __english = enWords;
        __user = usr;
        __usrT = T;
        __gameSettings = settings;
      }


    private String recv( )
      {
        try
          {
            return __inBuff.readLine( );
          }
        catch( IOException e )
          {
            System.err.println( "Problem to recv MSG" );
          }

        return null;
      }

    private void send( String str )
      {
        try
          {
            __outBuff.writeBytes( str + "\n" );
          }
        catch( IOException e )
          {
            System.err.println( "Problem to send MSG" );
          }
      }

    @Override
    public void run( )
      {
        String wordRec = null;
        JSONArray storeWord = null;
        boolean isFinished = false;
        int score = 0;

        storeWord = new JSONArray( );
        for( int iWord = 0; ( iWord < __italian.size( ) ) && !isFinished; iWord++ )
          {
            send( __italian.get( iWord ) );
            wordRec = recv( );

            if( wordRec.equals( "**end**" ) )
              {
                isFinished = true;
                continue;
              }
            else
              {
                if( wordRec.equals(  __english.get( iWord ) ) )
                  {
                    score += __gameSettings[0];
                  }
                else
                  {
                    score -= __gameSettings[1];
                  }
              }
          }

        __user.setCScore( score );
        __udb.updateUser( __user );

        if( !isFinished )
          {
            __usrT.interrupt( );
          }
      }
  }
