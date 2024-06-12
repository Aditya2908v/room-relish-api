package org.example.roomrelish.services;

public class DeluxeRoom  implements RoomTypeInterface{
    @Override
    public double roomServiceCharge() {
        return 100.0;
    }
}
