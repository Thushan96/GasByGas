package com.esoft.gascollect.model;

public enum OrderStatus {
    PENDING, // Awaiting stock or user pickup
    DELIVERED, // Successfully picked up or delivered
    CANCELLED, // Cancelled due to any reason
    PENDING_REALLOCATION // Awaiting reallocation for another customer
}
