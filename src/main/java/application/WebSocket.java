package application;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocket/{group}")
public class WebSocket {

    //    @OnMessage
    //    public String echo(String message) {
    //        return message;
    //    }

    private static Map<String, Set<Session>> sessionMap = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("group") String group) {

        Set<Session> sessionSet = sessionMap.get(group);
        if (sessionSet == null) {
            sessionSet = Collections.synchronizedSet(new HashSet<Session>());
        }
        sessionSet.add(session);
        sessionMap.put(group, sessionSet);
    }

    @OnMessage
    public void broadcastOnMessage(String message, Session session, @PathParam("group") String group)
            throws IOException {

        String option = session.getRequestParameterMap().get("option").get(0);
        System.out.println(option);

        Set<Session> sessionSet = sessionMap.get(group);
        for (Session eachSession : sessionSet) {
            eachSession.getBasicRemote().sendText(message);
        }
    }

    @OnClose
    public void onClose(Session session, @PathParam("group") String group) {

        Set<Session> sessionSet = sessionMap.get(group);
        if (sessionSet == null) {
            return;
        }

        sessionSet.remove(session);

        if (sessionSet.isEmpty()) {
            sessionMap.remove(group);
        }
    }
}
