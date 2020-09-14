package felix.api.controller;

import felix.api.models.User;
import java.util.HashMap;
import java.util.Map;

abstract class WebSocket
{
    static Map<String, User> sessions = new HashMap<>();

    static void addSession(User user)
    {
        sessions.put(user.getDisplayName(), user);
    }

    static void removeSession(User user)
    {
        sessions.remove(user.getDisplayName());
    }
}