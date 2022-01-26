package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.fasterxml.jackson.databind.node.BooleanNode;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.utils.MayhemTalonSRX;

public class Climber extends SubsystemBase {
    private final Solenoid strongArmPiston = new Solenoid(PneumaticsModuleType.CTREPCM, Constants.Solenoid.CLIMBER);
    private final MayhemTalonSRX leftTalon = new MayhemTalonSRX(Constants.Talon.CLIMBER_L);
    private final MayhemTalonSRX rightTalon = new MayhemTalonSRX(Constants.Talon.CLIMBER_R);

    private final boolean ARMS_UP = true;
    private final boolean ARMS_DOWN = false;
    private final double ARMS_OUT_POSITION = 100.0;
    private final double ARMS_UNHOOK_POSITION = 75.0;
    private final double ARMS_IN_POSITION = 50.0;

    private final double MAX_POSITION = 150.0;
    private final double MIN_POSITION = 25.0;

    public Climber() {
        ConfigureTalon(leftTalon);
        ConfigureTalon(rightTalon);
    }

    private void ConfigureTalon(MayhemTalonSRX talon) {
        talon.config_kP(0, 10.0, 0);
        talon.config_kI(0, 0.0, 0);
        talon.config_kD(0, 0.0, 0);
        talon.config_kF(0, 0.0, 0);

        talon.setNeutralMode(NeutralMode.Coast);

        talon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        talon.configNominalOutput(0, 0);
        talon.configPeakOutputVoltage(1, -1);
        talon.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 40, 60, 0.5));

        talon.configNominalOutput(+0.0f, -0.0f);
        talon.configPeakOutputVoltage(100.0, -100.0);
        talon.setInverted(false);
        talon.setSensorPhase(false);

        talon.configForwardSoftLimitThreshold(MAX_POSITION);
        talon.configForwardSoftLimitEnable(true);
        talon.configReverseSoftLimitThreshold(MIN_POSITION);
        talon.configReverseSoftLimitEnable(true);
    }

    public void strongArmsUp() {
        strongArmPiston.set(ARMS_UP);
    }

    public void strongArmsDown() {
        strongArmPiston.set(ARMS_DOWN);
    }

    public void extendArmsFully() {
        leftTalon.set(ControlMode.Position, ARMS_OUT_POSITION);
    }

    public void extendArmsToUnhook() {
        leftTalon.set(ControlMode.Position, ARMS_UNHOOK_POSITION);
    }

    public void retractArms() {
        leftTalon.set(ControlMode.Position, ARMS_IN_POSITION);
    }
}
