package com.devlukas.hogwartsartifactsonline.system.exception;

public class ObjectNotFoundException extends RuntimeException{

    public ObjectNotFoundException(String objectName, String id) {
        super("Could not found %s with Id %s".formatted(objectName, id));
    }

    public ObjectNotFoundException(String objectName, Integer id) {
        super("Could not found %s with Id %d".formatted(objectName, id));
    }
}
