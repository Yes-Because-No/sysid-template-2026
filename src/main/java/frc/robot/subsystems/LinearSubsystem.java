package frc.robot.subsystems;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;

import java.util.ArrayList;
import java.util.Map;

import com.ctre.phoenix6.SignalLogger;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.StrictFollower;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;

import frc.robot.Constants;

public class LinearSubsystem extends SubsystemBase {

    private ArrayList<TalonFX> motors = new ArrayList<TalonFX>();
    private TalonFXConfiguration config = new TalonFXConfiguration();
    

    public LinearSubsystem(Map<Integer, InvertedValue> motorMap){
            config.Feedback.SensorToMechanismRatio = Constants.ROTATIONS_TO_LINEAR_UNITS;
            config.CurrentLimits.StatorCurrentLimitEnable = true;
            config.CurrentLimits.StatorCurrentLimit = Constants.CURRENT_LIMIT;

            for (Map.Entry<Integer, InvertedValue> entry : motorMap.entrySet()){
                int CANID = entry.getKey().intValue();
                InvertedValue inversion = entry.getValue();
                TalonFX motor = new TalonFX(CANID);
                config.MotorOutput.Inverted = inversion;
                motor.getConfigurator().apply(config);
                motors.add(motor);
            }

            for (int i = 1; i < motors.size(); i++){
                motors.get(i).setControl(new StrictFollower(motors.get(0).getDeviceID()));
            }
        }

    public void setVoltage(double voltage){
        motors.get(0).setControl(new VoltageOut(MathUtil.clamp(voltage, -Constants.MAX_VOLTAGE, Constants.MAX_VOLTAGE)));
    }

     private SysIdRoutine sysIdRoutine = new SysIdRoutine(new SysIdRoutine.Config(
            Constants.RAMP_RATE,
            Constants.STEP_VOLTAGE,
            Constants.TIME_OUT,
            (state)-> SignalLogger.writeString("state", state.toString())
        ),
        new SysIdRoutine.Mechanism(
            voltage -> setVoltage(voltage.magnitude()), 
            null, 
            this
    ));

    public Command sysIdQuasistatic(SysIdRoutine.Direction direction) {
        return sysIdRoutine.quasistatic(direction);
     }
     
     public Command sysIdDynamic(SysIdRoutine.Direction direction) {
        return sysIdRoutine.dynamic(direction);
     }
}
