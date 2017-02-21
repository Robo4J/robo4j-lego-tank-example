package com.robo4j.lego.tank.example;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.robo4j.core.ConfigurationException;
import com.robo4j.core.RoboBuilder;
import com.robo4j.core.RoboBuilderException;
import com.robo4j.core.RoboContext;
import com.robo4j.core.RoboSystem;
import com.robo4j.core.client.util.RoboClassLoader;
import com.robo4j.core.client.util.RoboHttpUtils;
import com.robo4j.core.configuration.Configuration;
import com.robo4j.core.configuration.ConfigurationFactory;
import com.robo4j.core.httpunit.HttpServerUnit;
import com.robo4j.core.util.SystemUtil;

/**
 * Robo4J Tank example test focused on HttpUnit
 *
 * @author Marcus Hirt (@hirt)
 * @author Miro Wengner (@miragemiko)
 * @since 01.02.2017
 */
public class TankHttpUnitTest {

	@Test
	public void testProgrammatically() throws ConfigurationException, IOException {
		RoboSystem system = new RoboSystem();
		Configuration config = ConfigurationFactory.createEmptyConfiguration();

		HttpServerUnit http = new HttpServerUnit(system, "http");
		config.setString("target", "");
		config.setInteger("port", TankExampleMain.PORT);
		/* specific configuration */
		Configuration targetUnits = config.createChildConfiguration(RoboHttpUtils.HTTP_TARGET_UNITS);
		targetUnits.setString("controller", "GET");

		http.initialize(config);

		system.addUnits(http);
		system.start();

		printSystem(system);

		System.out.println("Press Going Down!");
		system.shutdown();
		System.out.println("System is Down!");
		Assert.assertNotNull(system.getUnits());
		Assert.assertEquals(system.getUnits().size(), 1);

	}

	@Test
	public void testDeclarative() throws RoboBuilderException {
		RoboBuilder builder = new RoboBuilder().add(RoboClassLoader.getInstance().getResource("robo4j_http.xml"));
		RoboContext ctx = builder.build();
		ctx.start();

		printSystem(ctx);
		System.out.println("Press Going Down!");
		ctx.stop();
		ctx.shutdown();
		System.out.println("System is Down!");
		Assert.assertNotNull(ctx.getUnits());
		Assert.assertEquals(ctx.getUnits().size(), 1);
	}

	// Private Methods
	private void printSystem(final RoboContext system) {
		System.out.println("State before start:");
		System.out.println(SystemUtil.generateStateReport(system));
		system.start();

		System.out.println("State after start:");
		System.out.println(SystemUtil.generateStateReport(system));

		System.out.println("RoboSystem http server\n\tPort:" + TankExampleMain.PORT + "\n");
		System.out.println(
				"Usage:\n\tRequest GET: http://<IP_ADDRESS>:" + TankExampleMain.PORT + "/tank?&command=stop");
		System.out.println("\tRequest command types: stop, move, back, left, right\n");
	}

}
