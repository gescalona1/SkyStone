package org.firstinspires.ftc.teamcode.drive;

import com.qualcomm.hardware.bosch.BNO055IMUImpl;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

public class UltroImu extends BNO055IMUImpl {
    public UltroImu(I2cDeviceSynch deviceClient) {
        super(deviceClient);
    }

    @Override
    public String getDeviceName() {
        return null;
    }

    @Override
    public Manufacturer getManufacturer() {
        return null;
    }
}
