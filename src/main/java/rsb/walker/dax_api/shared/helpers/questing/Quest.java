package rsb.walker.dax_api.shared.helpers.questing;

public class Quest {

    private State state;
    private String name;

    Quest(String name, State state){
        this.name = name;
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString(){
        return name + " [" + state + "]";
    }

    public enum State {
        COMPLETED (65280),
        IN_PROGRESS (16711680),
        NOT_STARTED (16776960),
        UNKNOWN (-1);

        private int colorID;

        State(int colorID){
            this.colorID = colorID;
        }

        public static State getState(int colorID){
            for (State state : values()){
                if (state.colorID == colorID){
                    return state;
                }
            }
            return UNKNOWN;
        }

    }

}
