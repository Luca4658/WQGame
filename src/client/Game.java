package client;

import javax.swing.Timer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;


class Game implements Runnable
  {
    private CGUI mainUI;
    private static AtomicBoolean isEnd;

    public Game( CGUI ui )
      {
        isEnd = new AtomicBoolean( false );
        mainUI = ui;
      }

    public static void end( )
      {
        isEnd.set(true);
      }

    @Override
    public void run( )
      {
        JFrame newF = new JFrame( "Gioca" );
        JPanel pan = new JPanel( );
        JTextField txt = new JTextField( );
        JButton ch = new JButton( "Invia" );
        JLabel ret = new JLabel( );
        JLabel txtLab = new JLabel( );
        pan.setBackground( new Color( 0, 0, 26 ) );
        txtLab.setForeground( Color.WHITE );
        ret.setForeground( Color.WHITE );
        txt.setHorizontalAlignment( JTextField.CENTER );
        txtLab.setHorizontalAlignment( JLabel.CENTER );

        pan.setLayout( new GridLayout( 5, 1 ) );
        ret.setText( "" );

        pan.add( txtLab );
        pan.add( txt );
        pan.add( ch );
        newF.add( pan );
        newF.setSize( 400, 300 );
        newF.setLocationRelativeTo( null );
        newF.setVisible( true );
        mainUI.game( );
        newF.addWindowListener(new java.awt.event.WindowAdapter()
          {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent)
              {
                if( JOptionPane.showConfirmDialog( newF, "Are you sure you want to close this window?", "Close Window?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION )
                  {
                    newF.dispose();
                    mainUI.endgame();
                    try
                      {
                        Main.send( "**end**" );
                      }
                    catch( IOException e )
                      {
                        e.printStackTrace( );
                      }
                  }
            }
          });
        ch.addMouseListener( new MouseAdapter( )
          {
            @Override
            public void mousePressed( MouseEvent e )
              {
                try
                  {
                    Main.send( txt.getText( ) );
                    txt.setText( "" );
                  }
                catch( IOException ioException )
                  {
                    ioException.printStackTrace( );
                  }
              }
          } );

        String size = null;
        String winner = null;

        try
          {
            size = Main.recv( );
            int s = Integer.parseInt( size );

            Timer t = new Timer((int) Main.parser.getTimeoutGame()+500, (e)->{
                isEnd.set( true );
                try
                  {
                    txt.setText( "" );
                    ch.doClick( 100 );
                    Main.send( "**end**" );
                  }
                catch( IOException Exception )
                  {
                    Exception.printStackTrace( );
                  }
               });
            t.start();
            int i = 0;
            while( i < s && !isEnd.get() )
              {
                String w = Main.recv( );
                winner = w;
                txtLab.setText( w );
                i++;
              }
            ActionListener[] lists = t.getActionListeners();
            for( int l = 0; l < lists.length; l++ )
              {
                t.removeActionListener( lists[l] );
              }
            t.stop();
          }
        catch( IOException e )
          {
            e.printStackTrace( );
          }
        newF.dispose();
        mainUI.endgame( );

        if( !isEnd.get() )
          {
            try
              {
                winner = Main.recv();
              }
            catch( IOException e )
              {
                e.printStackTrace( );
              }
          }

        if( winner.equals( "TIE" ) )
          {
            CGUI.messagepopup( "Partita in pareggio" );
          }
        else
          {
            CGUI.messagepopup( "Il vincitore è " + winner );
          }

        mainUI.updateTotScore();
      }
  }
