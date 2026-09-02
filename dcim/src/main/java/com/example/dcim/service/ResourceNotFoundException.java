package com.example.dcim.service;
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, Long id) { super(resource + " sa ID " + id + " nije pronađen."); }
}
