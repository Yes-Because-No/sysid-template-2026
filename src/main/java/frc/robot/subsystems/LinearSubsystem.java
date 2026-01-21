package frc.robot.subsystems;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;

import static edu.wpi.first.units.Units.Second;
import static edu.wpi.first.units.Units.Volts;

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
    

    public LinearSubsystem(Map<Integer, Boolean> motorMap){
            config.Feedback.SensorToMechanismRatio = Constants.ROTATIONS_TO_LINEAR_UNITS;
            config.CurrentLimits.StatorCurrentLimitEnable = true;
            config.CurrentLimits.StatorCurrentLimit = Constants.CURRENT_LIMIT;

            for (Map.Entry<Integer, Boolean> entry : motorMap.entrySet()){
                int CANID = entry.getKey().intValue();
                boolean inversion = entry.getValue().booleanValue();
                TalonFX motor = new TalonFX(CANID);
                config.MotorOutput.Inverted = inversion? InvertedValue.Clockwise_Positive: InvertedValue.CounterClockwise_Positive;
                motor.getConfigurator().apply(config);
                motors.add(motor);
            }

            for (int i = 1; i < motors.size(); i++){
                motors.get(i).setControl(new StrictFollower(motors.get(0).getDeviceID()));
            }
        }

    public void setVoltage(double voltage){
        motors.get(0).setControl(new VoltageOut(MathUtil.clamp(voltage, -7, 7)));
    }

     private SysIdRoutine sysIdRoutine = new SysIdRoutine(new SysIdRoutine.Config(
            null,null,Second.of(8),
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
