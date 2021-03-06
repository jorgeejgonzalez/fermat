package com.bitdubai.fermat_dap_plugin.layer.actor.network.service.asset.redeem_point.developer.bitdubai.version_1.event_handlers;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.events.EventSource;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEvent;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEventHandler;
import com.bitdubai.fermat_dap_api.layer.all_definition.enums.EventType;
import com.bitdubai.fermat_dap_api.layer.all_definition.events.NewReceiveMessageActorNotificationEvent;
import com.bitdubai.fermat_dap_api.layer.all_definition.network_service_message.DAPMessage;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.JsonANSNamesConstants;
import com.bitdubai.fermat_dap_plugin.layer.actor.network.service.asset.redeem_point.developer.bitdubai.version_1.AssetRedeemPointActorNetworkServicePluginRoot;
import com.bitdubai.fermat_dap_plugin.layer.actor.network.service.asset.redeem_point.developer.bitdubai.version_1.communications.CommunicationNetworkServiceConnectionManager;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.events.NewNetworkServiceMessageReceivedNotificationEvent;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.contents.FermatMessage;
import com.bitdubai.fermat_pip_api.layer.platform_service.event_manager.interfaces.EventManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Created by Nerio on 26/11/15.
 */
public class NewReceiveMessagesNotificationEventHandler implements FermatEventHandler {

    private EventManager eventManager;
    private CommunicationNetworkServiceConnectionManager communicationNetworkServiceConnectionManager;

    public NewReceiveMessagesNotificationEventHandler(CommunicationNetworkServiceConnectionManager communicationNetworkServiceConnectionManager, EventManager eventManager) {
        this.communicationNetworkServiceConnectionManager = communicationNetworkServiceConnectionManager;
        this.eventManager = eventManager;
    }

    @Override
    public void handleEvent(FermatEvent platformEvent) throws FermatException {

        if (platformEvent.getSource() == AssetRedeemPointActorNetworkServicePluginRoot.EVENT_SOURCE) {

            System.out.println("ACTOR NETWORK SERVICE ASSET REDEEM POINT - NOTIFICACION EVENTO MENSAJE RECIBIDO!!!!");

            NewNetworkServiceMessageReceivedNotificationEvent newNetworkServiceMessageReceivedNotificationEvent = (NewNetworkServiceMessageReceivedNotificationEvent) platformEvent;
            FermatMessage fermatMessageReceive = (FermatMessage) newNetworkServiceMessageReceivedNotificationEvent.getData();

            Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(fermatMessageReceive.getContent()).getAsJsonObject();
            CharSequence pblicKeyExtended = JsonANSNamesConstants.PUBLICKEY_EXTENDED;


            if (fermatMessageReceive.getContent() != null) {

                if (fermatMessageReceive.getContent().contains(pblicKeyExtended)) {

                    DAPMessage message = gson.fromJson(jsonObject.get(JsonANSNamesConstants.PUBLICKEY_EXTENDED).getAsString(), DAPMessage.class);

                    System.out.println("ACTOR NETWORK SERVICE ASSET REDEEM POINT - SE LANZARA EVENTO PARA REQUEST PUBLIC KEY EXTENDED");

                    communicationNetworkServiceConnectionManager.getIncomingMessageDao().create(fermatMessageReceive);
                    FermatEvent event = eventManager.getNewEvent(EventType.NEW_RECEIVE_MESSAGE_ACTOR);
                    event.setSource(EventSource.NETWORK_SERVICE_ACTOR_ASSET_REDEEM_POINT);
                    ((NewReceiveMessageActorNotificationEvent) event).setNewReceiveMessage(message);
                    eventManager.raiseEvent(event);

                }

            }
        }
    }
}
