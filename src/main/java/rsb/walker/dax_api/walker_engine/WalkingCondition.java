package rsb.walker.dax_api.walker_engine;


public interface WalkingCondition {

    enum State {
        /*
        Exits out WebWalker and returns true
         */
        EXIT_OUT_WALKER_SUCCESS,


        /*
        Exits out WebWalker and returns false
         */
        EXIT_OUT_WALKER_FAIL,


        /*
        Resumes walker
         */
        CONTINUE_WALKER
    }

    /**
     *
     * The action you want to call while the walker is walking to your destination.
     * You can eat and do whatever here. You do not need to include a sleep.
     *
     * @return The @State which you want the walker to handle. Refer to @{@link State}
     */
    State action();

    /**
     * Combines two conditions
     * @param conditionB
     * @return
     */
    default WalkingCondition combine(WalkingCondition conditionB){
        if (conditionB == null){
            return this;
        }
        WalkingCondition conditionA = this;
        return () -> {
            State a = conditionA.action();
            if (a != State.CONTINUE_WALKER){
                return a;
            }
            return conditionB.action();
        };
    }
}
