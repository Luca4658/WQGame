/************************************************
 *                                              *
 *                    COMUDP                    *
 *                                              *
 ************************************************
 *
 */
package client;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.IOException;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import java.nio.charset.StandardCharsets;



/**
 * In questa classe vengono implementati i metodi necessari per la comunicazione
 * su protocollo UDP tra server e client per la ricezione di richieste di sfida
 * e l'invio della risposta. Tale classe deve essere eseguita da un thread.
 *
 * @class   comUDP
 * @author  Luca Canessa (Mat. 516639)
 * @version 1.4
 * @since   1.0
 */
class comUDP implements Runnable
  {
    //            //
    //  VARIABLES //
    //            //
    private int             myport; ///< numero di porta su cui il client riceve il paccetto
    private CGUI            mainUI; ///< riferimento al gestore della grafica
    private DatagramPacket  packToSend = null; ///< pacchetto per l'invio di messaggi
    private DatagramPacket  packToRecv = null; ///< pacchetto per la ricezione di messaggi
    private DatagramSocket  sock = null; ///< socket UDP per la comunicazione


    //          //
    //  METHODS //
    //          //

    /**
     * Costruttore del gestore della comunicazione su protocollo UDP
     *
     * @param port  porta di comunicazione del client
     * @param ui    riferimento al gestore della grafica
     */
    public comUDP( int port, CGUI ui )
      {
        myport = port;
        mainUI = ui;
      }

    /**
     * Metodo che si occupa dell'invio del messaggio 'msg' all'indirizzo
     * 'address' sulla porta 'port' del server
     *
     * @param msg     messaggio da inviare al server
     * @param address indirizzo IP del server
     * @param port    numero di porta su cui comunica il server
     */
    private void send( String msg, InetAddress address, int port )
      {
        byte[] buff = msg.getBytes();
        packToSend = new DatagramPacket( buff, buff.length, address, port );
        try
          {
            sock.send( packToSend );
          }
        catch( IOException e )
          {
            e.printStackTrace( );
          }
      }

    /**
     * Metodo messo in esecuzione dal thread che si occupa di gestire il
     * pacchetto in ingresso, tradurre il messaggio all'intero, interfaccaiarsi
     * con l'utente e inviare un messaggio di risposta al server. Si aspetta che
     * il messaggio in ingresso sia il 'Nickname' del friend che vuole una sfida.
     * Quando il Nickname è disponibile, crea una finestra per comunicare con
     * l'utente e invia reinvia al server la risposta alla sfida che l'utente
     * gli ha dato. Poi chiude la finestra creata e si rimetti in ascolto sulla
     * socket.
     * Allo scadere di un tempo la riposta inviata di default è il
     * rifiuto della sfida.
     */
    @Override
    public void run( )
      {
        try
          {
            sock = new DatagramSocket( myport );
          }
        catch( SocketException e )
          {
            e.printStackTrace( );
          }

        while( true )
          {
            byte[] buff = new byte[1024];
            packToRecv = new DatagramPacket( buff, buff.length );
            int port = -1;
            InetAddress address = null;
            try
              {
                sock.receive( packToRecv );
                address = packToRecv.getAddress();
                port = packToRecv.getPort();
                String friend = new String( packToRecv.getData(), 0, packToRecv.getLength(), StandardCharsets.US_ASCII );

                // setting della finestra //
                JFrame newF = new JFrame( "Richiesta Sfida" ); // frame per il messaggio in arrivo
                JPanel pan = new JPanel(  );
                JButton ok = new JButton( "Accetti" ); // bottone di accettazione
                JButton no = new JButton( "Rifiuti" ); // bottone di rifiuto
                JLabel ret = new JLabel(  );
                JLabel txtLab = new JLabel(  );
                pan.setBackground( new Color( 0, 0, 26) );
                txtLab.setForeground( Color.WHITE );
                ret.setForeground( Color.WHITE );
                pan.setLayout( new GridLayout( 5, 1 ) );
                ret.setText( "" );
                txtLab.setText( "Il tuo amico " + friend + " ti vuole sfidare" );
                txtLab.setHorizontalAlignment( JLabel.CENTER );
                pan.add( txtLab );
                pan.add( ok );
                pan.add( no );
                pan.add( ret );
                newF.add( pan );

                InetAddress finalAddress = address;
                int finalPort = port;

                final Timer timer = new Timer( (int) Main.parser.getTimeoutReply(), e->{
                  send( ACK.Rejected.name(), finalAddress, finalPort );
                  newF.dispose();
                  ((Timer)e.getSource()).stop();
                } );

                ok.addMouseListener( new MouseAdapter( )
                  {
                    @Override
                    public void mouseClicked( MouseEvent e )
                      {
                        send( ACK.Accepted.name(), finalAddress, finalPort );
                        newF.dispose();
                        try
                          {
                            Thread.sleep( 500 );
                          }
                        catch( InterruptedException interruptedException )
                          {
                            interruptedException.printStackTrace( );
                          }

                        Thread t = new Thread( new Game( mainUI ) );
                        try
                          {
                            Main.send( ClientMSG.ACCEPTEDCH.name( ) );
                            Main.send( friend );
                            timer.stop();
                            Thread.sleep( 1000 );
                          }
                        catch( InterruptedException | IOException interruptedException )
                          {
                            interruptedException.printStackTrace( );
                          }
                        t.start();
                      }
                  } );

                no.addMouseListener( new MouseAdapter( )
                  {
                    @Override
                    public void mouseClicked( MouseEvent e )
                      {
                        send( ACK.Rejected.name(), finalAddress, finalPort );
                        newF.dispose();
                        timer.stop();
                      }
                  } );

                newF.setSize( 400, 300 );
                newF.setLocationRelativeTo( null );
                newF.setVisible( true );

                timer.start();
              }
            catch( IOException e )
              {
                e.printStackTrace( );
              }
          }
      }
  }
