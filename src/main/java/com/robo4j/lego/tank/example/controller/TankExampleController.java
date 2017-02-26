package com.robo4j.lego.tank.example.controller;

import java.util.Collection;
import java.util.Collections;

import com.robo4j.core.AttributeDescriptor;
import com.robo4j.core.ConfigurationException;
import com.robo4j.core.DefaultAttributeDescriptor;
import com.robo4j.core.RoboContext;
import com.robo4j.core.RoboUnit;
import com.robo4j.core.configuration.Configuration;
import com.robo4j.units.lego.enums.LegoPlatformMessageTypeEnum;
import com.robo4j.units.lego.platform.LegoPlatformMessage;

/**
 * Simple Lego Tank Example
 *
 * @author Marcus Hirt (@hirt)
 * @author Miro Wengner (@miragemiko)
 */
public class TankExampleController extends RoboUnit<LegoPlatformMessageTypeEnum> {

	private static final String ATTRIBUTE_NAME_BUTTONS = "button";
	private final static Collection<AttributeDescriptor<?>> KNOWN_ATTRIBUTES = Collections.unmodifiableCollection(Collections
			.singleton(DefaultAttributeDescriptor.create(LegoPlatformMessageTypeEnum.class, ATTRIBUTE_NAME_BUTTONS)));


	private String target;

	public TankExampleController(RoboContext context, String id) {
		super(LegoPlatformMessageTypeEnum.class, context, id);
	}

	/**
	 *
	 * @param message
	 *            accepted message
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onMessage(LegoPlatformMessageTypeEnum message) {
		processPlatformMessage(message);
	}

	/**
	 *
	 * @param configuration
	 *            desired configuration
	 * @throws ConfigurationException
	 */
	@Override
	protected void onInitialization(Configuration configuration) throws ConfigurationException {
		target = configuration.getString("target", null);
		if (target == null) {
			throw ConfigurationException.createMissingConfigNameException("target");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <R> R getMessageAttribute(AttributeDescriptor<R> descriptor, String value) {
		return descriptor != null ? (R) LegoPlatformMessageTypeEnum.getInternalByName(value) : null;
	}

	@Override
	public Collection<AttributeDescriptor<?>> getKnownAttributes() {
		return KNOWN_ATTRIBUTES;
	}

	// Private Methods
	private void sendTankMessage(RoboContext ctx, LegoPlatformMessage message) {
		ctx.getReference(target).sendMessage(message);
	}

	private void processPlatformMessage(LegoPlatformMessageTypeEnum myMessage) {
		sendTankMessage(getContext(), new LegoPlatformMessage(myMessage));
	}
}
