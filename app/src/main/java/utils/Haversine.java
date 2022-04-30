package utils;

public class Haversine {
    private static final int EARTH_RADIUS = 6371; // Approx Earth radius in KM
    public static double haversin(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }

    public static double kmtometer(double km){
            int m = 1000;
            System.out.println("Enter Kilometer: ");
            double meter=km*m;
           return meter;
    }

    public static double distance(double lat1, double lat2, double lon1,
                                   double lon2) {
//System.out.println("first location==="+lat1+" "+lon1);
       // System.out.println("second location==="+lat2+" "+lon2);
        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = 0 - 0;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }
    //distance calculateDistance============6359702.360395054
    // distance============191412.58146738145

    public static   double calculateDistance(
            double longitude1, double latitude1,
            double longitude2, double latitude2) {
        double c =
                Math.sin(Math.toRadians(latitude1)) *
                        Math.sin(Math.toRadians(latitude2)) +
                        Math.cos(Math.toRadians(latitude1)) *
                                Math.cos(Math.toRadians(latitude2)) *
                                Math.cos(Math.toRadians(longitude2) -
                                        Math.toRadians(longitude1));
        c = c > 0 ? Math.min(1, c) : Math.max(-1, c);
        return 3959 * 1.609 * 1000 * Math.acos(c);
    }
}
