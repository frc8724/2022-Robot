package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.VictorSPXControlMode;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


import com.ctre.phoenix.motorcontrol.can.*;

public class Intake extends SubsystemBase{
    private final Solenoid piston = new Solenoid(PneumaticsModuleType.CTREPCM, 1);
    private final VictorSPX rollersTalon = new WPI_VictorSPX(Constants.Talon.INTAKE_ROLLERS);

    private final boolean INTAKE_DOWN = false;   
    private final boolean INTAKE_UP = true;
    private final double ROLLER_INTAKE = 0.7;
    private final double ROLLER_STOP = 0.0;

    public Intake()
    {
        rollersTalon.setNeutralMode(NeutralMode.Coast);
        rollersTalon.configNominalOutputForward(+0.0f);
        rollersTalon.configNominalOutputReverse(0.0);
        rollersTalon.configPeakOutputForward(+12.0);
        rollersTalon.configPeakOutputReverse(-12.0);
    }

    public void putDown()
    {
        piston.set(INTAKE_DOWN);
        rollersTalon.set(VictorSPXControlMode.PercentOutput, ROLLER_INTAKE);
    }

    public void eject()
    {
        piston.set(INTAKE_DOWN);
        rollersTalon.set(VictorSPXControlMode.PercentOutput, -ROLLER_INTAKE);
    }

    public void putUp()
    {
        piston.set(INTAKE_UP);
        rollersTalon.set(VictorSPXControlMode.PercentOutput, ROLLER_STOP);
    }

    public void updateSmartDashboard()
    {
        SmartDashboard.putBoolean("Intake Position", piston.get());
        SmartDashboard.putNumber("Intake Speed", rollersTalon.getMotorOutputPercent());        
    }
}
