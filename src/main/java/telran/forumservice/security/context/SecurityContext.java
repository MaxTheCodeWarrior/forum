package telran.forumservice.security.context;

import telran.forumservice.security.model.User;

public interface SecurityContext {
	User addUserSession(String sessionId, User user);
	User removeUserSession(String sessionId);
	User getUserBySessionId(String sessionId);
	
}
