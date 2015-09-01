package com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.events;

import com.bitdubai.fermat_api.layer.all_definition.event.EventSource;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.enums.EventType;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.interfaces.PlatformEvent;

/**
 * Created by natalia on 17/08/15.
 */
public class IntraUserActorRequestConnectionEvent implements PlatformEvent {
    private EventType eventType;
    private EventSource eventSource;
    private  String intraUserLoggedInPublicKey;
    private String intraUserToAddPublicKey;
    private String intraUserToAddName;
    private byte[] profileImage;

    public IntraUserActorRequestConnectionEvent(EventType eventType){
        this.eventType = eventType;
    }

    /**
     * Constructor with parameters
     *
     * @param eventType
     * @param intraUserLoggedInPublicKey
     * @param intraUserToAddPublicKey
     */
    public IntraUserActorRequestConnectionEvent(EventType eventType, String intraUserLoggedInPublicKey,String intraUserToAddName, String intraUserToAddPublicKey, byte[] profileImage) {

        this.eventType = eventType;
        this.intraUserLoggedInPublicKey = intraUserLoggedInPublicKey;
        this.intraUserToAddPublicKey = intraUserToAddPublicKey;
        this.intraUserToAddName = intraUserToAddName;
        this.profileImage = profileImage;
    }
    @Override
    public EventType getEventType() {
        return this.eventType;
    }

    @Override
    public void setSource(EventSource eventSource) {
        this.eventSource = eventSource;
    }

    @Override
    public EventSource getSource() {
        return this.eventSource;
    }

    /**
     *Return the public key of intra user logged
     * @return String intra User Logged In PublicKey
     */
    public String getIntraUserLoggedInPublicKey() {
        return this.intraUserLoggedInPublicKey;
    }

    /**
     * Return the public key of intra user actor to add
     * @return String intra User To Add PublicKey
     */
    public String getIntraUserToAddPublicKey() {
        return this.intraUserToAddPublicKey;
    }

    /**
     *Return the name of intra user actor to add
     * @return String name
     */
    public String getIntraUserToAddName() {
        return this.intraUserToAddName;
    }

    /**
     * Return the profile image of intra user actor to add
     * @return byte image
     */
    public byte[] getIntraUserProfileImage() {
        return this.profileImage;
    }
}