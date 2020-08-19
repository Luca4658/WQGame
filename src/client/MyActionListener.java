/************************************************
 *                                              *
 *                MYACTIONLISTENER              *
 *                                              *
 ************************************************
 *
 */
package client;

import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.IOException;



/**
 * Classe con metodi statici utile per gestire gli input che l'utente invia
 * attraverso la GUI
 *
 * @class   MyActionListener
 * @author  Luca Canessa (Mat. 516639)
 * @version 1.3
 * @since   1.0
 */
class MyActionListener
  {

    /**
     * Metodo che effettua il cambio di scenario da pannello di Login a
     * pannello con il menù principale
     *
     * @param back1   bottone che riceve l'input
     * @param loginP  pannello di login
     * @param mainW   finestra principale su cui cambiare pannelli
     * @param firstP  pannello con menù iniziale
     */
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

    /**
     * Metodo che effettua il cambio di scenario da pannello di registrazione
     * a pannello con il menù principale
     *
     * @param back2   bottone che riceve input
     * @param signupP pannello di registrazione dell'utente
     * @param mainW   finestra principale
     * @param firstP  pannello con menù principale
     */
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

    /**
     * Metodo predisosto alla registrazione dell'utente
     *
     * @param goup    bottone che riceve l'input da gestire
     * @param nick    nickname dell'utente
     * @param pass    password dell'utente
     * @param name    nome dell'utente
     * @param surname cognome dell'utente
     */
    public static void setGoReg( JButton goup, JTextField nick, JPasswordField pass, JTextField name, JTextField surname )
      {
        goup.addMouseListener( new MouseAdapter( )
          {
            @Override
            public void mouseClicked( MouseEvent e )
              {
                String nname = nick.getText();
                String n = nick.getText();
                String sname = nick.getText();
                String passwd = new String( pass.getPassword( ) );
                Tasks.reg( nname, passwd, n, sname );
                nick.setText( "" );
                pass.setText( "" );
                name.setText( "" );
                surname.setText( "" );
              }
          } );

        goup.addKeyListener( new KeyAdapter( )
          {
            @Override
            public void keyTyped( KeyEvent e )
              {
                String nname = nick.getText();
                String n = nick.getText();
                String sname = nick.getText();
                String passwd = new String( pass.getPassword( ) );
                Tasks.reg( nname, passwd, n, sname );
                nick.setText( "" );
                pass.setText( "" );
                name.setText( "" );
                surname.setText( "" );
              }
          } );
      }

    /**
     * Metodo per effettuare il login dell'utente e visualizzare i tasks
     * possibili e richiedibili al server
     *
     * @param go    bottone che riceve l'input da gestire
     * @param nick  nickname dell'utente
     * @param pass  password dell'utente
     * @param gui   riferimento al gestore della GUI
     */
    public static void setGoLogin( JButton go, JTextField nick, JPasswordField pass, CGUI gui )
      {
        go.addMouseListener( new MouseAdapter( )
          {
            @Override
            public void mouseClicked( MouseEvent e )
              {
                String nickname = nick.getText( );
                String password = new String( pass.getPassword( ) );
                if( nickname.equals( "" ) || password.equals( "" ) )
                  {
                    CGUI.messagepopup( "Empty Field" );
                  }
                else
                  {
                    String rec = Tasks.login( nickname, password );
                    if( rec.equals( ACK.LoggedIn.name() ) )
                      {
                        gui.viewTask( );
                        String[] list = Tasks.friendlist();
                        gui.friends( list, String.valueOf( list.length ) );

                        gui.setnicLab( nickname );
                        gui.setPoint( Tasks.getPoints( ) );

                        Thread udp = new Thread( new comUDP( Main.getMyPort( nickname ), gui ) );
                        udp.start();
                      }
                    else
                      {
                        CGUI.messagepopup( rec );
                      }
                  }

                nick.setText( "" );
                pass.setText( "" );
              }
          } );

        go.addKeyListener( new KeyAdapter( )
          {
            @Override
            public void keyTyped( KeyEvent e )
              {
                String nickname = nick.getText( );
                String password = new String( pass.getPassword( ) );
                if( nickname.equals( "" ) || password.equals( "" ) )
                  {
                    CGUI.messagepopup( "Empty Field" );
                  }
                else
                  {
                    String rec = Tasks.login( nickname, password );
                    if( rec.equals( ACK.LoggedIn.name() ) )
                      {
                        gui.viewTask( );
                        String[] list = Tasks.friendlist();
                        gui.friends( list, String.valueOf( list.length ) );
                        gui.setnicLab( nickname );
                        gui.setPoint( Tasks.getPoints( ) );

                        Thread udp = new Thread( new comUDP( Main.getMyPort( nickname ), gui ) );
                        udp.start();
                      }
                    else
                      {
                        CGUI.messagepopup( rec );
                      }
                  }

                nick.setText( "" );
                pass.setText( "" );
              }
          } );
      }

    /**
     * Metodo per effettuare il cambio scenario da pannello principale a
     * pannello per effettuare il login
     *
     * @param signin  bottone che riceve l'input da gestire
     * @param mainW   finestra principale su gestire gli scenari
     * @param firstP  pannello del menù principale
     * @param loginP  pannello per eseguire il login
     */
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

    /**
     * Metodo per effettuare il cambio scenario da pannello principale a
     * pannello per effettuare la registrazione utente
     *
     * @param signup  bottone che riceve l'input
     * @param mainW   finestra principale su cui eseguire lo scenario
     * @param firstP  pannello del menù principale
     * @param signupP pannello per effettuare la registrazione
     */
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

    /**
     * Metodo per gestire la richiesta di aggiunta di un amico alla lista
     * Per l'aggiunta dell'amico viene creata una nuova finestra in cui si può
     * inserire il Nickname desiderato e inviarlo al server e visualizzare la
     * risposta all'operazione richiesta.
     *
     * @param addfrd  bottone per richiedere l'operazione di aggiunta dell'amico
     * @param ui      riferimento al gestore della GUI
     */
    public static void setAddFriend( JButton addfrd, CGUI ui )
      {
        addfrd.addMouseListener( new MouseAdapter( )
          {
            @Override
            public void mouseClicked( MouseEvent e )
              {
                JFrame newF = new JFrame( "Aggiungi Amico" );
                JPanel pan = new JPanel(  );
                JTextField txt = new JTextField(  );
                JButton add = new JButton( "Aggiungi" );
                JLabel ret = new JLabel(  );
                JLabel txtLab = new JLabel(  );
                pan.setBackground( new Color( 0, 0, 26) );
                txtLab.setForeground( Color.WHITE );
                ret.setForeground( Color.WHITE );

                pan.setLayout( new GridLayout( 5, 1 ) );
                ret.setText( "" );

                txtLab.setText( "Nickname User" );
                pan.add( txtLab );
                pan.add( txt );
                pan.add( add );
                pan.add( ret );
                newF.add( pan );

                newF.setSize( 400, 300 );
                newF.setLocationRelativeTo( null );
                newF.setVisible( true );

                add.addMouseListener( new MouseAdapter( )
                  {
                    @Override
                    public void mouseClicked( MouseEvent e )
                      {
                        String name = txt.getText( );
                        if( name.equals( "" ) )
                          {
                            ret.setText( "Nickname non valido" );
                          }
                        else
                          {
                            String retu = Tasks.addFriend( name );
                            ret.setText( retu );

                            String[] list = Tasks.friendlist( );
                            ui.friends( list, String.valueOf( list.length ) );
                          }
                        txt.setText( "" );
                      }
                  } );
              }
          } );
      }

    /**
     * Metodo per gestire la richiesta di rimozione di un amico dalla lista
     * Per la rimozione dell'amico viene creata una nuova finestra in cui si
     * può inserire il Nickname desiderato e inviarlo al server e visualizzare
     * la risposta all'operazione richiesta.
     *
     * @param rm  bottone che riceve l'input per la richiesta della rimozione dell'amico
     * @param ui  riferimento al gestore della GUI
     */
    public static void removeFriend( JButton rm, CGUI ui )
      {
        rm.addMouseListener( new MouseAdapter( )
          {
            @Override
            public void mouseClicked( MouseEvent e )
              {
                JFrame newF = new JFrame( "Rimuovi Amico" );
                JPanel pan = new JPanel(  );
                JTextField txt = new JTextField(  );
                JButton add = new JButton( "Rimuovi" );
                JLabel ret = new JLabel(  );
                JLabel txtLab = new JLabel(  );
                pan.setBackground( new Color( 0, 0, 26) );
                txtLab.setForeground( Color.WHITE );
                ret.setForeground( Color.WHITE );

                pan.setLayout( new GridLayout( 5, 1 ) );
                ret.setText( "" );

                txtLab.setText( "Nickname User" );
                pan.add( txtLab );
                pan.add( txt );
                pan.add( add );
                pan.add( ret );
                newF.add( pan );
                newF.setSize( 400, 300 );
                newF.setLocationRelativeTo( null );
                newF.setVisible( true );

                add.addMouseListener( new MouseAdapter( )
                  {
                    @Override
                    public void mouseClicked( MouseEvent e )
                      {
                        String name = txt.getText( );
                        if( name.equals( "" ) )
                          {
                            ret.setText( "Nickname non valido" );
                          }
                        else
                          {
                            String ret = Tasks.rmFriend( name );
                            String[] list = Tasks.friendlist( );
                            ui.friends( list, String.valueOf( list.length ) );
                          }
                        txt.setText( "" );
                      }
                  } );
              }
          } );
      }

    /**
     * Metodo che richiede la lista delle amicizie al server quando l'utente
     * preme il bottone e aggiorna la lista visualizzata.
     *
     * @param update  bottone per richiedere l'aggiornamento lista
     * @param ui      riferimento al gestore della GUI
     */
    public static void updateFriend( JButton update, CGUI ui )
      {
        update.addMouseListener( new MouseAdapter( )
          {
            @Override
            public void mouseClicked( MouseEvent e )
              {
                String[] list = Tasks.friendlist( );
                ui.friends( list, String.valueOf( list.length ) );
              }
          } );
      }

    /**
     * Metodo che richiede la classifica dell'utente al server e la visualizza
     * nella GUI
     *
     * @param rank  bottone per richiedere la visualizzazione della classifica
     * @param ui    riferimento al gestore della GUI
     */
    public static void getRank( JButton rank, CGUI ui )
      {
        rank.addMouseListener( new MouseAdapter( )
          {
            @Override
            public void mouseClicked( MouseEvent e )
              {
                String[] ranking = Tasks.getRank( );
                ui.friends( ranking, String.valueOf( ranking.length ) );
              }
          } );
      }

    /**
     * Metodo che effettua il logout quando riceve risposta positiva dal server
     * cambiando contesto.
     *
     * @param logout  bottone per richiedere il logout dell'utente
     * @param ui      riferimento al gestore GUI
     */
    public static void logout( JButton logout, CGUI ui )
      {
        logout.addMouseListener( new MouseAdapter( )
          {
            @Override
            public void mouseClicked( MouseEvent e )
              {
                String rec = Tasks.logout();
                if( rec.equals( ACK.LoggedOut.name() ) )
                  {
                    ui.logout();
                  }

                CGUI.messagepopup( rec );
              }
          } );
      }

    /**
     * Metodo che si preoccupa di gestire la richiesta di rimozione dell'utente
     * dal gioco
     *
     * @param remove  bottone per richiedere la cancellazione dell'utente
     * @param ui      riferimento al gestore della GUI
     */
    public static void removeUser( JButton remove, CGUI ui )
      {
        remove.addMouseListener( new MouseAdapter( )
          {
            @Override
            public void mouseClicked( MouseEvent e )
              {
                String rec = Tasks.rmUser( );
                if( rec.equals( ACK.UserDeleted.name() ) )
                  {
                    ui.logout();
                  }
                CGUI.messagepopup( rec );
              }
          } );
      }

    /**
     * Metodo incaricato di gestire la richiesta di aggiornamento del profilo
     * dell'utente
     *
     * @param up  bottone per richiedere l'aggiornamento del profilo
     * @param ui  riferimento al gestore della GUI
     */
    public static void update( JButton up, CGUI ui )
      {
        up.addMouseListener( new MouseAdapter( )
          {
            @Override
            public void mouseClicked( MouseEvent e )
              {
                ui.updateU( );
              }
          } );
      }

    /**
     * Metodo che effettua il cambio di scenario da pannello di aggiornamento
     * del profilo a pannello con i tasks possibili e richiedibili
     *
     * @param back3   bottone per richiedere il cambio di contesto
     * @param tasks   pannello con i tasks possibili
     * @param mainW   finestra su cui effettuare il cambio contesto
     * @param updateU pannello per l'aggiornamento profilo
     */
    public static void setBack3( JButton back3, JPanel tasks, JFrame mainW, JPanel updateU )
      {
        back3.addMouseListener( new MouseAdapter( )
          {
            @Override
            public void mouseClicked( MouseEvent e )
              {
                mainW.add( updateU );
                tasks.setVisible( true );
                updateU.setVisible( false );
              }
          } );
      }

    /**
     * Metodo per la gestione dell'aggiornamento del profilo, invia al server i
     * dati da aggiornare e visualizza un popup di sistema con il messaggio di
     * risposta del server
     *
     * @param go      bottone per richiedere l'invio dei dati al server
     * @param name    campo dove prelevare il nome dell'utente
     * @param surname campo dove prelevare il cognome dell'utente
     * @param pass    campodove prelevare la password dell'utente
     * @param ui      riferimento al gestore della GUI
     */
    public static void goUpdate( JButton go, JTextField name, JTextField surname, JPasswordField pass, CGUI ui )
      {
        go.addMouseListener( new MouseAdapter( )
          {
            @Override
            public void mouseClicked( MouseEvent e )
              {
                String nm = name.getText( );
                String snm = surname.getText();
                String pswd = new String( pass.getPassword() );
                String rec = Tasks.updateProfile( nm, snm, pswd );
                CGUI.messagepopup( rec );
                ui.closeU();

                name.setText( "" );
                surname.setText( "" );
                pass.setText( "" );
              }
          } );
      }

    /**
     * Metodo predisposto per gestire l'invio della richiesta di sfida e
     * rispettiva risposta. Crea una finestra in cui indicare l'amico da
     * sfidare e poter richiedere la sfida al server, e nel caso di risposta
     * positiva dall'amico fa partire il thread per la gestiore del gioco e
     * chiude la finestra appea utilizzata
     *
     * @param sendCh  bottone per richiedere l'operazione di invio richiesta sfida
     * @param ui      riferimento al gestore della GUI
     */
    public static void challenge( JButton sendCh, CGUI ui )
      {
        final boolean[] clicked = { false };
        sendCh.addMouseListener( new MouseAdapter( )
          {
            @Override
            public void mouseClicked( MouseEvent e )
              {
                JFrame newF = new JFrame( "Sfida un amico" );
                JPanel pan = new JPanel( );
                JTextField txt = new JTextField( );
                JButton ch = new JButton( "Sfida" );
                JLabel ret = new JLabel( );
                JLabel txtLab = new JLabel( );
                pan.setBackground( new Color( 0, 0, 26 ) );
                txtLab.setForeground( Color.WHITE );
                ret.setForeground( Color.WHITE );

                pan.setLayout( new GridLayout( 5, 1 ) );
                ret.setText( "" );

                txtLab.setText( "Nickname User" );
                pan.add( txtLab );
                pan.add( txt );
                pan.add( ch );
                pan.add( ret );
                newF.add( pan );

                ch.addMouseListener( new MouseAdapter( )
                  {
                    @Override
                    public void mouseClicked( MouseEvent e )
                      {
                        String name = txt.getText( );
                        if( name.equals( "" ) )
                          {
                            ret.setText( "Nickname non valido" );
                          }
                        else
                          {
                            try
                              {
                                clicked[0] = true;
                                Main.send( ClientMSG.STARTCH.name() );
                                Main.send( name );
                                String rec = Main.recv();
                                ret.setText( rec );
                                if( rec.equals( ACK.Accepted.name( ) ) )
                                  {
                                    Thread T = new Thread( new Game( ui ) );
                                    T.start( );
                                    newF.dispose();
                                  }
                              }
                            catch( IOException Exception )
                              {
                                Exception.printStackTrace( );
                              }
                          }
                        txt.setText( "" );
                      }
                  } );

                newF.setSize( 400, 300 );
                newF.setLocationRelativeTo( null );
                newF.setVisible( true );
              }
          } );
      }
  }