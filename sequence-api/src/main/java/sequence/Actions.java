package sequence;

public enum Actions {
    GetNumbers("get_numlist");

    private String action;

    Actions(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public static Actions getByAction(String action) {
        for (Actions value : values()) {
            if (value.action.equals(action)) {
                return value;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Actions{" +
                "action='" + action + '\'' +
                '}';
    }

}
