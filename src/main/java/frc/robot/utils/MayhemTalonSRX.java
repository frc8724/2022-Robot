package frc.robot.utils;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class MayhemTalonSRX extends WPI_TalonSRX {
    public double kP;
    public double kI;
    public double kD;
    public double kF;

    public MayhemTalonSRX(int channel) {
        super(channel);
    }

    @Override
    public ErrorCode config_kP(int slotIdx, double value, int timeoutMs) {
        this.kP = value;
        return super.config_kP(slotIdx, value, timeoutMs);
    }

    @Override
    public ErrorCode config_kI(int slotIdx, double value, int timeoutMs) {
        this.kI = value;
        return super.config_kI(slotIdx, value, timeoutMs);
    }

    @Override
    public ErrorCode config_kD(int slotIdx, double value, int timeoutMs) {
        this.kD = value;
        return super.config_kD(slotIdx, value, timeoutMs);
    }

    @Override
    public ErrorCode config_kF(int slotIdx, double value, int timeoutMs) {
        this.kF = value;
        return super.config_kF(slotIdx, value, timeoutMs);
    }

    public void configNominalOutput(double forward, double reverse) {
        super.configNominalOutputForward(forward, 1000);
        super.configNominalOutputReverse(reverse, 1000);
    }

    public void configPeakOutputVoltage(double forward, double reverse) {
        super.configPeakOutputForward(forward, 1000);
        super.configPeakOutputReverse(reverse, 1000);
    }
}
