package server;

public class Main
	{
		public static void main( String[] args )
			{
				Users DB = Users.init( );
				
				
				User a = new User( "Luca1", "Password1" );
				User b = new User( "Luca2", "Password2", "LucaC", null );
				
				DB.insertUser( a );
				DB.insertUser( b );
				
				b.resetPassword( "Passwd2" );
				a.resetName( "LucaD" );
				
				DB.updateUser( a );
				DB.updateUser( b );
				
				DB.writeONfile( );
				
				User l = DB.getUser( "Luca1" );
				User k = DB.getUser( "Luca2" );
				
				System.out.println( l.getName( ) + "\n" + k.getPassword( ) );
			}
	}
