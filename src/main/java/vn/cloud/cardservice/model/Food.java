package vn.cloud.cardservice.model;

import javax.xml.stream.Location;

public class Food {

    private Long foodId;
    private String title;
    private String description;
    private Double listDays;
    private Boolean isPendingPickup;
    private Boolean isCollected;
    private String isHalal;
    private Boolean isListed;
//    private Double longitude;
//    private Double latitude;
    private Location foodLocation;
}
