package frc.robot;
import java.util.HashMap;
import java.util.Map;

public class Constants {
    public static final double CURRENT_LIMIT = 30;
    public static final double ROTATIONS_TO_LINEAR_UNITS = Math.PI;
    //needs at least one entry (pls no break twin)
    public static final Map<Integer, Boolean> MOTORS = new HashMap<>(){{
        put(4, false);
    }};
}
