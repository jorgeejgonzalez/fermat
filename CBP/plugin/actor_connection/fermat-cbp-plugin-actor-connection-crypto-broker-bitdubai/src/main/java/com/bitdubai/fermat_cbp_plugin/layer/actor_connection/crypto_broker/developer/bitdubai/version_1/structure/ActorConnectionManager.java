package com.bitdubai.fermat_cbp_plugin.layer.actor_connection.crypto_broker.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.layer.actor_connection.common.enums.ConnectionState;
import com.bitdubai.fermat_api.layer.actor_connection.common.exceptions.ActorConnectionAlreadyExistsException;
import com.bitdubai.fermat_api.layer.actor_connection.common.exceptions.ActorConnectionNotFoundException;
import com.bitdubai.fermat_api.layer.actor_connection.common.exceptions.CantAcceptActorConnectionRequestException;
import com.bitdubai.fermat_api.layer.actor_connection.common.exceptions.CantCancelActorConnectionRequestException;
import com.bitdubai.fermat_api.layer.actor_connection.common.exceptions.CantChangeActorConnectionStateException;
import com.bitdubai.fermat_api.layer.actor_connection.common.exceptions.CantDenyActorConnectionRequestException;
import com.bitdubai.fermat_api.layer.actor_connection.common.exceptions.CantDisconnectFromActorException;
import com.bitdubai.fermat_api.layer.actor_connection.common.exceptions.CantGetConnectionStateException;
import com.bitdubai.fermat_api.layer.actor_connection.common.exceptions.CantRegisterActorConnectionException;
import com.bitdubai.fermat_api.layer.actor_connection.common.exceptions.CantRequestActorConnectionException;
import com.bitdubai.fermat_api.layer.actor_connection.common.exceptions.ConnectionAlreadyRequestedException;
import com.bitdubai.fermat_api.layer.actor_connection.common.exceptions.UnexpectedConnectionStateException;
import com.bitdubai.fermat_api.layer.actor_connection.common.exceptions.UnsupportedActorTypeException;
import com.bitdubai.fermat_api.layer.actor_connection.common.structure_common_classes.ActorIdentityInformation;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginVersionReference;
import com.bitdubai.fermat_cbp_api.layer.actor_connection.crypto_broker.interfaces.CryptoBrokerActorConnectionManager;
import com.bitdubai.fermat_cbp_api.layer.actor_connection.crypto_broker.interfaces.CryptoBrokerActorConnectionSearch;
import com.bitdubai.fermat_cbp_api.layer.actor_connection.crypto_broker.utils.CryptoBrokerActorConnection;
import com.bitdubai.fermat_cbp_api.layer.actor_connection.crypto_broker.utils.CryptoBrokerLinkedActorIdentity;
import com.bitdubai.fermat_cbp_api.layer.actor_network_service.crypto_broker.exceptions.CantAcceptConnectionRequestException;
import com.bitdubai.fermat_cbp_api.layer.actor_network_service.crypto_broker.exceptions.CantCancelConnectionRequestException;
import com.bitdubai.fermat_cbp_api.layer.actor_network_service.crypto_broker.exceptions.CantDenyConnectionRequestException;
import com.bitdubai.fermat_cbp_api.layer.actor_network_service.crypto_broker.exceptions.CantDisconnectException;
import com.bitdubai.fermat_cbp_api.layer.actor_network_service.crypto_broker.exceptions.CantRequestConnectionException;
import com.bitdubai.fermat_cbp_api.layer.actor_network_service.crypto_broker.exceptions.ConnectionRequestNotFoundException;
import com.bitdubai.fermat_cbp_api.layer.actor_network_service.crypto_broker.interfaces.CryptoBrokerManager;
import com.bitdubai.fermat_cbp_api.layer.actor_network_service.crypto_broker.utils.CryptoBrokerConnectionInformation;
import com.bitdubai.fermat_cbp_plugin.layer.actor_connection.crypto_broker.developer.bitdubai.version_1.database.CryptoBrokerActorConnectionDao;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;

import java.util.UUID;

/**
 * The class <code>com.bitdubai.fermat_cbp_plugin.layer.actor_connection.crypto_customer.developer.bitdubai.version_1.structure.ActorConnectionManager</code>
 * bla bla bla.
 * <p>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 21/11/2015.
 */
