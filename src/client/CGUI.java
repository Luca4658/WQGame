package client;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.IOException;



class CGUI extends JFrame
  {

    private JFrame mainW;
    private JPanel firstP;
    private JButton signin;
    private JButton signup;
    private JPanel loginP;
    private JTextField nickin;
    private JPasswordField passin;
    private JLabel nickupLab;
    private JLabel passLab;
    private JButton go;
    private JButton back1;
    private JLabel nickLab;
    private JPanel signupP;
    private JButton back2;
    private JPasswordField passup;
    private JButton goup;
    private JPanel SignupP;
    private JTextField nickup;
    private JLabel passupLab;
    private JTextField surnameup;
    private JLabel nameupLab;
    private JLabel surnameupLab;
    private JTextField nameup;
    private JPanel tasks;
    private JSplitPane split;
    private JButton addfrd;
    private JLabel friend;
    private JList listf;
    private JLabel num;
    private JButton rmfrd;
    private JButton upfrd;
    private JButton chal;
    private JButton upprof;
    private JLabel totpoint;
    private JButton getrk;
    private JButton rmusr;
    private JButton logoutButton;
    private JPanel updateU;
    private JButton back3;
    private JTextField nameU;
    private JTextField snameU;
    private JPasswordField passU;
    private JButton go3;
    private JLabel nameLU;
    private JLabel snameUL;
    private JLabel passLU;
    private JLabel helloNick;
    private JPanel gameP;
    private JTextField word;
    private JButton sendW;
    private JLabel getW;
    private JLabel counter;

    public CGUI( String title ) throws HeadlessException
      {
        mainW = new JFrame( title );
        mainW.setBackground( Color.BLACK );
        mainW.setSize( 700, 700 );
        mainW.setMaximumSize( new Dimension( 1080, 1080 ) );
        mainW.add( firstP );
        mainW.setDefaultCloseOperation( EXIT_ON_CLOSE );
        mainW.setLocationRelativeTo( null );
        mainW.setVisible( true );

        //         mouse listener
        MyActionListener.setSignin( signin, mainW, firstP, loginP );
        MyActionListener.setSignup( signup, mainW, firstP, signupP );
        MyActionListener.setBack1( back1, loginP, mainW, firstP );
        MyActionListener.setBack2( back2, signupP, mainW, firstP );
        MyActionListener.setGoReg( goup, nickup, passup, nameup, surnameup );
        MyActionListener.setGoLogin( go, nickin, passin, this );
        MyActionListener.setAddFriend( addfrd, this );
        MyActionListener.removeFriend( rmfrd, this );
        MyActionListener.updateFriend( upfrd, this );
        MyActionListener.getRank( getrk, this );
        MyActionListener.logout( logoutButton, this );
        MyActionListener.removeUser( rmusr, this );
        MyActionListener.update( upprof, this );
        MyActionListener.setBack3( back3, tasks, mainW, updateU );
        MyActionListener.goUpdate( go3, nameU, snameU, passU, this );
        MyActionListener.challenge( chal, this );
      }

    public static void winner( )
      {
        try
          {
            messagepopup( Main.recv( ) );
          }
        catch( IOException e )
          {
            e.printStackTrace( );
          }
      }

    public static void messagepopup( String Message )
      {
        JFrame ret = new JFrame( "System Message" );
        JPanel p = new JPanel( );
        JLabel reg = new JLabel( );
        p.add( reg );
        p.setBackground( new Color( 0, 0, 26 ) );
        ret.add( p );
        reg.setForeground( Color.WHITE );
        Font labelFont = reg.getFont( );
        int size = 20;
        reg.setFont( new Font( labelFont.getFontName( ), labelFont.getStyle( ), size ) );
        ret.setLocationRelativeTo( null );
        ret.setSize( 300, 150 );
        reg.setText( Message );
        ret.setVisible( true );
      }


    public void friends( String[] friend, String numF )
      {
        num.setText( "(" + numF + ")" );
        listf.setListData( friend );
      }

    public void setPoint( String point )
      {
        totpoint.setText( point );
      }


    public void viewTask( )
      {
        mainW.add( tasks );
        loginP.setVisible( false );
        tasks.setVisible( true );
      }

    public void closeU( )
      {
        updateU.setVisible( false );
        tasks.setVisible( true );
      }

    public void logout( )
      {
        tasks.setVisible( false );
        firstP.setVisible( true );
      }

    public void updateU( )
      {
        mainW.add( updateU );
        tasks.setVisible( false );
        updateU.setVisible( true );
      }

    public void setnicLab( String Nickname )
      {
        String lab = "Ciao,";
        String nic = Nickname.toUpperCase( );
        lab += " " + nic + "!";

        helloNick.setText( lab );
      }

    public void game( )
      {
        mainW.setVisible( false );
      }

    public void endgame( )
      {
        mainW.setVisible( true );
      }

    public void updateTotScore( )
      {
        try
          {
            Main.send( ClientMSG.GETPOINTS.name( ) );
            totpoint.setText( Main.recv( ) );
          }
        catch( IOException e )
          {
            e.printStackTrace( );
          }
      }

    {
      // GUI initializer generated by IntelliJ IDEA GUI Designer
      // >>> IMPORTANT!! <<<
      // DO NOT EDIT OR ADD ANY CODE HERE!
      $$$setupUI$$$( );
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$( )
      {
        final JPanel panel1 = new JPanel( );
        panel1.setLayout( new GridLayoutManager( 6, 3, new Insets( 0, 0, 0, 0 ), -1, -1 ) );
        Font panel1Font = this.$$$getFont$$$( null, -1, -1, panel1.getFont( ) );
        if( panel1Font != null )
          {
            panel1.setFont( panel1Font );
          }
        panel1.setForeground( new Color( -1 ) );
        panel1.setMaximumSize( new Dimension( 200000, 200000 ) );
        panel1.setMinimumSize( new Dimension( -1, -1 ) );
        panel1.setPreferredSize( new Dimension( -1, -1 ) );
        panel1.putClientProperty( "html.disable", Boolean.FALSE );
        loginP = new JPanel( );
        loginP.setLayout( new GridLayoutManager( 6, 1, new Insets( 0, 0, 0, 0 ), -1, -1 ) );
        loginP.setBackground( new Color( -16777190 ) );
        loginP.setForeground( new Color( -1 ) );
        panel1.add( loginP, new GridConstraints( 1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, new Dimension( 20000, 20000 ), 0, false ) );
        loginP.setBorder( BorderFactory.createTitledBorder( BorderFactory.createEmptyBorder( 50, 10, 50, 10 ), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null ) );
        nickin = new JTextField( );
        Font nickinFont = this.$$$getFont$$$( "Courier New", -1, 16, nickin.getFont( ) );
        if( nickinFont != null )
          {
            nickin.setFont( nickinFont );
          }
        nickin.setForeground( new Color( -16777216 ) );
        nickin.setName( "Nickname" );
        nickin.setToolTipText( "Inser your nickname" );
        loginP.add( nickin, new GridConstraints( 2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension( 150, 50 ), null, 0, false ) );
        passin = new JPasswordField( );
        Font passinFont = this.$$$getFont$$$( "Courier New", -1, 16, passin.getFont( ) );
        if( passinFont != null )
          {
            passin.setFont( passinFont );
          }
        passin.setToolTipText( "Insert your password" );
        loginP.add( passin, new GridConstraints( 4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension( 150, 50 ), null, 0, false ) );
        nickLab = new JLabel( );
        nickLab.setAlignmentY( 0.0f );
        Font nickLabFont = this.$$$getFont$$$( "Courier New", -1, 14, nickLab.getFont( ) );
        if( nickLabFont != null )
          {
            nickLab.setFont( nickLabFont );
          }
        nickLab.setForeground( new Color( -1 ) );
        nickLab.setHorizontalTextPosition( 11 );
        nickLab.setText( "Nickname" );
        loginP.add( nickLab, new GridConstraints( 1, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false ) );
        passLab = new JLabel( );
        Font passLabFont = this.$$$getFont$$$( "Courier New", -1, 14, passLab.getFont( ) );
        if( passLabFont != null )
          {
            passLab.setFont( passLabFont );
          }
        passLab.setForeground( new Color( -1 ) );
        passLab.setHorizontalAlignment( 10 );
        passLab.setHorizontalTextPosition( 11 );
        passLab.setText( "Password" );
        loginP.add( passLab, new GridConstraints( 3, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false ) );
        go = new JButton( );
        go.setBackground( new Color( -6400 ) );
        go.setBorderPainted( true );
        Font goFont = this.$$$getFont$$$( "Courier New", -1, 20, go.getFont( ) );
        if( goFont != null )
          {
            go.setFont( goFont );
          }
        go.setForeground( new Color( -16777216 ) );
        go.setLabel( "Go" );
        go.setRequestFocusEnabled( true );
        go.setRolloverEnabled( false );
        go.setText( "Go" );
        loginP.add( go, new GridConstraints( 5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension( -1, 80 ), null, null, 0, false ) );
        back1 = new JButton( );
        back1.setActionCommand( "Back" );
        back1.setBackground( new Color( -58368 ) );
        back1.setEnabled( true );
        Font back1Font = this.$$$getFont$$$( "Courier New", -1, 14, back1.getFont( ) );
        if( back1Font != null )
          {
            back1.setFont( back1Font );
          }
        back1.setForeground( new Color( -1 ) );
        back1.setLabel( "BACK" );
        back1.setText( "BACK" );
        loginP.add( back1, new GridConstraints( 0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension( 80, 40 ), null, new Dimension( 80, -1 ), 0, false ) );
        firstP = new JPanel( );
        firstP.setLayout( new GridLayoutManager( 2, 1, new Insets( 0, 0, 0, 0 ), -1, -1 ) );
        firstP.setBackground( new Color( -16777190 ) );
        firstP.setMinimumSize( new Dimension( 500, 500 ) );
        panel1.add( firstP, new GridConstraints( 0, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, new Dimension( 200000, 200000 ), 0, false ) );
        signin = new JButton( );
        signin.setAlignmentY( 0.0f );
        signin.setBackground( new Color( -6400 ) );
        Font signinFont = this.$$$getFont$$$( "Courier New", -1, 18, signin.getFont( ) );
        if( signinFont != null )
          {
            signin.setFont( signinFont );
          }
        signin.setForeground( new Color( -16777216 ) );
        signin.setMargin( new Insets( 0, 0, 0, 0 ) );
        signin.setText( "SIGN-IN" );
        firstP.add( signin, new GridConstraints( 0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension( 300, 300 ), null, null, 0, false ) );
        signup = new JButton( );
        signup.setBackground( new Color( -13913600 ) );
        Font signupFont = this.$$$getFont$$$( "Courier New", -1, 18, signup.getFont( ) );
        if( signupFont != null )
          {
            signup.setFont( signupFont );
          }
        signup.setForeground( new Color( -16777216 ) );
        signup.setText( "SIGN-UP" );
        firstP.add( signup, new GridConstraints( 1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension( 300, 300 ), null, null, 0, false ) );
        signupP = new JPanel( );
        signupP.setLayout( new GridLayoutManager( 11, 1, new Insets( 0, 0, 0, 0 ), -1, -1 ) );
        signupP.setBackground( new Color( -16777190 ) );
        panel1.add( signupP, new GridConstraints( 2, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false ) );
        back2 = new JButton( );
        back2.setActionCommand( "Back" );
        back2.setBackground( new Color( -58368 ) );
        Font back2Font = this.$$$getFont$$$( "Courier New", -1, 14, back2.getFont( ) );
        if( back2Font != null )
          {
            back2.setFont( back2Font );
          }
        back2.setForeground( new Color( -1 ) );
        back2.setLabel( "BACK" );
        back2.setText( "BACK" );
        back2.setVisible( true );
        signupP.add( back2, new GridConstraints( 0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension( 80, 40 ), null, new Dimension( 80, -1 ), 0, false ) );
        nickupLab = new JLabel( );
        nickupLab.setAlignmentX( 0.5f );
        nickupLab.setAutoscrolls( false );
        nickupLab.setBackground( new Color( -16316147 ) );
        Font nickupLabFont = this.$$$getFont$$$( "Courier New", -1, 16, nickupLab.getFont( ) );
        if( nickupLabFont != null )
          {
            nickupLab.setFont( nickupLabFont );
          }
        nickupLab.setForeground( new Color( -1 ) );
        nickupLab.setText( "Nickname" );
        signupP.add( nickupLab, new GridConstraints( 1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false ) );
        nickup = new JTextField( );
        signupP.add( nickup, new GridConstraints( 2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension( 150, 50 ), null, 0, false ) );
        passupLab = new JLabel( );
        Font passupLabFont = this.$$$getFont$$$( "Courier New", -1, 14, passupLab.getFont( ) );
        if( passupLabFont != null )
          {
            passupLab.setFont( passupLabFont );
          }
        passupLab.setForeground( new Color( -1 ) );
        passupLab.setRequestFocusEnabled( true );
        passupLab.setText( "Password" );
        signupP.add( passupLab, new GridConstraints( 3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false ) );
        passup = new JPasswordField( );
        Font passupFont = this.$$$getFont$$$( "Courier New", -1, 14, passup.getFont( ) );
        if( passupFont != null )
          {
            passup.setFont( passupFont );
          }
        passup.setForeground( new Color( -16316147 ) );
        signupP.add( passup, new GridConstraints( 4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension( 150, 50 ), null, 0, false ) );
        nameupLab = new JLabel( );
        Font nameupLabFont = this.$$$getFont$$$( "Courier New", -1, 16, nameupLab.getFont( ) );
        if( nameupLabFont != null )
          {
            nameupLab.setFont( nameupLabFont );
          }
        nameupLab.setForeground( new Color( -1 ) );
        nameupLab.setText( "Name" );
        signupP.add( nameupLab, new GridConstraints( 5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false ) );
        surnameupLab = new JLabel( );
        Font surnameupLabFont = this.$$$getFont$$$( "Courier New", -1, 16, surnameupLab.getFont( ) );
        if( surnameupLabFont != null )
          {
            surnameupLab.setFont( surnameupLabFont );
          }
        surnameupLab.setForeground( new Color( -1 ) );
        surnameupLab.setText( "Surname" );
        signupP.add( surnameupLab, new GridConstraints( 7, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false ) );
        surnameup = new JTextField( );
        Font surnameupFont = this.$$$getFont$$$( "Courier New", -1, 14, surnameup.getFont( ) );
        if( surnameupFont != null )
          {
            surnameup.setFont( surnameupFont );
          }
        surnameup.setForeground( new Color( -16316147 ) );
        signupP.add( surnameup, new GridConstraints( 8, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension( 150, 50 ), null, 0, false ) );
        goup = new JButton( );
        goup.setBackground( new Color( -13913600 ) );
        Font goupFont = this.$$$getFont$$$( "Courier New", -1, 14, goup.getFont( ) );
        if( goupFont != null )
          {
            goup.setFont( goupFont );
          }
        goup.setForeground( new Color( -16316147 ) );
        goup.setRolloverEnabled( false );
        goup.setText( "Go" );
        signupP.add( goup, new GridConstraints( 9, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension( 80, 40 ), new Dimension( -1, 80 ), new Dimension( -1, 80 ), 0, false ) );
        nameup = new JTextField( );
        Font nameupFont = this.$$$getFont$$$( "Courier New", -1, 14, nameup.getFont( ) );
        if( nameupFont != null )
          {
            nameup.setFont( nameupFont );
          }
        nameup.setForeground( new Color( -16316147 ) );
        signupP.add( nameup, new GridConstraints( 6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension( 150, 50 ), null, 0, false ) );
        tasks = new JPanel( );
        tasks.setLayout( new GridLayoutManager( 2, 1, new Insets( 0, 0, 0, 0 ), -1, -1 ) );
        tasks.setBackground( new Color( -16777190 ) );
        tasks.setEnabled( false );
        tasks.setForeground( new Color( -1 ) );
        panel1.add( tasks, new GridConstraints( 3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false ) );
        split = new JSplitPane( );
        split.setBackground( new Color( -16777190 ) );
        split.setDividerLocation( 300 );
        split.setDividerSize( 5 );
        split.setEnabled( false );
        split.setLastDividerLocation( 0 );
        split.setOrientation( 1 );
        tasks.add( split, new GridConstraints( 1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false ) );
        final JPanel panel2 = new JPanel( );
        panel2.setLayout( new GridLayoutManager( 9, 2, new Insets( 0, 0, 0, 0 ), -1, -1 ) );
        panel2.setBackground( new Color( -16777195 ) );
        split.setRightComponent( panel2 );
        addfrd = new JButton( );
        addfrd.setBackground( new Color( -16773996 ) );
        addfrd.setBorderPainted( false );
        Font addfrdFont = this.$$$getFont$$$( "Courier New", -1, 16, addfrd.getFont( ) );
        if( addfrdFont != null )
          {
            addfrd.setFont( addfrdFont );
          }
        addfrd.setForeground( new Color( -1 ) );
        addfrd.setText( "Aggiungi Amico" );
        panel2.add( addfrd, new GridConstraints( 1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false ) );
        rmfrd = new JButton( );
        rmfrd.setBackground( new Color( -16773996 ) );
        rmfrd.setBorderPainted( false );
        Font rmfrdFont = this.$$$getFont$$$( "Courier New", -1, 16, rmfrd.getFont( ) );
        if( rmfrdFont != null )
          {
            rmfrd.setFont( rmfrdFont );
          }
        rmfrd.setForeground( new Color( -1 ) );
        rmfrd.setLabel( "Rimuovi Amico" );
        rmfrd.setText( "Rimuovi Amico" );
        panel2.add( rmfrd, new GridConstraints( 2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false ) );
        upfrd = new JButton( );
        upfrd.setBackground( new Color( -16773996 ) );
        upfrd.setBorderPainted( false );
        Font upfrdFont = this.$$$getFont$$$( "Courier New", -1, 16, upfrd.getFont( ) );
        if( upfrdFont != null )
          {
            upfrd.setFont( upfrdFont );
          }
        upfrd.setForeground( new Color( -1 ) );
        upfrd.setText( "Aggiorna Lista Amici" );
        panel2.add( upfrd, new GridConstraints( 3, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false ) );
        chal = new JButton( );
        chal.setBackground( new Color( -16773996 ) );
        chal.setBorderPainted( false );
        Font chalFont = this.$$$getFont$$$( "Courier New", -1, 16, chal.getFont( ) );
        if( chalFont != null )
          {
            chal.setFont( chalFont );
          }
        chal.setForeground( new Color( -1 ) );
        chal.setText( "Sfida un Amico" );
        panel2.add( chal, new GridConstraints( 4, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false ) );
        upprof = new JButton( );
        upprof.setBackground( new Color( -16773996 ) );
        upprof.setBorderPainted( false );
        Font upprofFont = this.$$$getFont$$$( "Courier New", -1, 16, upprof.getFont( ) );
        if( upprofFont != null )
          {
            upprof.setFont( upprofFont );
          }
        upprof.setForeground( new Color( -1 ) );
        upprof.setText( "Aggiorna Profilo" );
        panel2.add( upprof, new GridConstraints( 5, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false ) );
        final JLabel label1 = new JLabel( );
        Font label1Font = this.$$$getFont$$$( "Courier New", -1, 12, label1.getFont( ) );
        if( label1Font != null )
          {
            label1.setFont( label1Font );
          }
        label1.setForeground( new Color( -3618616 ) );
        label1.setText( "Punti Totali" );
        panel2.add( label1, new GridConstraints( 0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false ) );
        totpoint = new JLabel( );
        Font totpointFont = this.$$$getFont$$$( "Courier New", -1, 12, totpoint.getFont( ) );
        if( totpointFont != null )
          {
            totpoint.setFont( totpointFont );
          }
        totpoint.setForeground( new Color( -3618616 ) );
        totpoint.setText( "Label" );
        panel2.add( totpoint, new GridConstraints( 0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false ) );
        getrk = new JButton( );
        getrk.setBackground( new Color( -16773996 ) );
        getrk.setBorderPainted( false );
        Font getrkFont = this.$$$getFont$$$( "Courier New", -1, 16, getrk.getFont( ) );
        if( getrkFont != null )
          {
            getrk.setFont( getrkFont );
          }
        getrk.setForeground( new Color( -1 ) );
        getrk.setText( "Classifica" );
        panel2.add( getrk, new GridConstraints( 6, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false ) );
        rmusr = new JButton( );
        rmusr.setAutoscrolls( false );
        rmusr.setBackground( new Color( -5438976 ) );
        rmusr.setBorderPainted( false );
        Font rmusrFont = this.$$$getFont$$$( "Courier New", -1, 16, rmusr.getFont( ) );
        if( rmusrFont != null )
          {
            rmusr.setFont( rmusrFont );
          }
        rmusr.setForeground( new Color( -1 ) );
        rmusr.setText( "Rimuovi Account" );
        panel2.add( rmusr, new GridConstraints( 7, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false ) );
        logoutButton = new JButton( );
        logoutButton.setAutoscrolls( false );
        logoutButton.setBackground( new Color( -5438976 ) );
        logoutButton.setBorderPainted( false );
        Font logoutButtonFont = this.$$$getFont$$$( "Courier New", -1, 16, logoutButton.getFont( ) );
        if( logoutButtonFont != null )
          {
            logoutButton.setFont( logoutButtonFont );
          }
        logoutButton.setForeground( new Color( -1 ) );
        logoutButton.setHorizontalAlignment( 0 );
        logoutButton.setText( "Logout" );
        panel2.add( logoutButton, new GridConstraints( 8, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false ) );
        final JPanel panel3 = new JPanel( );
        panel3.setLayout( new GridLayoutManager( 2, 2, new Insets( 0, 0, 0, 0 ), -1, -1 ) );
        panel3.setBackground( new Color( -16777190 ) );
        panel3.setForeground( new Color( -16777190 ) );
        panel3.setMinimumSize( new Dimension( 300, -1 ) );
        panel3.setPreferredSize( new Dimension( -1, -1 ) );
        split.setLeftComponent( panel3 );
        friend = new JLabel( );
        Font friendFont = this.$$$getFont$$$( "Courier New", -1, 18, friend.getFont( ) );
        if( friendFont != null )
          {
            friend.setFont( friendFont );
          }
        friend.setForeground( new Color( -1 ) );
        friend.setText( "Amici" );
        panel3.add( friend, new GridConstraints( 0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false ) );
        listf = new JList( );
        listf.setBackground( new Color( -16777190 ) );
        Font listfFont = this.$$$getFont$$$( "Courier New", -1, 20, listf.getFont( ) );
        if( listfFont != null )
          {
            listf.setFont( listfFont );
          }
        listf.setForeground( new Color( -1 ) );
        panel3.add( listf, new GridConstraints( 1, 0, 1, 2, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false ) );
        num = new JLabel( );
        num.setBackground( new Color( -16777195 ) );
        Font numFont = this.$$$getFont$$$( "Courier New", -1, 16, num.getFont( ) );
        if( numFont != null )
          {
            num.setFont( numFont );
          }
        num.setForeground( new Color( -1 ) );
        num.setText( "Label" );
        panel3.add( num, new GridConstraints( 0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false ) );
        helloNick = new JLabel( );
        helloNick.setBackground( new Color( -16777190 ) );
        Font helloNickFont = this.$$$getFont$$$( null, -1, 20, helloNick.getFont( ) );
        if( helloNickFont != null )
          {
            helloNick.setFont( helloNickFont );
          }
        helloNick.setForeground( new Color( -1 ) );
        helloNick.setText( "Label" );
        tasks.add( helloNick, new GridConstraints( 0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false ) );
        updateU = new JPanel( );
        updateU.setLayout( new GridLayoutManager( 8, 1, new Insets( 0, 0, 0, 0 ), -1, -1 ) );
        updateU.setBackground( new Color( -16777190 ) );
        Font updateUFont = this.$$$getFont$$$( null, -1, -1, updateU.getFont( ) );
        if( updateUFont != null )
          {
            updateU.setFont( updateUFont );
          }
        panel1.add( updateU, new GridConstraints( 4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 1, false ) );
        back3 = new JButton( );
        back3.setBackground( new Color( -5438976 ) );
        Font back3Font = this.$$$getFont$$$( "Courier New", -1, 14, back3.getFont( ) );
        if( back3Font != null )
          {
            back3.setFont( back3Font );
          }
        back3.setForeground( new Color( -1 ) );
        back3.setText( "Back" );
        updateU.add( back3, new GridConstraints( 0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension( 80, 40 ), null, new Dimension( 80, -1 ), 0, false ) );
        nameLU = new JLabel( );
        nameLU.setBackground( new Color( -16777190 ) );
        nameLU.setEnabled( true );
        Font nameLUFont = this.$$$getFont$$$( "Courier New", -1, 16, nameLU.getFont( ) );
        if( nameLUFont != null )
          {
            nameLU.setFont( nameLUFont );
          }
        nameLU.setForeground( new Color( -1 ) );
        nameLU.setText( "Nome" );
        updateU.add( nameLU, new GridConstraints( 1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false ) );
        nameU = new JTextField( );
        Font nameUFont = this.$$$getFont$$$( "Courier New", -1, 14, nameU.getFont( ) );
        if( nameUFont != null )
          {
            nameU.setFont( nameUFont );
          }
        nameU.setForeground( new Color( -16777216 ) );
        nameU.setToolTipText( "your name" );
        updateU.add( nameU, new GridConstraints( 2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension( -1, 50 ), null, 1, false ) );
        snameUL = new JLabel( );
        snameUL.setBackground( new Color( -16777190 ) );
        Font snameULFont = this.$$$getFont$$$( "Courier New", -1, 16, snameUL.getFont( ) );
        if( snameULFont != null )
          {
            snameUL.setFont( snameULFont );
          }
        snameUL.setForeground( new Color( -1 ) );
        snameUL.setText( "Cognome" );
        snameUL.setToolTipText( "your surname" );
        updateU.add( snameUL, new GridConstraints( 3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false ) );
        snameU = new JTextField( );
        Font snameUFont = this.$$$getFont$$$( "Courier New", -1, 14, snameU.getFont( ) );
        if( snameUFont != null )
          {
            snameU.setFont( snameUFont );
          }
        snameU.setForeground( new Color( -16777216 ) );
        updateU.add( snameU, new GridConstraints( 4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension( -1, 50 ), null, 0, false ) );
        passLU = new JLabel( );
        passLU.setBackground( new Color( -16777190 ) );
        Font passLUFont = this.$$$getFont$$$( "Courier New", -1, 14, passLU.getFont( ) );
        if( passLUFont != null )
          {
            passLU.setFont( passLUFont );
          }
        passLU.setForeground( new Color( -1 ) );
        passLU.setText( "New Password" );
        updateU.add( passLU, new GridConstraints( 5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false ) );
        passU = new JPasswordField( );
        Font passUFont = this.$$$getFont$$$( "Courier New", -1, 14, passU.getFont( ) );
        if( passUFont != null )
          {
            passU.setFont( passUFont );
          }
        passU.setForeground( new Color( -16777216 ) );
        passU.setToolTipText( "Your Password" );
        updateU.add( passU, new GridConstraints( 6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension( -1, 50 ), null, 0, false ) );
        go3 = new JButton( );
        go3.setBackground( new Color( -16347055 ) );
        Font go3Font = this.$$$getFont$$$( "Courier New", -1, 16, go3.getFont( ) );
        if( go3Font != null )
          {
            go3.setFont( go3Font );
          }
        go3.setForeground( new Color( -16777216 ) );
        go3.setText( "Update" );
        updateU.add( go3, new GridConstraints( 7, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension( -1, 80 ), null, 0, false ) );
        gameP = new JPanel( );
        gameP.setLayout( new GridLayoutManager( 3, 2, new Insets( 0, 0, 0, 0 ), -1, -1 ) );
        gameP.setBackground( new Color( -16777190 ) );
        gameP.setEnabled( true );
        panel1.add( gameP, new GridConstraints( 5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false ) );
        getW = new JLabel( );
        getW.setBackground( new Color( -16777190 ) );
        Font getWFont = this.$$$getFont$$$( "Courier New", -1, 22, getW.getFont( ) );
        if( getWFont != null )
          {
            getW.setFont( getWFont );
          }
        getW.setForeground( new Color( -6143960 ) );
        getW.setText( "" );
        gameP.add( getW, new GridConstraints( 0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false ) );
        word = new JTextField( );
        Font wordFont = this.$$$getFont$$$( "Courier New", -1, 16, word.getFont( ) );
        if( wordFont != null )
          {
            word.setFont( wordFont );
          }
        word.setForeground( new Color( -16777216 ) );
        gameP.add( word, new GridConstraints( 1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false ) );
        sendW = new JButton( );
        sendW.setBackground( new Color( -16732673 ) );
        Font sendWFont = this.$$$getFont$$$( "Courier New", -1, 16, sendW.getFont( ) );
        if( sendWFont != null )
          {
            sendW.setFont( sendWFont );
          }
        sendW.setForeground( new Color( -16777216 ) );
        sendW.setText( "INVIA" );
        gameP.add( sendW, new GridConstraints( 2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false ) );
        counter = new JLabel( );
        counter.setBackground( new Color( -16777195 ) );
        Font counterFont = this.$$$getFont$$$( "Courier New", -1, 10, counter.getFont( ) );
        if( counterFont != null )
          {
            counter.setFont( counterFont );
          }
        counter.setForeground( new Color( -1 ) );
        counter.setText( "Label" );
        gameP.add( counter, new GridConstraints( 0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension( 50, -1 ), null, 0, false ) );
        nickLab.setLabelFor( nickin );
        nickupLab.setLabelFor( nickup );
        passupLab.setLabelFor( passup );
      }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$( String fontName, int style, int size, Font currentFont )
      {
        if( currentFont == null )
          {
            return null;
          }
        String resultName;
        if( fontName == null )
          {
            resultName = currentFont.getName( );
          }
        else
          {
            Font testFont = new Font( fontName, Font.PLAIN, 10 );
            if( testFont.canDisplay( 'a' ) && testFont.canDisplay( '1' ) )
              {
                resultName = fontName;
              }
            else
              {
                resultName = currentFont.getName( );
              }
          }
        return new Font( resultName, style >= 0 ? style : currentFont.getStyle( ), size >= 0 ? size : currentFont.getSize( ) );
      }

  }