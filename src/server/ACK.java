package server;


public enum ACK
	{
		//	USER	//
		PasswordUnmatch,
		UserAlreadyLoggedIn,
		LoggedIn,
		LoggedOut,
		//	USERS	//
		AlreadyRegistered,
		EmptyField,
		UserRegistered,
		RegistrationError,
		UserFound,
		UserNotFound,
		UserDeleted,
		UserNotDeleted,
		
		//	FRIENDS	//
		AlreadyFriends,
		FriendNotFound,
		FriendAdded,
		UserAdded,
		AlreadyExisting,
		FriendRemoved,
		
		OperationUnknown,
		OK,
		ERROR;
	}
