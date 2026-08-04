package com.chatterjee.sayan.payzapp.common.exceptions;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {

    private final String resourceName;
    private final Object identifier;

    public ResourceNotFoundException(String resourceName, Object identifier) {
        super("Resource not found with name " + resourceName + " and identifier " + identifier);
        this.resourceName = resourceName;
        this.identifier = identifier;
    }
}
