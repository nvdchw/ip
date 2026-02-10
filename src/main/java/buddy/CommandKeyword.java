package buddy;

/**
 * Defines command keywords and their derived lengths.
 */
public enum CommandKeyword {
    LIST("list"),
    MARK("mark"),
    UNMARK("unmark"),
    DELETE("delete"),
    TODO("todo"),
    DEADLINE("deadline"),
    EVENT("event"),
    FIND("find"),
    BYE("bye");

    private final String keyword;

    CommandKeyword(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Returns the length of the command keyword.
     *
     * @return the length of the command keyword
     */
    public int length() {
        return keyword.length();
    }
}
