package model;

public class Road {
    private int id;
    private int sourceLibraryId;
    private int destinationLibraryId;
    private double distance;
    private double travelTime;

    public Road(
            int id,
            int sourceLibraryId,
            int destinationLibraryId,
            double distance,
            double travelTime
    ) {
        this.id = id;
        this.sourceLibraryId = sourceLibraryId;
        this.destinationLibraryId = destinationLibraryId;
        this.distance = distance;
        this.travelTime = travelTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSourceLibraryId() {
        return sourceLibraryId;
    }

    public void setSourceLibraryId(int sourceLibraryId) {
        this.sourceLibraryId = sourceLibraryId;
    }

    public int getDestinationLibraryId() {
        return destinationLibraryId;
    }

    public void setDestinationLibraryId(int destinationLibraryId) {
        this.destinationLibraryId = destinationLibraryId;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(double travelTime) {
        this.travelTime = travelTime;
    }
}
