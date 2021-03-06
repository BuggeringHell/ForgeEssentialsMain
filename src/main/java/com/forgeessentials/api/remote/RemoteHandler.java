package com.forgeessentials.api.remote;

import com.forgeessentials.api.remote.RemoteRequest.JsonRemoteRequest;

/**
 *
 */
public interface RemoteHandler {

    public static final String PERM = "fe.remote";

    public static final String MSG_NO_PERMISSION = "no permission";

    public static final String MSG_EXCEPTION = "exception";

    String getID();

    String getPermission();

    RemoteResponse handle(RemoteSession session, JsonRemoteRequest request);

    public static class RemoteException extends RuntimeException {

        public RemoteException(String message)
        {
            super(message);
        }

        public RemoteException()
        {
            super(MSG_EXCEPTION);
        }

    }

    public static class PermissionException extends RemoteException {

        public PermissionException()
        {
            super(MSG_NO_PERMISSION);
        }

    }

}
