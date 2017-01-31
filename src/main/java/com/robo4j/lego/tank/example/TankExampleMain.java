package com.robo4j.lego.tank.example;

import com.robo4j.core.RoboSystem;
import com.robo4j.core.configuration.Configuration;
import com.robo4j.core.configuration.ConfigurationFactory;
import com.robo4j.core.unit.HttpUnit;
import com.robo4j.core.util.SystemUtil;
import com.robo4j.lego.tank.example.controller.TankExampleController;
import com.robo4j.units.lego.SimpleTankUnit;

/**
 * @author Marcus Hirt (@hirt)
 * @author Miro Wengner (@miragemiko)
 * @since 30.01.2017
 */
public class TankExampleMain {

    private static int PORT = 8025;

    public static void main(String[] args) throws Exception{
        RoboSystem system = new RoboSystem();
        Configuration config = ConfigurationFactory.createEmptyConfiguration();

        HttpUnit http = new HttpUnit(system, "http");
        config.setString("target", "controller");
        config.setInteger("port", PORT);
        http.initialize(config);

        TankExampleController ctrl = new TankExampleController(system, "controller");
        config = ConfigurationFactory.createEmptyConfiguration();
        config.setString("target", "platform");
        ctrl.initialize(config);

        SimpleTankUnit platform = new SimpleTankUnit(system, "platform");
        config = ConfigurationFactory.createEmptyConfiguration();
        config.setString("leftMotorPort", "B");
        config.setCharacter("leftMotorType", 'N');
        config.setString("rightMotorPort", "C");
        config.setCharacter("rightMotorType", 'N');
        platform.initialize(config);

        system.addUnits(http, ctrl, platform);

        System.out.println("State before start:");
        System.out.println(SystemUtil.generateStateReport(system));
        system.start();

        System.out.println("State after start:");
        System.out.println(SystemUtil.generateStateReport(system));

        system.getReference("platform").sendMessage("stop");

        System.out.println("RoboSystem http server\n\tPort:" + PORT + "\n");
        System.out.println("Usage:\n\tRequest GET: http://<IP_ADDRESS>:" + PORT + "?type=tank&command=stop");
        System.out.println("\tRequest command types: stop, move, back, left, right\n");

        System.out.println("Press enter to quit!");
        System.in.read();
        system.shutdown();


    }

}
