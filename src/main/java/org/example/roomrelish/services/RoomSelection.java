package org.example.roomrelish.services;

public class RoomSelection {
    public RoomTypeInterface selectRoom(String roomType) {

      if (roomType.equalsIgnoreCase("kingsize")) {
            return new KingSizeRoom();
        }
      else if (roomType.equalsIgnoreCase("queensize")) {
            return new QueenSizeRoom();
        }
      else {
          return new DeluxeRoom();
                }
    }
}
