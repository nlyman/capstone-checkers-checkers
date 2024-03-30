package checkers.capstone.controllers.tools.session_tools;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import checkers.capstone.controllers.tools.exception_stuff.InvalidToken;
import jakarta.servlet.http.*;

public class SessionManager {

    private static final Map<Long, SessionData> sessions = new HashMap<>();

    /**
     * Sets the cookie for the user.  (Needs security.)
     * @param response The thing storing cookie.
     * @param userId The id being set to cookie.
     */
    public static void login(HttpServletResponse response, long userId) {
        Cookie id = new Cookie("user-id", String.valueOf(userId));
        long tokenNumber = createToken();
        Cookie token = new Cookie("token", convertNumbersToToken(tokenNumber));
        sessions.put(userId, new SessionData(userId, tokenNumber));
        response.addCookie(id);
        response.addCookie(token);
    }

    /**
     * Clears the cookie storing user.
     * @param response Where cookie is stored.
     */
    public static void logout(long userId, HttpServletResponse response){
        Cookie id = new Cookie("user-id", null);
        Cookie token = new Cookie("token", null);
        id.setMaxAge(0);
        token.setMaxAge(0);
        sessions.remove(userId);
        response.addCookie(id);
        response.addCookie(token);
    }

    public static long createToken(){
        Random random = new Random();
        return random.nextLong();
    }

    private static String convertNumbersToToken(long number){
        char[] stuff = new char[8];
        for(int i=0; i<8; i++){
            stuff[i]=(char)(number%256);
            number/=256;
        }
        try{
            return URLEncoder.encode(String.valueOf(stuff), "UTF-8");
        }
        catch(UnsupportedEncodingException e){//This should not be encountered and checking for it is annoying.
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Gets the id of the user making the request.
     * @param request User's stuff.
     * @return The user id.  0 on fail.
     */
    public static long getUser(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie: cookies){
            if (cookie.getName().equals("user-id")){
                return Long.parseLong(cookie.getValue());
            }
        }
        return 0;
    }

    /**
     * Verifies the session and returns it.
     * @param request The user's cookies.
     * @return The data.  Null if user guest.
     */
    public static SessionData getVerifiedSessionData(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        long userId=0;
        String token=null;
        for(Cookie cookie: cookies){
            if (cookie.getName().equals("user-id")){
                userId = Long.parseLong(cookie.getValue());
            }
            else if (cookie.getName().equals("token")){
                token = cookie.getValue();
            }
        }
        if (userId==0)//No need to confirm a guest.
            return null;

        if (token!=null && sessions.containsKey(userId)){
            SessionData data = sessions.get(userId);
            String correctToken=convertNumbersToToken(data.getKey());
            if (correctToken.equals(token))
                return data;
        }
        throw new InvalidToken("Failed token validation with "+userId+".");
    }
}
