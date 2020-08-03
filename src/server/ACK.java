package server;


public enum ACK
	{
		//	USER	//
		PasswordUnmatch,
		UserAlreadyLoggedIn,
		LoggedIn,
		LoggedOut,
		ONLINE,
		OFFLINE,
		INCHALLENGE,

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

		//  CHALLENGE //
		Accepted,
		Rejected,


		OperationUnknown,
		OK,
		ERROR;
	}
