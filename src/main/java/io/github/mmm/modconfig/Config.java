package io.github.mmm.modconfig;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config  {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> LIDAR1_SWITCH;
    public static final ForgeConfigSpec.ConfigValue<Float> LIDAR1_HORIZONTAL_SCANNING_RADIUS_IN_DEG;
    public static final ForgeConfigSpec.ConfigValue<Integer> LIDAR1_HORIZONTAL_SCANS_PER_RADIUS;
    public static final ForgeConfigSpec.ConfigValue<Float> LIDAR1_VERTICAL_SCANNING_RADIUS_IN_DEG;
    public static final ForgeConfigSpec.ConfigValue<Integer> LIDAR1_VERTICAL_SCANS_PER_RADIUS;
    public static final ForgeConfigSpec.ConfigValue<Float> LIDAR1_YAW_OFFSET_FROM_POV_IN_DEG;
    public static final ForgeConfigSpec.ConfigValue<Float> LIDAR1_PITCH_OFFSET_FROM_POV_IN_DEG;
    public static final ForgeConfigSpec.ConfigValue<Float> LIDAR1_ROLL_OFFSET_FROM_POV_IN_DEG;
    public static final ForgeConfigSpec.ConfigValue<Float> LIDAR1_MAXIMUM_MEASUREMENT_DISTANCE;

    public static final ForgeConfigSpec.ConfigValue<Boolean> LIDAR2_SWITCH;
    public static final ForgeConfigSpec.ConfigValue<Float> LIDAR2_HORIZONTAL_SCANNING_RADIUS_IN_DEG;
    public static final ForgeConfigSpec.ConfigValue<Integer> LIDAR2_HORIZONTAL_SCANS_PER_RADIUS;
    public static final ForgeConfigSpec.ConfigValue<Float> LIDAR2_VERTICAL_SCANNING_RADIUS_IN_DEG;
    public static final ForgeConfigSpec.ConfigValue<Integer> LIDAR2_VERTICAL_SCANS_PER_RADIUS;
    public static final ForgeConfigSpec.ConfigValue<Float> LIDAR2_YAW_OFFSET_FROM_POV_IN_DEG;
    public static final ForgeConfigSpec.ConfigValue<Float> LIDAR2_PITCH_OFFSET_FROM_POV_IN_DEG;
    public static final ForgeConfigSpec.ConfigValue<Float> LIDAR2_ROLL_OFFSET_FROM_POV_IN_DEG;
    public static final ForgeConfigSpec.ConfigValue<Float> LIDAR2_MAXIMUM_MEASUREMENT_DISTANCE;

    public static final ForgeConfigSpec.ConfigValue<Boolean> LIDAR3_SWITCH;
    public static final ForgeConfigSpec.ConfigValue<Float> LIDAR3_HORIZONTAL_SCANNING_RADIUS_IN_DEG;
    public static final ForgeConfigSpec.ConfigValue<Integer> LIDAR3_HORIZONTAL_SCANS_PER_RADIUS;
    public static final ForgeConfigSpec.ConfigValue<Float> LIDAR3_VERTICAL_SCANNING_RADIUS_IN_DEG;
    public static final ForgeConfigSpec.ConfigValue<Integer> LIDAR3_VERTICAL_SCANS_PER_RADIUS;
    public static final ForgeConfigSpec.ConfigValue<Float> LIDAR3_YAW_OFFSET_FROM_POV_IN_DEG;
    public static final ForgeConfigSpec.ConfigValue<Float> LIDAR3_PITCH_OFFSET_FROM_POV_IN_DEG;
    public static final ForgeConfigSpec.ConfigValue<Float> LIDAR3_ROLL_OFFSET_FROM_POV_IN_DEG;
    public static final ForgeConfigSpec.ConfigValue<Float> LIDAR3_MAXIMUM_MEASUREMENT_DISTANCE;

    public static final ForgeConfigSpec.ConfigValue<Boolean> IMU1_SWITCH;

    public static final ForgeConfigSpec.ConfigValue<Boolean> MULTITHREAD_SWITCH;
    public static final ForgeConfigSpec.ConfigValue<Integer> THREAD_COUNT_MULTIPLIER;
    public static final ForgeConfigSpec.ConfigValue<Integer> SAVE_INTERVAL;

    static {
        BUILDER.push("Configs for the Minecraft Measurement Mod");


        LIDAR1_SWITCH = BUILDER.comment("Activate/Deactivate LIDAR Sensor lidar1 (active = true, inactive = false)")
                .define("lidar1_switch", true);
        LIDAR1_HORIZONTAL_SCANNING_RADIUS_IN_DEG = BUILDER.comment("Horizontal Scanning Radius in Degrees for LIDAR Sensor lidar1")
                .define("lidar1_horizontal_scanning_radius_in_deg", (float)180.0);
        LIDAR1_HORIZONTAL_SCANS_PER_RADIUS = BUILDER.comment("Horizontal Scans per Radius for LIDAR Sensor lidar1")
                .define("lidar1_horizontal_scans_per_radius", 180);
        LIDAR1_VERTICAL_SCANNING_RADIUS_IN_DEG = BUILDER.comment("Vertical Scanning Radius in Degrees for LIDAR Sensor lidar1 (set it to 0 if Lidar should be 2D)")
                .define("lidar1_vertical_scanning_radius_in_deg", (float)60.0);
        LIDAR1_VERTICAL_SCANS_PER_RADIUS = BUILDER.comment("Vertical Scans per Radius for LIDAR Sensor lidar1 (set it to 0 if Lidar should be 2D)")
                .define("lidar1_vertical_scans_per_radius", 60);
        LIDAR1_YAW_OFFSET_FROM_POV_IN_DEG = BUILDER.comment("Yaw Offset from POV in Degrees for LIDAR Sensor lidar1")
                .define("lidar1_yaw_offset_from_pov_in_deg", (float)0.0);
        LIDAR1_PITCH_OFFSET_FROM_POV_IN_DEG = BUILDER.comment("Pitch Offset from POV in Degrees for LIDAR Sensor lidar1")
                .define("lidar1_pitch_offset_from_pov_in_deg", (float)0.0);
        LIDAR1_ROLL_OFFSET_FROM_POV_IN_DEG = BUILDER.comment("Roll Offset from POV in Degrees for LIDAR Sensor lidar1")
                .define("lidar1_roll_offset_from_pov_in_deg", (float)0.0);
        LIDAR1_MAXIMUM_MEASUREMENT_DISTANCE = BUILDER.comment("Maximum Measurement Distance in Meters for LIDAR Sensor lidar1")
                .define("lidar1_maximum_measurement_distance", (float)10.0);

        LIDAR2_SWITCH = BUILDER.comment("Activate/Deactivate LIDAR Sensor lidar2 (active = true, inactive = false)")
                .define("lidar2_switch", true);
        LIDAR2_HORIZONTAL_SCANNING_RADIUS_IN_DEG = BUILDER.comment("Horizontal Scanning Radius in Degrees for LIDAR Sensor lidar2")
                .define("lidar2_horizontal_scanning_radius_in_deg", (float)360.0);
        LIDAR2_HORIZONTAL_SCANS_PER_RADIUS = BUILDER.comment("Horizontal Scans per Radius for LIDAR Sensor lidar2")
                .define("lidar2_horizontal_scans_per_radius", 45);
        LIDAR2_VERTICAL_SCANNING_RADIUS_IN_DEG = BUILDER.comment("Vertical Scanning Radius in Degrees for LIDAR Sensor lidar2 (set it to 0 if Lidar should be 2D)")
                .define("lidar2_vertical_scanning_radius_in_deg", (float)0.0);
        LIDAR2_VERTICAL_SCANS_PER_RADIUS = BUILDER.comment("Vertical Scans per Radius for LIDAR Sensor lidar2 (set it to 0 if Lidar should be 2D)")
                .define("lidar2_vertical_scans_per_radius", 0);
        LIDAR2_YAW_OFFSET_FROM_POV_IN_DEG = BUILDER.comment("Yaw Offset from POV in Degrees for LIDAR Sensor lidar2")
                .define("lidar2_yaw_offset_from_pov_in_deg", (float)0.0);
        LIDAR2_PITCH_OFFSET_FROM_POV_IN_DEG = BUILDER.comment("Pitch Offset from POV in Degrees for LIDAR Sensor lidar2")
                .define("lidar2_pitch_offset_from_pov_in_deg", (float)0.0);
        LIDAR2_ROLL_OFFSET_FROM_POV_IN_DEG = BUILDER.comment("Roll Offset from POV in Degrees for LIDAR Sensor lidar2")
                .define("lidar2_roll_offset_from_pov_in_deg", (float)0.0);
        LIDAR2_MAXIMUM_MEASUREMENT_DISTANCE = BUILDER.comment("Maximum Measurement Distance in Meters for LIDAR Sensor lidar2")
                .define("lidar2_maximum_measurement_distance", (float)10.0);

        LIDAR3_SWITCH = BUILDER.comment("Activate/Deactivate LIDAR Sensor lidar3 (active = true, inactive = false)")
                .define("lidar3_switch", true);
        LIDAR3_HORIZONTAL_SCANNING_RADIUS_IN_DEG = BUILDER.comment("Horizontal Scanning Radius in Degrees for LIDAR Sensor lidar3")
                .define("lidar3_horizontal_scanning_radius_in_deg", (float)360);
        LIDAR3_HORIZONTAL_SCANS_PER_RADIUS = BUILDER.comment("Horizontal Scans per Radius for LIDAR Sensor lidar3")
                .define("lidar3_horizontal_scans_per_radius", 180);
        LIDAR3_VERTICAL_SCANNING_RADIUS_IN_DEG = BUILDER.comment("Vertical Scanning Radius in Degrees for LIDAR Sensor lidar3 (set it to 0 if Lidar should be 2D)")
                .define("lidar3_vertical_scanning_radius_in_deg", (float)0.0);
        LIDAR3_VERTICAL_SCANS_PER_RADIUS = BUILDER.comment("Vertical Scans per Radius for LIDAR Sensor lidar3 (set it to 0 if Lidar should be 2D)")
                .define("lidar3_vertical_scans_per_radius", 0);
        LIDAR3_YAW_OFFSET_FROM_POV_IN_DEG = BUILDER.comment("Yaw Offset from POV in Degrees for LIDAR Sensor lidar3")
                .define("lidar3_yaw_offset_from_pov_in_deg", (float)0.0);
        LIDAR3_PITCH_OFFSET_FROM_POV_IN_DEG = BUILDER.comment("Pitch Offset from POV in Degrees for LIDAR Sensor lidar3")
                .define("lidar3_pitch_offset_from_pov_in_deg", (float)0.0);
        LIDAR3_ROLL_OFFSET_FROM_POV_IN_DEG = BUILDER.comment("Roll Offset from POV in Degrees for LIDAR Sensor lidar3")
                .define("lidar3_roll_offset_from_pov_in_deg", (float)0.0);
        LIDAR3_MAXIMUM_MEASUREMENT_DISTANCE = BUILDER.comment("Maximum Measurement Distance in Meters for LIDAR Sensor lidar3")
                .define("lidar3_maximum_measurement_distance", (float)10.0);

        IMU1_SWITCH = BUILDER.comment("Activate/Deactivate IMU Sensor imu1 (active = true, inactive = false)")
                .define("imu1_switch", true);

        MULTITHREAD_SWITCH = BUILDER.comment("Activate/Deactivate Multithreading (active = true, inactive = false)")
                .define("multithread_switch", true);
        THREAD_COUNT_MULTIPLIER = BUILDER.comment("Multiplier for the amount of Threads. Default is 1, resulting in 1*LOGICAL_CORES_AMOUNT Threads")
                .define("thread_count_multiplier", 1);
        SAVE_INTERVAL = BUILDER.comment("Interval in ticks (1 tick usually takes 50ms) in which the data is saved to the file")
                .define("save_interval", 1);


        BUILDER.pop();
        SPEC = BUILDER.build();
    }

}
