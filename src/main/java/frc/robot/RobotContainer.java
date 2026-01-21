// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.SignalLogger;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.subsystems.LinearSubsystem;

public class RobotContainer {
  LinearSubsystem linearSubsystem = new LinearSubsystem(Constants.MOTORS);
  CommandXboxController controller = new CommandXboxController(0);

  public RobotContainer() {
    configureBindings();
  }

  private void configureBindings() {
    controller.x().whileTrue(linearSubsystem.sysIdQuasistatic(Direction.kForward));
    controller.y().whileTrue(linearSubsystem.sysIdQuasistatic(Direction.kReverse));
    controller.a().whileTrue(linearSubsystem.sysIdDynamic(Direction.kForward));
    controller.b().whileTrue(linearSubsystem.sysIdDynamic(Direction.kReverse));
    controller.rightBumper().whileTrue(Commands.runOnce(SignalLogger::start));
    controller.leftBumper().whileTrue(Commands.runOnce(SignalLogger::stop));
    
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
