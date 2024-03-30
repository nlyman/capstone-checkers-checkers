package checkers.capstone.controllers.tools.session_tools;

import checkers.capstone.game.rules.RuleInterfaces.TheGame;

/**Contains the data for the user's session. */
public class SessionData {

    /**The user's id. */
    private final long userId;
    /**Randomly generated key used to verify the user. */
    private long key;
    /**The game session the user is using. */
    private TheGame game;

    /**
     * @param userId The user's id.
     * @param key Randomly generated key used to verify the user.
     */
    public SessionData(long userId, long key) {
        this.userId = userId;
        this.key = key;
        this.game = null;
    }

    /**
     * Gets the user id.
     * @return The user id.
     */
    public long getUserId() {
        return userId;
    }

    protected long getKey() {
        return key;
    }

    protected void setKey(long key) {
        this.key = key;
    }

    /**
     * Gets the game instance instance.
     * @return The game instance.
     */
    public TheGame getGame() {
        return game;
    }

    /**
     * Sets the current game instance.
     * @param game The current game.
     */
    public void setGame(TheGame game) {
        this.game = game;
    }
}
