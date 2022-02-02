package org.mayheminc.util;

public interface PidTunerObject {

	double getP();

	double getI();

	double getD();

	double getF();

	void setP(double d);

	void setI(double d);

	void setD(double d);

	void setF(double d);
}
