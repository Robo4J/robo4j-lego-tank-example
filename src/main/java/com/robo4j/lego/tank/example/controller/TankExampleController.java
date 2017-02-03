package com.robo4j.lego.tank.example.controller;

import com.robo4j.core.ConfigurationException;
import com.robo4j.core.RoboContext;
import com.robo4j.core.RoboResult;
import com.robo4j.core.RoboUnit;
import com.robo4j.core.configuration.Configuration;
import com.robo4j.units.lego.enums.LegoPlatformMessageTypeEnum;
import com.robo4j.units.lego.platform.LegoPlatformMessage;

/**
 * This controller binds together {@link SimpleTankUnit}, {@link HttpUnit} and
 * {@link LegoButtonUnit} to provide a demo.
 *
 * @author Marcus Hirt (@hirt)
 * @author Miro Wengner (@miragemiko)
 * @since 30.01.2017
 */
public class TankExampleController extends RoboUnit<String> {

    private String target;

    public TankExampleController(RoboContext context, String id) {
        super(context, id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public RoboResult<String, ?> onMessage(Object message) {

        if (message instanceof LegoPlatformMessageTypeEnum) {
            LegoPlatformMessageTypeEnum myMessage = (LegoPlatformMessageTypeEnum) message;
            processPlatformMessage(myMessage);
        }
        if (message instanceof String) {
            LegoPlatformMessageTypeEnum myMessage = LegoPlatformMessageTypeEnum.getByText(message.toString());
            processPlatformMessage(myMessage);
        }

        return null;
    }


    @Override
    protected void onInitialization(Configuration configuration) throws ConfigurationException {
        target = configuration.getString("target", null);
        if (target == null) {
            throw ConfigurationException.createMissingConfigNameException("target");
        }
    }

    //Private Methods
    private void sendTankMessage(RoboContext ctx, LegoPlatformMessage message){
         ctx.getReference(target).sendMessage(message);
    }

    private void processPlatformMessage(LegoPlatformMessageTypeEnum myMessage){
        sendTankMessage(getContext(), new LegoPlatformMessage(myMessage));
    }
}
