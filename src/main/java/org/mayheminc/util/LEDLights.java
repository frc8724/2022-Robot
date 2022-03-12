package org.mayheminc.util;

import edu.wpi.first.wpilibj.motorcontrol.Spark;
import frc.robot.Constants;

public class LEDLights {

    public enum PatternID {
        RAINBOW_RAINBOW_PALETTE(-0.99, "Rainbow, Rainbow Palette"),
        RAINBOW_PARTY_PALETTE(-0.97, "Rainbow, Party Palette"), RAINBOW_OCEAN_PALETTE(-0.95, "Rainbow, Ocean Palette"),
        RAINBOW_LAVA_PALETTE(-0.93, "Rainbow, Lava Palette"), RAINBOW_FOREST_PALETTE(-0.91, "Rainbow, Forest Palette"),
        RAINBOW_WITH_GLITTER(-0.89, "Rainbow with Glitter"), CONFETTI(-0.87, "Confetti"), SHOT_RED(-0.85, "Shot, Red"),
        SHOT_BLUE(-0.83, "Shot, Blue"), SHOT_WHITE(-0.81, "Show, White"),
        SINELON_RAINBOW_PALETTE(-0.79, "Sinelon, Rainbow Palette"),
        SINELON_PARTY_PALETTE(-0.77, "Sinelon, Party Palette"), SINELON_OCEAN_PALETTE(-0.75, "Sinelon, Ocean Palette"),
        SINELON_LAVA_PALETTE(-0.73, "Sinelon, Lava Palette"), SINELON_FOREST_PALETTE(-0.71, "Sinelon, Forest Palette"),
        BPM_RAINBOW_PALETTE(-0.69, "BPM, Rainbow Palette"), BPM_PARTY_PALETTE(-0.67, "BPM, Party Palette"),
        BPM_OCEAN_PALETTE(-0.65, "BPM, Ocean Palette"), BPM_LAVA_PALETTE(-0.63, "BPM, Lava Palette"),
        BPM_FOREST_PALETTE(-0.61, "BPM, Forest Palette"), FIRE_MEDIUM(-0.59, "Fire, Medium"),
        FIRE_LARGE(-0.57, "Fire, Large"), TWINKLES_RAINBOW_PALETTE(-0.55, "Twinkles, Rainbow Palette"),
        TWINKLES_PARTY_PALETTE(-0.53, "Twinkles, Party Palette"),
        TWINKLES_OCEAN_PALETTE(-0.51, "Twinkles, Ocean Palette"),
        TWINKLES_LAVA_PALETTE(-0.49, "Twinkles, Lava Palette"),
        TWINKLES_FOREST_PALETTE(-0.47, "Twinkles, Forest Palette"),
        COLOR_WAVES_RAINBOW_PALETTE(-0.45, "Color Waves, Rainbow Palette"),
        COLOR_WAVES_PARTY_PALETTE(-0.43, "Color Waves, Party Palette"),
        COLOR_WAVES_OCEAN_PALETTE(-0.41, "Color Waves, Ocean Palette"),
        COLOR_WAVES_LAVA_PALETTE(-0.39, "Color Waves, Lava Palette"),
        COLOR_WAVES_FOREST_PALETTE(-0.37, "Color Waves, Forest Palette"),
        LARSON_SCANNER_RED(-0.35, "Larson Scanner, Red"), LARSON_SCANNER_GRAY(-0.33, "Larson Scanner, Gray"),
        LIGHT_CHASE_RED(-0.31, "Light Chase, Red"), LIGHT_CHASE_BLUE(-0.29, "Light Chase, Blue"),
        LIGHT_CHASE_GRAY(-0.27, "Light Chase, Gray"), HEARTBEAT_RED(-0.25, "Heartbeat, Red"),
        HEARTBEAT_BLUE(-0.23, "Heartbeat, Blue"), HEARTBEAT_WHITE(-0.21, "Heartbeat, Blue"),
        HEARTBEAT_GRAY(-0.19, "Heartbeat, Gray"), BREATH_RED(-0.17, "Breath, Red"), BREATH_BLUE(-0.15, "Breath, Blue"),
        BREATH_GRAY(-0.13, "Breath, Gray"), STROBE_RED(-0.11, "Strobe, Red"), STROBE_BLUE(-0.09, "Strobe, Blue"),
        STROBE_GOLD(-0.07, "Strobe, Blue"), STROBE_WHITE(-0.05, "Strobe, Gray"),
        COLOR_1_BLEND_TO_BLACK(-0.03, "Color 1 Blend to Black"),
        COLOR_1_LARSON_SCANNER(-0.01, "Color 1 Larson Scanner"), COLOR_1_LIGHT_CHASE(0.01, "Color 1 Light Chase"),
        COLOR_1_HEARTBEAT_SLOW(0.03, "Color 1 Heartbeat Slow"),
        COLOR_1_HEARTBEAT_MEDIUM(0.05, "Color 1 Heartbeat Medium"),
        COLOR_1_HEARTBEAT_FAST(0.07, "Color 1 Heartbeat Fast"), COLOR_1_BREATH_SLOW(0.09, "Color 1 Breath Slow"),
        COLOR_1_BREATH_FAST(0.11, "Color 1 Breath Fast"), COLOR_1_SHOT(0.13, "Color 1 Shot"),
        COLOR_1_STROBE(0.15, "Color 1 Strobe"), COLOR_2_BLEND_TO_BLACK(0.17, "Color 2 Blend to Black"),
        COLOR_2_LARSON_SCANNER(0.19, "Color 2 Larson Scanner"), COLOR_2_LIGHT_CHASE(0.21, "Color 2 Light Chase"),
        COLOR_2_HEARTBEAT_SLOW(0.23, "Color 2 Heartbeat Slow"),
        COLOR_2_HEARTBEAT_MEDIUM(0.25, "Color 2 Heartbeat Medium"),
        COLOR_2_HEARTBEAT_FAST(0.27, "Color 2 Heartbeat Fast"), COLOR_2_BREATH_SLOW(0.29, "Color 2 Breath Slow"),
        COLOR_2_BREATH_FAST(0.31, "Color 2 Breath Fast"), COLOR_2_SHOT(0.33, "Color 2 Shot"),
        COLOR_2_STROBE(0.35, "Color 2 Strobe"), SPARKLE_1_ON_2(0.37, "Sparkle, Color 1 on Color 2"),
        SPARKLE_2_ON_1(0.39, "Sparkle, Color 2 on Color 1"), GRADIENT_1_AND_2(0.41, "Gradient, Color 1 and 2"),
        BPM_1_AND_2(0.43, "BPM, Color 1 and 2"), END_TO_END_BLEND_1_TO_2(0.45, "End to End Blend, Color 1 to 2"),
        END_TO_END_BLEND_2_TO_1(0.47, "End to End Blend, Color 2 to 1"),
        COLOR_1_AND_2(0.49, "Color 1 and 2, no blending"), TWINKLES_COLOR_1_AND_2(0.51, "Twinkles, Color 1 and 2"),
        WAVES_COLOR_1_AND_2(0.53, "Waves, Color 1 and 2"), SINELON_COLOR_1_AND_2(0.55, "Sinelon, Color 1 and 2"),
        HOT_PINK(0.57, "Solid Hot Pink"), DARK_RED(0.59, "Solid Dark Red"), RED(0.61, "Solid Red"),
        RED_ORANGE(0.63, "Solid Red Orange"), ORANGE(0.65, "Solid Orange"), GOLD(0.67, "Solid Gold"),
        YELLOW(0.69, "Solid Yellow"), LAWN_GREEN(0.71, "Solid Lawn Green"), LIME(0.73, "Solid Lime Green"),
        DARK_GREEN(0.75, "Solid Dark Green"), GREEN(0.77, "Solid Green"), BLUE_GREEN(0.79, "Solid Blue Green"),
        AQUA(0.81, "Solid Aqua"), SKY_BLUE(0.83, "Solid Sky Blue"), DARK_BLUE(0.85, "Solid Dark Blue"),
        BLUE(0.87, "Solid Blue"), BLUE_VIOLET(0.89, "Solid Blue Violet"), VIOLET(0.91, "Solid Violet"),
        WHITE(0.93, "Solid White"), GRAY(0.95, "Solid Gray"), DARK_GRAY(0.97, "Solid Dark Gray"),
        BLACK(0.99, "Solid Black");

        private final double m_pwmVal;
        private final String m_patternName;

        private PatternID(double pwmVal, String patternName) {
            m_pwmVal = pwmVal;
            m_patternName = patternName;
        }

        private double getVal() {
            return m_pwmVal;
        }

        public String getName() {
            return m_patternName;
        }
    }

    private final Spark m_blinkin = new Spark(Constants.Lights.LIGHTS_PORT);

    public void set(PatternID pattern) {
        m_blinkin.set(pattern.getVal());
    }
}
