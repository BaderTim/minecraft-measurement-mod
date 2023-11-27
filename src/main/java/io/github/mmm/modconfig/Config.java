package io.github.mmm.modconfig;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config  {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> LIDAR1_SWITCH;
    public static final ForgeConfigSpec.ConfigValue<Integer> LIDAR1_HORIZONTAL_SCANNING_RADIUS_IN_DEG;
    public static final ForgeConfigSpec.ConfigValue<Integer> LIDAR1_HORIZONTAL_SCANS_PER_RADIUS;
    public static final ForgeConfigSpec.ConfigValue<Integer> LIDAR1_VERTICAL_SCANNING_RADIUS_IN_DEG;
    public static final ForgeConfigSpec.ConfigValue<Integer> LIDAR1_VERTICAL_SCANS_PER_RADIUS;
    public static final ForgeConfigSpec.ConfigValue<Integer> LIDAR1_YAW_OFFSET_FROM_POV_IN_DEG;
    public static final ForgeConfigSpec.ConfigValue<Integer> LIDAR1_PITCH_OFFSET_FROM_POV_IN_DEG;
    public static final ForgeConfigSpec.ConfigValue<Integer> LIDAR1_ROLL_OFFSET_FROM_POV_IN_DEG;
    public static final ForgeConfigSpec.ConfigValue<Integer> LIDAR1_MAXIMUM_MEASUREMENT_DISTANCE;
    public static final ForgeConfigSpec.ConfigValue<Integer> LIDAR1_MEASUREMENT_FREQUENCY_IN_HZ;

    public static final ForgeConfigSpec.ConfigValue<Boolean> LIDAR2_SWITCH;
    public static final ForgeConfigSpec.ConfigValue<Integer> LIDAR2_HORIZONTAL_SCANNING_RADIUS_IN_DEG;
    public static final ForgeConfigSpec.ConfigValue<Integer> LIDAR2_HORIZONTAL_SCANS_PER_RADIUS;
    public static final ForgeConfigSpec.ConfigValue<Integer> LIDAR2_VERTICAL_SCANNING_RADIUS_IN_DEG;
    public static final ForgeConfigSpec.ConfigValue<Integer> LIDAR2_VERTICAL_SCANS_PER_RADIUS;
    public static final ForgeConfigSpec.ConfigValue<Integer> LIDAR2_YAW_OFFSET_FROM_POV_IN_DEG;
    public static final ForgeConfigSpec.ConfigValue<Integer> LIDAR2_PITCH_OFFSET_FROM_POV_IN_DEG;
    public static final ForgeConfigSpec.ConfigValue<Integer> LIDAR2_ROLL_OFFSET_FROM_POV_IN_DEG;
    public static final ForgeConfigSpec.ConfigValue<Integer> LIDAR2_MAXIMUM_MEASUREMENT_DISTANCE;
    public static final ForgeConfigSpec.ConfigValue<Integer> LIDAR2_MEASUREMENT_FREQUENCY_IN_HZ;

    public static final ForgeConfigSpec.ConfigValue<Boolean> LIDAR3_SWITCH;
    public static final ForgeConfigSpec.ConfigValue<Integer> LIDAR3_HORIZONTAL_SCANNING_RADIUS_IN_DEG;
    public static final ForgeConfigSpec.ConfigValue<Integer> LIDAR3_HORIZONTAL_SCANS_PER_RADIUS;
    public static final ForgeConfigSpec.ConfigValue<Integer> LIDAR3_VERTICAL_SCANNING_RADIUS_IN_DEG;
    public static final ForgeConfigSpec.ConfigValue<Integer> LIDAR3_VERTICAL_SCANS_PER_RADIUS;
    public static final ForgeConfigSpec.ConfigValue<Integer> LIDAR3_YAW_OFFSET_FROM_POV_IN_DEG;
    public static final ForgeConfigSpec.ConfigValue<Integer> LIDAR3_PITCH_OFFSET_FROM_POV_IN_DEG;
    public static final ForgeConfigSpec.ConfigValue<Integer> LIDAR3_ROLL_OFFSET_FROM_POV_IN_DEG;
    public static final ForgeConfigSpec.ConfigValue<Integer> LIDAR3_MAXIMUM_MEASUREMENT_DISTANCE;
    public static final ForgeConfigSpec.ConfigValue<Integer> LIDAR3_MEASUREMENT_FREQUENCY_IN_HZ;

    public static final ForgeConfigSpec.ConfigValue<Boolean> IMU1_SWITCH;
    public static final ForgeConfigSpec.ConfigValue<Boolean> IMU1_CONSIDER_GRAVITY;
    public static final ForgeConfigSpec.ConfigValue<Integer> IMU1_YAW_OFFSET_FROM_POV_IN_DEG;
    public static final ForgeConfigSpec.ConfigValue<Integer> IMU1_PITCH_OFFSET_FROM_POV_IN_DEG;
    public static final ForgeConfigSpec.ConfigValue<Integer> IMU1_ROLL_OFFSET_FROM_POV_IN_DEG;
    public static final ForgeConfigSpec.ConfigValue<Integer> IMU1_MEAUREMENT_FREQUENCY_IN_HZ;

    public static final ForgeConfigSpec.ConfigValue<Boolean> MULTITHREAD_SWITCH;
    public static final ForgeConfigSpec.ConfigValue<Integer> THREAD_COUNT_MULTIPLIER;
    public static final ForgeConfigSpec.ConfigValue<Integer> SAVE_INTERVAL;
    public static final ForgeConfigSpec.ConfigValue<Boolean> TICK_TIME_WARNING;
    public static final ForgeConfigSpec.ConfigValue<Integer> TICK_TIME_WARNING_TOLERANCE;

    static {
        BUILDER.push("mmm_config");
        MULTITHREAD_SWITCH = BUILDER.comment("Activate/Deactivate Multithreading (active = true, inactive = false)")
                .define("multithread_switch", true);
        THREAD_COUNT_MULTIPLIER = BUILDER.comment("Multiplier for the amount of Threads. Default is 1, resulting in 1*LOGICAL_CORES_AMOUNT Threads")
                .define("thread_count_multiplier", 4);
        SAVE_INTERVAL = BUILDER.comment("Interval in ticks (1 tick usually takes 50ms) in which the data is saved to the file")
                .define("save_interval", 20*60*5); // 20 ticks * 60 seconds * 5 minutes = 5 minutes
        TICK_TIME_WARNING = BUILDER.comment("Activate/Deactivate Tick Time Warning (active = true, inactive = false)")
                .define("tick_time_warning", false);
        TICK_TIME_WARNING_TOLERANCE = BUILDER.comment("Tolerance for the Tick Time Warning in MS. Default Tick Time is 50ms, this means a warning will appear when the Tick Time is longer than 50ms + tolerance.")
                .define("tick_time_warning_tolerance", 5);

        BUILDER.push("lidar1");
        LIDAR1_SWITCH = BUILDER.comment("Activate/Deactivate LIDAR Sensor lidar1 (active = true, inactive = false)")
                .define("lidar1_switch", true);
        LIDAR1_HORIZONTAL_SCANNING_RADIUS_IN_DEG = BUILDER.comment("Horizontal Scanning Radius in Degrees for LIDAR Sensor lidar1")
                .define("lidar1_horizontal_scanning_radius_in_deg", 180);
        LIDAR1_HORIZONTAL_SCANS_PER_RADIUS = BUILDER.comment("Horizontal Scans per Radius for LIDAR Sensor lidar1")
                .define("lidar1_horizontal_scans_per_radius", 180);
        LIDAR1_VERTICAL_SCANNING_RADIUS_IN_DEG = BUILDER.comment("Vertical Scanning Radius in Degrees for LIDAR Sensor lidar1 (set it to 0 if Lidar should be 2D)")
                .define("lidar1_vertical_scanning_radius_in_deg", 60);
        LIDAR1_VERTICAL_SCANS_PER_RADIUS = BUILDER.comment("Vertical Scans per Radius for LIDAR Sensor lidar1 (set it to 0 if Lidar should be 2D)")
                .define("lidar1_vertical_scans_per_radius", 60);
        LIDAR1_YAW_OFFSET_FROM_POV_IN_DEG = BUILDER.comment("Yaw Offset from POV in Degrees for LIDAR Sensor lidar1")
                .define("lidar1_yaw_offset_from_pov_in_deg", 0);
        LIDAR1_PITCH_OFFSET_FROM_POV_IN_DEG = BUILDER.comment("Pitch Offset from POV in Degrees for LIDAR Sensor lidar1")
                .define("lidar1_pitch_offset_from_pov_in_deg", 0);
        LIDAR1_ROLL_OFFSET_FROM_POV_IN_DEG = BUILDER.comment("Roll Offset from POV in Degrees for LIDAR Sensor lidar1")
                .define("lidar1_roll_offset_from_pov_in_deg", 0);
        LIDAR1_MAXIMUM_MEASUREMENT_DISTANCE = BUILDER.comment("Maximum Measurement Distance in Meters for LIDAR Sensor lidar1")
                .define("lidar1_maximum_measurement_distance", 10);
        LIDAR1_MEASUREMENT_FREQUENCY_IN_HZ = BUILDER.comment("Measurement Frequency in Hz for LIDAR Sensor lidar1 (allowed are: 1, 2, 4, 5, 10, 20)")
                .define("lidar1_measurement_frequency_in_hz", 10);
        BUILDER.pop();

        BUILDER.push("lidar2");
        LIDAR2_SWITCH = BUILDER.comment("Activate/Deactivate LIDAR Sensor lidar2 (active = true, inactive = false)")
                .define("lidar2_switch", true);
        LIDAR2_HORIZONTAL_SCANNING_RADIUS_IN_DEG = BUILDER.comment("Horizontal Scanning Radius in Degrees for LIDAR Sensor lidar2")
                .define("lidar2_horizontal_scanning_radius_in_deg", 360);
        LIDAR2_HORIZONTAL_SCANS_PER_RADIUS = BUILDER.comment("Horizontal Scans per Radius for LIDAR Sensor lidar2")
                .define("lidar2_horizontal_scans_per_radius", 45);
        LIDAR2_VERTICAL_SCANNING_RADIUS_IN_DEG = BUILDER.comment("Vertical Scanning Radius in Degrees for LIDAR Sensor lidar2 (set it to 0 if Lidar should be 2D)")
                .define("lidar2_vertical_scanning_radius_in_deg", 0);
        LIDAR2_VERTICAL_SCANS_PER_RADIUS = BUILDER.comment("Vertical Scans per Radius for LIDAR Sensor lidar2 (set it to 0 if Lidar should be 2D)")
                .define("lidar2_vertical_scans_per_radius", 0);
        LIDAR2_YAW_OFFSET_FROM_POV_IN_DEG = BUILDER.comment("Yaw Offset from POV in Degrees for LIDAR Sensor lidar2")
                .define("lidar2_yaw_offset_from_pov_in_deg", 0);
        LIDAR2_PITCH_OFFSET_FROM_POV_IN_DEG = BUILDER.comment("Pitch Offset from POV in Degrees for LIDAR Sensor lidar2")
                .define("lidar2_pitch_offset_from_pov_in_deg", 0);
        LIDAR2_ROLL_OFFSET_FROM_POV_IN_DEG = BUILDER.comment("Roll Offset from POV in Degrees for LIDAR Sensor lidar2")
                .define("lidar2_roll_offset_from_pov_in_deg", 0);
        LIDAR2_MAXIMUM_MEASUREMENT_DISTANCE = BUILDER.comment("Maximum Measurement Distance in Meters for LIDAR Sensor lidar2")
                .define("lidar2_maximum_measurement_distance", 10);
        LIDAR2_MEASUREMENT_FREQUENCY_IN_HZ = BUILDER.comment("Measurement Frequency in Hz for LIDAR Sensor lidar2 (allowed are: 1, 2, 4, 5, 10, 20)")
                .define("lidar2_measurement_frequency_in_hz", 10);
        BUILDER.pop();

        BUILDER.push("lidar3");
        LIDAR3_SWITCH = BUILDER.comment("Activate/Deactivate LIDAR Sensor lidar3 (active = true, inactive = false)")
                .define("lidar3_switch", true);
        LIDAR3_HORIZONTAL_SCANNING_RADIUS_IN_DEG = BUILDER.comment("Horizontal Scanning Radius in Degrees for LIDAR Sensor lidar3")
                .define("lidar3_horizontal_scanning_radius_in_deg", 360);
        LIDAR3_HORIZONTAL_SCANS_PER_RADIUS = BUILDER.comment("Horizontal Scans per Radius for LIDAR Sensor lidar3")
                .define("lidar3_horizontal_scans_per_radius", 180);
        LIDAR3_VERTICAL_SCANNING_RADIUS_IN_DEG = BUILDER.comment("Vertical Scanning Radius in Degrees for LIDAR Sensor lidar3 (set it to 0 if Lidar should be 2D)")
                .define("lidar3_vertical_scanning_radius_in_deg", 0);
        LIDAR3_VERTICAL_SCANS_PER_RADIUS = BUILDER.comment("Vertical Scans per Radius for LIDAR Sensor lidar3 (set it to 0 if Lidar should be 2D)")
                .define("lidar3_vertical_scans_per_radius", 0);
        LIDAR3_YAW_OFFSET_FROM_POV_IN_DEG = BUILDER.comment("Yaw Offset from POV in Degrees for LIDAR Sensor lidar3")
                .define("lidar3_yaw_offset_from_pov_in_deg", 0);
        LIDAR3_PITCH_OFFSET_FROM_POV_IN_DEG = BUILDER.comment("Pitch Offset from POV in Degrees for LIDAR Sensor lidar3")
                .define("lidar3_pitch_offset_from_pov_in_deg", 0);
        LIDAR3_ROLL_OFFSET_FROM_POV_IN_DEG = BUILDER.comment("Roll Offset from POV in Degrees for LIDAR Sensor lidar3")
                .define("lidar3_roll_offset_from_pov_in_deg", 0);
        LIDAR3_MAXIMUM_MEASUREMENT_DISTANCE = BUILDER.comment("Maximum Measurement Distance in Meters for LIDAR Sensor lidar3")
                .define("lidar3_maximum_measurement_distance", 10);
        LIDAR3_MEASUREMENT_FREQUENCY_IN_HZ = BUILDER.comment("Measurement Frequency in Hz for LIDAR Sensor lidar3 (allowed are: 1, 2, 4, 5, 10, 20)")
                .define("lidar3_measurement_frequency_in_hz", 10);
        BUILDER.pop();

        BUILDER.push("imu1");
        IMU1_SWITCH = BUILDER.comment("Activate/Deactivate IMU Sensor imu1 (active = true, inactive = false)")
                .define("imu1_switch", true);
        IMU1_CONSIDER_GRAVITY = BUILDER.comment("Consider Gravity for IMU Sensor imu1 (true = consider, false = do not consider)")
                .define("imu1_consider_gravity", true);
        IMU1_YAW_OFFSET_FROM_POV_IN_DEG = BUILDER.comment("Yaw Offset from POV in Degrees for IMU Sensor imu1")
                .define("imu1_yaw_offset_from_pov_in_deg", 0);
        IMU1_PITCH_OFFSET_FROM_POV_IN_DEG = BUILDER.comment("Pitch Offset from POV in Degrees for IMU Sensor imu1")
                .define("imu1_pitch_offset_from_pov_in_deg", 0);
        IMU1_ROLL_OFFSET_FROM_POV_IN_DEG = BUILDER.comment("Roll Offset from POV in Degrees for IMU Sensor imu1")
                .define("imu1_roll_offset_from_pov_in_deg", 0);
        IMU1_MEAUREMENT_FREQUENCY_IN_HZ = BUILDER.comment("Measurement Frequency in Hz for IMU Sensor imu1 (allowed are: 1, 2, 4, 5, 10, 20)")
                .define("imu1_measurement_frequency_in_hz", 10);
        BUILDER.pop();

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

}
