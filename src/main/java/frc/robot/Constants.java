package frc.robot;
import java.util.HashMap;
import java.util.Map;

import com.ctre.phoenix6.signals.InvertedValue;

import edu.wpi.first.units.VoltageUnit;
import edu.wpi.first.units.measure.Time;
import edu.wpi.first.units.measure.Velocity;
import edu.wpi.first.units.measure.Voltage;

public class Constants {
    public static final double CURRENT_LIMIT = 30;
    public static final double ROTATIONS_TO_LINEAR_UNITS = Math.PI;
    public static final double MAX_VOLTAGE = 12;
    public static final Velocity<VoltageUnit> RAMP_RATE = null;
    public static final Voltage STEP_VOLTAGE = null;
    public static final Time TIME_OUT = null;
    //needs at least one entry (pls no break twin)
    public static final Map<Integer, InvertedValue> MOTORS = new HashMap<>(){{
        put(0, InvertedValue.Clockwise_Positive);
    }};
}
