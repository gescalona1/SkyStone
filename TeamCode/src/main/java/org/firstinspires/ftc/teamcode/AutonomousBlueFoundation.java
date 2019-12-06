package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.drive.Direction;

@Autonomous(name = "AutoBlueFoundation")
public class AutonomousBlueFoundation extends AutoPart1{
    @Override
    public void beforeLoop(){

    }

    @Override
    public void run(){
        DeviceMap map = DeviceMap.getInstance();
        map.getLeftBat().setPosition(1);
        map.getRightBat().setPosition(0);
        driver.move(Direction.BACKWARD, 0.3, 30);
        map.getLeftBat().setPosition(0);
        map.getRightBat().setPosition(1);
        sleep(1000);
        driver.move(Direction.FORWARD, 0.7, 50);
        driver.turn(1.0, 90);
        map.getLeftBat().setPosition(1);
        map.getRightBat().setPosition(0);
        driver.move(Direction.RIGHT, 0.7, 20);
        driver.move(Direction.BACKWARD, 0.3, 10);
        driver.move(Direction.FORWARD, 0.7, 30);
    }

}
