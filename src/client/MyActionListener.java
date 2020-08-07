package client;

import server.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;



class MyActionListener
  {
    private static void reg( JTextField nickin, JPasswordField passin )
      {
        String nickname = nickin.getText( );
        String password = new String( passin.getPassword( ) );
        User me = new User( nickname, password );

        try
          {
            System.out.println( Main.stub.RegUser( me ) );
          }
        catch( RemoteException remoteException )
          {
            remoteException.printStackTrace( );
          }
        nickin.setText( "" );
        passin.setText( "" );
      }

    public static void setBack1( JButton back1, JPanel loginP, JFrame mainW, JPanel firstP )
      {
        back1.addMouseListener( new MouseAdapter( )
          {
            @Override
            public void mouseClicked( MouseEvent e )
              {
                loginP.setVisible( false );
                mainW.remove( loginP );
                firstP.setVisible( true );
              }
          } );
      }


    public static void setGoReg( JButton go, JTextField nick, JPasswordField pass )
      {
        go.addMouseListener( new MouseAdapter( )
          {
            @Override
            public void mouseClicked( MouseEvent e )
              {
                reg( nick, pass );
              }
          } );

        go.addKeyListener( new KeyAdapter( )
          {
            @Override
            public void keyTyped( KeyEvent e )
              {
                reg( nick, pass );
              }
          } );
      }

    public static void setSignin( JButton signin, JFrame mainW, JPanel firstP, JPanel loginP )
      {
        signin.addMouseListener( new MouseAdapter( )
          {
            @Override
            public void mouseClicked( MouseEvent e )
              {
                mainW.add( loginP );
                firstP.setVisible( false );
                loginP.setVisible( true );
              }

            @Override
            public void mouseEntered( MouseEvent e )
              {
                Font f = signin.getFont( );
                f = f.deriveFont( Font.BOLD, 28 );

                signin.setFont( f );

              }

            @Override
            public void mouseExited( MouseEvent e )
              {
                Font f = signin.getFont( );
                f = f.deriveFont( Font.PLAIN, 18 );

                signin.setFont( f );
              }
          } );
      }



    public static void setSignup( JButton signup, JFrame mainW, JPanel firstP, JPanel signupP )
      {
        signup.addMouseListener( new MouseAdapter( )
          {
            @Override
            public void mouseClicked( MouseEvent e )
              {
                mainW.add( signupP );
                firstP.setVisible( false );
                signupP.setVisible( true );
              }

            @Override
            public void mouseEntered( MouseEvent e )
              {
                Font f = signup.getFont( );
                f = f.deriveFont( Font.BOLD, 28 );

                signup.setFont( f );
              }

            @Override
            public void mouseExited( MouseEvent e )
              {
                Font f = signup.getFont( );
                f = f.deriveFont( Font.PLAIN, 18 );

                signup.setFont( f );
              }
          } );
      }
  }
