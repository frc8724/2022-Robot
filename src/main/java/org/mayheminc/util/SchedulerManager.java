/**
 * Copyright (c) 2015-2019 Team 501 - The PowerKnights. All Rights Reserved.
 * Open Source Software - May be modified and shared by FRC teams. The code must
 * be accompanied by the Team 501 BSD license file in the root directory of the
 * project. You may also obtain a copy of it from the following:
 * http://www.opensource.org/licenses/bsd-license.php.
 *
 * See (Git) repository metadata for author and revision history for this file.
 **/

package org.mayheminc.util;

// import org.slf4j.Logger;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableType;
import edu.wpi.first.networktables.NetworkTableValue;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// import riolog.RioLogger;

/**
 * 
 */
public class SchedulerManager // implements ITelemetryProvider
{

   /* Our classes logger */
   // @SuppressWarnings("unused")
   // private static final Logger logger =
   // RioLogger.getLogger(SchedulerManager.class.getName());

   /** Singleton instance of class for all to use **/
   private static SchedulerManager ourInstance;
   /** Name of our subsystem **/
   private final static String myName = "Running Commands";// TelemetryNames.Scheduler.name;

   /**
    * Constructs instance of the subsystem. Assumed to be called before any usage
    * of the subsystem; and verifies only called once. Allows controlled startup
    * sequencing of the robot and all it's subsystems.
    **/
   public static synchronized void constructInstance() {
      // SmartDashboard.putBoolean(TelemetryNames.Scheduler.status, false);

      if (ourInstance != null) {
         throw new IllegalStateException(myName + " Already Constructed");
      }
      ourInstance = new SchedulerManager();

      // SmartDashboard.putBoolean(TelemetryNames.Scheduler.status, true);
   }

   /**
    * Returns the singleton instance of the subsystem in the form of the
    * <i>Interface</i> that is defined for it. If it hasn't been constructed yet,
    * throws an <code>IllegalStateException</code>.
    *
    * @return singleton instance of subsystem
    **/
   public static SchedulerManager getInstance() {
      if (ourInstance == null) {
         throw new IllegalStateException(myName + " Not Constructed Yet");
      }
      return ourInstance;
   }

   private final NetworkTable liveWindow;
   private final NetworkTable ungrouped;
   private final NetworkTable scheduler;
   private final StringBuilder names;

   public SchedulerManager() {
      NetworkTableInstance inst = NetworkTableInstance.getDefault();
      liveWindow = inst.getTable("LiveWindow");
      ungrouped = liveWindow.getSubTable("Ungrouped");
      scheduler = ungrouped.getSubTable("Scheduler");

      names = new StringBuilder();
   }

   // @Override
   public void updateTelemetry() {
      NetworkTableEntry namesEntry = scheduler.getEntry("Names");
      NetworkTableValue namesValue = namesEntry.getValue();
      if ((namesValue == null)) {
         SmartDashboard.putString("Current Commands", "namesValue is null");
         return;
      }

      if ((namesValue.getType() != NetworkTableType.kStringArray)) {

         SmartDashboard.putString("Current Commands", "namesValue is " + namesValue.getType());
         return;
      }

      names.setLength(0);
      for (String name : namesValue.getStringArray()) {
         names.append(name).append(":  ");
      }

      SmartDashboard.putString("Current Commands", names.toString());
   }

}
