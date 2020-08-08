package client;

import server.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.rmi.RemoteException;



class MyActionListener
  {
    private static void login( String nickname, String Password )
      {
        try
          {
            Main.send( ClientMSG.LOGIN.name( ) );
            Main.send( nickname );
            Main.send( Password );
            System.out.println( nickname + " " + Password );
            if( Main.recv().equals( ACK.LoggedIn.name() ) )
              {
                //TODO
              }
            else
              {
                //TODO
              }
          }
        catch( IOException e )
          {
            e.printStackTrace( );
          }

      }

    private static void reg( JTextField nickin, JPasswordField passin, JTextField name, JTextField surname )
      {
        String nickname = nickin.getText( );
        String password = new String( passin.getPassword( ) );
        String nameu = name.getText( );
        String sname = surname.getText( );
        if( nameu.equals( "" ) )
          {
            nameu = null;
          }
        if( sname.equals( "" ) )
          {
            sname = null;
          }

        User me = new User( nickname, password, nameu, sname );

        System.out.println( me.getName( ) );

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
        name.setText( "" );
        surname.setText( "" );
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

    public static void setBack2( JButton back2, JPanel signupP, JFrame mainW, JPanel firstP )
      {
        back2.addMouseListener( new MouseAdapter( )
          {
            @Override
            public void mouseClicked( MouseEvent e )
              {
                signupP.setVisible( false );
                mainW.remove( signupP );
                firstP.setVisible( true );
              }
          } );
      }




    public static void setGoReg( JButton goup, JTextField nick, JPasswordField pass, JTextField name, JTextField surname )
      {
        goup.addMouseListener( new MouseAdapter( )
          {
            @Override
            public void mouseClicked( MouseEvent e )
              {
                reg( nick, pass, name, surname );
              }
          } );

        goup.addKeyListener( new KeyAdapter( )
          {
            @Override
            public void keyTyped( KeyEvent e )
              {
                reg( nick, pass, name, surname );
              }
          } );
      }

    public static void setGoLogin( JButton go, JTextField nick, JPasswordField pass )
      {
        go.addMouseListener( new MouseAdapter( )
          {
            @Override
            public void mouseClicked( MouseEvent e )
              {
                String nickname = nick.getText( );
                String password = new String( pass.getPassword( ) );
                login( nickname, password );
              }
          } );

        go.addKeyListener( new KeyAdapter( )
          {
            @Override
            public void keyTyped( KeyEvent e )
              {
                String nickname = nick.getText( );
                String password = new String( pass.getPassword( ) );
                login( nickname, password );
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
