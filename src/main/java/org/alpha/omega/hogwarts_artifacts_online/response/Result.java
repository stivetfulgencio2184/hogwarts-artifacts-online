package org.alpha.omega.hogwarts_artifacts_online.response;

import lombok.Builder;

/*
    This class defines the schema of the response. It is used to encapsulate data prepared by send to
    the server side, this object will be serialized to JSON before send back to the client
 */
@Builder
public record Result(
        Boolean flag, // Two values: true means success, false means not success
        Integer code, // Status code, e.g. 200
        String message, // Response message
        Object data // The response payload
) {
}