public class ActorConnectionManager implements CryptoBrokerActorConnectionManager {

    private final CryptoBrokerManager            cryptoBrokerNetworkService;
    private final CryptoBrokerActorConnectionDao dao                       ;
    private final ErrorManager                   errorManager              ;
    private final PluginVersionReference         pluginVersionReference    ;

    public ActorConnectionManager(final CryptoBrokerManager            cryptoBrokerNetworkService,
                                  final CryptoBrokerActorConnectionDao dao                       ,
                                  final ErrorManager                   errorManager              ,
                                  final PluginVersionReference         pluginVersionReference    ) {

        this.cryptoBrokerNetworkService = cryptoBrokerNetworkService;
        this.dao                        = dao                       ;
        this.errorManager               = errorManager              ;
        this.pluginVersionReference     = pluginVersionReference    ;
    }

    @Override
    public CryptoBrokerActorConnectionSearch getSearch(CryptoBrokerLinkedActorIdentity actorIdentitySearching) {

        return new ActorConnectionSearch(actorIdentitySearching, dao);
    }

    @Override
    public void requestConnection(final ActorIdentityInformation actorSending  ,
                                  final ActorIdentityInformation actorReceiving) throws CantRequestActorConnectionException,
                                                                                        UnsupportedActorTypeException      ,
                                                                                        ConnectionAlreadyRequestedException {

        try {

            final UUID newConnectionId = UUID.randomUUID();
            final CryptoBrokerLinkedActorIdentity linkedIdentity = new CryptoBrokerLinkedActorIdentity(actorSending.getPublicKey());
            final ConnectionState connectionState = ConnectionState.PENDING_REMOTELY_ACCEPTANCE;
            final long currentTime = System.currentTimeMillis();

            final CryptoBrokerActorConnection actorConnection = new CryptoBrokerActorConnection(
                    newConnectionId              ,
                    linkedIdentity               ,
                    actorReceiving.getPublicKey(),
                    actorReceiving.getActorType(),
                    actorReceiving.getAlias()    ,
                    actorReceiving.getImage()    ,
                    connectionState              ,
                    currentTime                  ,
                    currentTime
            );

            switch(actorReceiving.getActorType()) {
                case CBP_CRYPTO_BROKER:

                    dao.registerActorConnection(actorConnection);

                    final CryptoBrokerConnectionInformation connectionInformation = new CryptoBrokerConnectionInformation(
                            actorSending.getPublicKey()  ,
                            actorSending.getActorType()  ,
                            actorSending.getAlias()      ,
                            actorSending.getImage()      ,
                            actorReceiving.getPublicKey(),
                            currentTime
                    );

                    cryptoBrokerNetworkService.requestConnection(connectionInformation);
                    break;
                default:
                    throw new UnsupportedActorTypeException("actorSending: "+actorSending + " - actorReceiving: "+actorReceiving, "Unsupported actor type exception.");
            }

        } catch (final UnsupportedActorTypeException unsupportedActorTypeException) {

            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, unsupportedActorTypeException);
            throw unsupportedActorTypeException;

        } catch (final ActorConnectionAlreadyExistsException actorConnectionAlreadyExistsException) {

            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, actorConnectionAlreadyExistsException);
            throw new ConnectionAlreadyRequestedException(actorConnectionAlreadyExistsException, "actorSending: "+actorSending + " - actorReceiving: "+actorReceiving, "The connection was already requested or exists.");
        } catch (final CantRegisterActorConnectionException cantRegisterActorConnectionException) {

            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, cantRegisterActorConnectionException);
            throw new CantRequestActorConnectionException(cantRegisterActorConnectionException, "actorSending: "+actorSending + " - actorReceiving: "+actorReceiving, "Problem registering the actor connection in DAO.");
        } catch (final CantRequestConnectionException cantRequestConnectionException) {

            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, cantRequestConnectionException);
            throw new CantRequestActorConnectionException(cantRequestConnectionException, "actorSending: "+actorSending + " - actorReceiving: "+actorReceiving, "Error trying to request the connection through the network service.");
        } catch (final Exception exception) {

            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, exception);
            throw new CantRequestActorConnectionException(exception, "actorSending: "+actorSending + " - actorReceiving: "+actorReceiving, "Unhandled error.");
        }

    }

    @Override
    public void disconnect(final UUID connectionId) throws CantDisconnectFromActorException   ,
                                                           ActorConnectionNotFoundException   ,
                                                           UnexpectedConnectionStateException {

        try {

            ConnectionState currentConnectionState = dao.getConnectionState(connectionId);

            switch (currentConnectionState) {

                case DISCONNECTED_LOCALLY:
                    // no action needed
                    break;

                case CONNECTED:

                    // disconnect from an actor through network service and after that mark as DISCONNECTED LOCALLY
                    // TODO HAVE IN COUNT THAT YOU HAVE TO DISCONNECT FROM AN SPECIFIC TYPE OF ACTOR,.
                    cryptoBrokerNetworkService.disconnect(connectionId);

                    dao.changeConnectionState(
                            connectionId,
                            ConnectionState.DISCONNECTED_LOCALLY
                    );

                    break;

                default:
                    throw new UnexpectedConnectionStateException("connectionId: "+connectionId + " - currentConnectionState: "+currentConnectionState, "Unexpected contact state for denying.");
            }

        } catch (final ActorConnectionNotFoundException | UnexpectedConnectionStateException innerException) {

            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, innerException);
            throw innerException;
        } catch (final CantGetConnectionStateException cantGetConnectionStateException) {

            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, cantGetConnectionStateException);
            throw new CantDisconnectFromActorException(cantGetConnectionStateException, "connectionId: "+connectionId, "Error trying to get the connection state.");
        } catch (final CantDisconnectException | ConnectionRequestNotFoundException networkServiceException ) {

            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, networkServiceException);
            throw new CantDisconnectFromActorException(networkServiceException, "connectionId: "+connectionId, "Error trying to disconnect from an actor through the network service.");
        } catch (final CantChangeActorConnectionStateException cantChangeActorConnectionStateException ) {

            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, cantChangeActorConnectionStateException);
            throw new CantDisconnectFromActorException(cantChangeActorConnectionStateException, "connectionId: "+connectionId, "Error trying to change the actor connection state.");
        } catch (final Exception exception) {

            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, exception);
            throw new CantDisconnectFromActorException(exception, "connectionId: "+connectionId, "Unhandled error.");
        }
    }

    @Override
    public void denyConnection(final UUID connectionId) throws CantDenyActorConnectionRequestException,
                                                               ActorConnectionNotFoundException       ,
                                                               UnexpectedConnectionStateException     {

        try {

            ConnectionState currentConnectionState = dao.getConnectionState(connectionId);

            switch (currentConnectionState) {

                case DENIED_LOCALLY:
                    // no action needed
                    break;

                case PENDING_LOCALLY_ACCEPTANCE:

                    // deny connection through network service and after that mark as DENIED LOCALLY
                    cryptoBrokerNetworkService.denyConnection(connectionId);

                    dao.changeConnectionState(
                            connectionId,
                            ConnectionState.DENIED_LOCALLY
                    );

                    break;

                default:
                    throw new UnexpectedConnectionStateException("connectionId: "+connectionId + " - currentConnectionState: "+currentConnectionState, "Unexpected contact state for denying.");
            }

        } catch (final ActorConnectionNotFoundException | UnexpectedConnectionStateException innerException) {

            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, innerException);
            throw innerException;
        } catch (final CantGetConnectionStateException cantGetConnectionStateException) {

            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, cantGetConnectionStateException);
            throw new CantDenyActorConnectionRequestException(cantGetConnectionStateException, "connectionId: "+connectionId, "Error trying to get the connection state.");
        } catch (final CantDenyConnectionRequestException | ConnectionRequestNotFoundException networkServiceException ) {

            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, networkServiceException);
            throw new CantDenyActorConnectionRequestException(networkServiceException, "connectionId: "+connectionId, "Error trying to deny the connection through the network service.");
        } catch (final CantChangeActorConnectionStateException cantChangeActorConnectionStateException ) {

            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, cantChangeActorConnectionStateException);
            throw new CantDenyActorConnectionRequestException(cantChangeActorConnectionStateException, "connectionId: "+connectionId, "Error trying to change the actor connection state.");
        } catch (final Exception exception) {

            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, exception);
            throw new CantDenyActorConnectionRequestException(exception, "connectionId: "+connectionId, "Unhandled error.");
        }

    }

    @Override
    public void cancelConnection(final UUID connectionId) throws CantCancelActorConnectionRequestException,
                                                                 ActorConnectionNotFoundException         ,
                                                                 UnexpectedConnectionStateException       {

        try {

            ConnectionState currentConnectionState = dao.getConnectionState(connectionId);

            switch (currentConnectionState) {

                case CANCELLED_LOCALLY:
                    // no action needed
                    break;

                case PENDING_REMOTELY_ACCEPTANCE:

                    // cancel connection through network service and after that mark as CANCELLED LOCALLY
                    cryptoBrokerNetworkService.cancelConnection(connectionId);

                    dao.changeConnectionState(
                            connectionId,
                            ConnectionState.CANCELLED_LOCALLY
                    );

                    break;

                default:
                    throw new UnexpectedConnectionStateException(
                            "connectionId: "+connectionId + " - currentConnectionState: "+currentConnectionState,
                            "Unexpected contact state for cancelling."
                    );
            }

        } catch (final ActorConnectionNotFoundException | UnexpectedConnectionStateException innerException) {

            errorManager.reportUnexpectedPluginException(
                    pluginVersionReference,
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    innerException
            );
            throw innerException;
        } catch (final CantGetConnectionStateException cantGetConnectionStateException) {

            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, cantGetConnectionStateException);
            throw new CantCancelActorConnectionRequestException(cantGetConnectionStateException, "connectionId: "+connectionId, "Error trying to get the connection state.");
        } catch (final CantCancelConnectionRequestException | ConnectionRequestNotFoundException networkServiceException ) {

            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, networkServiceException);
            throw new CantCancelActorConnectionRequestException(networkServiceException, "connectionId: "+connectionId, "Error trying to cancel the connection through the network service.");
        } catch (final CantChangeActorConnectionStateException cantChangeActorConnectionStateException ) {

            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, cantChangeActorConnectionStateException);
            throw new CantCancelActorConnectionRequestException(cantChangeActorConnectionStateException, "connectionId: "+connectionId, "Error trying to change the actor connection state.");
        } catch (final Exception exception) {

            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, exception);
            throw new CantCancelActorConnectionRequestException(exception, "connectionId: "+connectionId, "Unhandled error.");
        }
    }

    @Override
    public void acceptConnection(final UUID connectionId) throws CantAcceptActorConnectionRequestException,
                                                                 ActorConnectionNotFoundException         ,
                                                                 UnexpectedConnectionStateException       {

        try {

            ConnectionState currentConnectionState = dao.getConnectionState(connectionId);

            switch (currentConnectionState) {

                case CONNECTED:
                    // no action needed
                    break;

                case PENDING_LOCALLY_ACCEPTANCE:

                    // cancel connection through network service and after that mark as CANCELLED LOCALLY
                    cryptoBrokerNetworkService.acceptConnection(connectionId);

                    dao.changeConnectionState(
                            connectionId,
                            ConnectionState.CONNECTED
                    );

                    break;

                default:
                    throw new UnexpectedConnectionStateException("connectionId: "+connectionId + " - currentConnectionState: "+currentConnectionState, "Unexpected contact state for cancelling.");
            }

        } catch (final ActorConnectionNotFoundException | UnexpectedConnectionStateException innerException) {

            errorManager.reportUnexpectedPluginException(
                    pluginVersionReference,
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                    innerException
            );
            throw innerException;
        } catch (final CantGetConnectionStateException cantGetConnectionStateException) {

            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, cantGetConnectionStateException);
            throw new CantAcceptActorConnectionRequestException(cantGetConnectionStateException, "connectionId: "+connectionId, "Error trying to get the connection state.");
        } catch (final CantAcceptConnectionRequestException | ConnectionRequestNotFoundException networkServiceException ) {

            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, networkServiceException);
            throw new CantAcceptActorConnectionRequestException(networkServiceException, "connectionId: "+connectionId, "Error trying to accept the connection through the network service.");
        } catch (final CantChangeActorConnectionStateException cantChangeActorConnectionStateException ) {

            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, cantChangeActorConnectionStateException);
            throw new CantAcceptActorConnectionRequestException(cantChangeActorConnectionStateException, "connectionId: "+connectionId, "Error trying to change the actor connection state.");
        } catch (final Exception exception) {

            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, exception);
            throw new CantAcceptActorConnectionRequestException(exception, "connectionId: "+connectionId, "Unhandled error.");
        }
    }

}
