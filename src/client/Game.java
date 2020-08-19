/************************************************
 *                                              *
 *                     GAME                     *
 *                                              *
 ************************************************
 *
 */
package client;

import javax.swing.Timer;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.IOException;

import java.util.concurrent.atomic.AtomicBoolean;



/**
 * In questa classe vengono implementati i metodi per gestire il gioco quando
 * entrambi i giocatori hanno accettato la sfida. Viene creata una nuova
 * finestra in cui vengono visualizzate le parole da tradurre e dove l'utente
 * può scrivere la traduzione secondo lui corretta. Al termine delle parole
 * o allo scadere del Timeout, viene gestita la chiusura della sfida.
 *
 * @class   Game
 * @author  Luca Canessa (Mat. 516639)
 * @version 1.5
 * @since   1.0
 */
class Game implements Runnable
  {
    //            //
    //  VARIABLES //
    //            //

    private CGUI                  mainUI; ///< riferimento al gestore della grafica
    private static AtomicBoolean  isEnd; ///< flag usato per determinare quando terminare la partita


    //          //
    //  METHODS //
    //          //

    /**
     * Costruttore del gestore della sfida
     *
     * @param ui  riferimento al gestore della grafica
     */
    public Game( CGUI ui )
      {
        isEnd = new AtomicBoolean( false );
        mainUI = ui;
      }

    /**
     * Metodo incaricato di settare la variabile di controllo di terminazione a
     * true, per concludere il match
     */
    public static void end( )
      {
        isEnd.set(true);
      }

    /**
     * Messo in esecuzione da un thread, tale metodo ha il compito di creare
     * una finestra per interfacciare l'utente con il gioco e nascondere la
     * schermata principale. Creata la finestra scrive la parola italiana
     * ricevuta e aspetta che l'utente premi il bottone di invio che permette
     * di recapitare al server la parola che l'utente ha scritto come
     * traduzione di quella visualizzata. Allo scadere del tempo richiesto per
     * la sfida o al termine delle parole da tradurre, il metodo si occupa di
     * aggiornare la grafica nascondendo la finestra di gioco e rivisualizzando
     * la schermata principale, di scrivere un un popup di sistema il nome
     * del vincitore e aggiornare i punti totali dell'utente sulla schermata
     * principale.
     */
    @Override
    public void run( )
      {
        JFrame newF = new JFrame( "Gioca" );
        JPanel pan = new JPanel( );
        JTextField txt = new JTextField( );
        JButton ch = new JButton( "Invia" );
        JLabel ret = new JLabel( );
        JLabel txtLab = new JLabel( );
        JLabel nW = new JLabel(  );
        pan.setBackground( new Color( 0, 0, 26 ) );
        txtLab.setForeground( Color.WHITE );
        ret.setForeground( Color.WHITE );
        nW.setForeground( Color.WHITE );
        txt.setHorizontalAlignment( JTextField.CENTER );
        txtLab.setHorizontalAlignment( JLabel.CENTER );
        nW.setHorizontalAlignment( JLabel.CENTER );

        pan.setLayout( new GridLayout( 5, 1 ) );
        ret.setText( "" );

        pan.add( txtLab );
        pan.add( txt );
        pan.add( ch );
        pan.add( nW );

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
                    Thread.sleep( 100 );
                  }
                catch( IOException | InterruptedException ioException )
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
            String w = null;
            while( !(w = Main.recv() ).equals( "#" ) && !isEnd.get( ) )
              {
                  txtLab.setText( w );
                  nW.setText( ( i+1 ) + "/" + size );
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


        try
          {
            winner = Main.recv();
          }
        catch( IOException e )
          {
            e.printStackTrace( );
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
