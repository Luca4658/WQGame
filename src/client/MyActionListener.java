package client;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import server.User;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Timer;



class MyActionListener
  {
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
                surname.setText( "" );              }
          } );
      }

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
                        gui.setPoint( Main.getPoints( ) );

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
                        gui.setPoint( Main.getPoints( ) );

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


    public static void challenge( JButton sendCh, CGUI ui )
      {
        sendCh.addMouseListener( new MouseAdapter( )
          {
            @Override
            public void mouseClicked( MouseEvent e )
              {
                JFrame newF = new JFrame( "Sfida un amico" );
                JPanel pan = new JPanel(  );
                JTextField txt = new JTextField(  );
                JButton ch = new JButton( "Sfida" );
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
                pan.add( ch );
                pan.add( ret );
                newF.add( pan );
                newF.setSize( 400, 300 );
                newF.setLocationRelativeTo( null );
                newF.setVisible( true );

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
                            String rec = Tasks.sendChRequest( name );
                            ret.setText( rec );
                            try
                              {
                                if( rec.equals( ACK.Accepted.name() ) )
                                  {
                                    Thread T = new Thread( new Game( ui ) );
                                    T.start( );
                                  }
                                Thread.sleep( 1000 );
                              }
                            catch( InterruptedException interruptedException )
                              {
                                interruptedException.printStackTrace( );
                              }

                            newF.dispose();
                          }
                        txt.setText( "" );
                      }
                  } );
              }
          } );
      }


  }
