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



class comUDP implements Runnable
  {
    private int myport;
    private CGUI mainUI;
    private DatagramPacket packToSend = null;
    private DatagramPacket packToRecv = null;
    private DatagramSocket sock = null;



    public comUDP( int port, CGUI ui )
      {
        myport = port;
        mainUI = ui;
      }

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

                JFrame newF = new JFrame( "Richiesta Sfida" );
                JPanel pan = new JPanel(  );
                JButton ok = new JButton( "Accetta" );
                JButton no = new JButton( "Rifiuta" );
                JLabel ret = new JLabel(  );
                JLabel txtLab = new JLabel(  );
                pan.setBackground( new Color( 0, 0, 26) );
                txtLab.setForeground( Color.WHITE );
                ret.setForeground( Color.WHITE );

                pan.setLayout( new GridLayout( 5, 1 ) );
                ret.setText( "" );

                txtLab.setText( friend );
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
