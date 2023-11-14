package io.github.mmm.modconfig;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config  {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> LIDAR1_SWITCH;
    public static final ForgeConfigSpec.ConfigValue<Boolean> LIDAR2_SWITCH;
    public static final ForgeConfigSpec.ConfigValue<Boolean> LIDAR3_SWITCH;
    public static final ForgeConfigSpec.ConfigValue<Boolean> IMU1_SWITCH;

    public static final ForgeConfigSpec.ConfigValue<Boolean> MULTITHREAD_SWITCH;
    public static final ForgeConfigSpec.ConfigValue<Integer> THREAD_COUNT_MULTIPLIER;

    static {
        BUILDER.push("Configs for the Minecraft Measurement Mod");


        LIDAR1_SWITCH = BUILDER.comment("Activate/Deactivate LIDAR Sensor lidar1 (active = true, inactive = false)")
                .define("lidar1_switch", true);
        LIDAR2_SWITCH = BUILDER.comment("Activate/Deactivate LIDAR Sensor lidar2 (active = true, inactive = false)")
                .define("lidar2_switch", true);
        LIDAR3_SWITCH = BUILDER.comment("Activate/Deactivate LIDAR Sensor lidar3 (active = true, inactive = false)")
                .define("lidar3_switch", true);
        IMU1_SWITCH = BUILDER.comment("Activate/Deactivate IMU Sensor imu1 (active = true, inactive = false)")
                .define("imu1_switch", true);

        MULTITHREAD_SWITCH = BUILDER.comment("Activate/Deactivate Multithreading (active = true, inactive = false)")
                .define("multithread_switch", true);
        THREAD_COUNT_MULTIPLIER = BUILDER.comment("Multiplier for the amount of Threads. Default is 1, resulting in 1*LOGICAL_CORES_AMOUNT Threads")
                .define("thread_count_multiplier", 1);


        BUILDER.pop();
        SPEC = BUILDER.build();
    }

}
