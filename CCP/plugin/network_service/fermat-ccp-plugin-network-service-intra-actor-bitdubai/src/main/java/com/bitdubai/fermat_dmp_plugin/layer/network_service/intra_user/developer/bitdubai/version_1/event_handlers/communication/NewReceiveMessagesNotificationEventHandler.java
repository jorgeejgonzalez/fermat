package com.bitdubai.fermat_dmp_plugin.layer.network_service.intra_user.developer.bitdubai.version_1.event_handlers.communication;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEvent;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEventHandler;
import com.bitdubai.fermat_dmp_plugin.layer.network_service.intra_user.developer.bitdubai.version_1.structure.ActorNetworkServiceRecordedAgent;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.events.NewNetworkServiceMessageReceivedNotificationEvent;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.contents.FermatMessage;

/**
 * Created by mati on 2015.10.09..
 */
public class NewReceiveMessagesNotificationEventHandler implements FermatEventHandler {


    private final ActorNetworkServiceRecordedAgent cryptoTransmissionAgent;

    /**
     * Constructor with parameter
     *
     * @param
     */
    public NewReceiveMessagesNotificationEventHandler(ActorNetworkServiceRecordedAgent cryptoTransmissionAgent) {
        this.cryptoTransmissionAgent = cryptoTransmissionAgent;
    }

    /**
     * (non-Javadoc)
     *
     * @see FermatEventHandler#handleEvent(FermatEvent)
     *
     * @param platformEvent
     * @throws Exception
     */
    @Override
    public void handleEvent(FermatEvent platformEvent) throws FermatException {

        System.out.println("CompleteComponentConnectionRequestNotificationEventHandler - handleEvent platformEvent =" + platformEvent.toString());

        System.out.print("NOTIFICACION EVENTO LLEGADA MENSAJE!!!!");


        if (this.cryptoTransmissionAgent.isRunning()) {

            NewNetworkServiceMessageReceivedNotificationEvent newNetworkServiceMessageSentNotificationEvent = (NewNetworkServiceMessageReceivedNotificationEvent) platformEvent;

            cryptoTransmissionAgent.handleNewMessages((FermatMessage)newNetworkServiceMessageSentNotificationEvent.getData());


        }
    }

}
