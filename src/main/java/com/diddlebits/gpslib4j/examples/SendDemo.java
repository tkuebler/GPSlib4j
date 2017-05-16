package com.diddlebits.gpslib4j.examples;

import java.util.ArrayList;

import com.diddlebits.gpslib4j.GPS;
import com.diddlebits.gpslib4j.ITrackpoint;
import com.diddlebits.gpslib4j.ITrackpointHeader;
import com.diddlebits.gpslib4j.IWaypoint;
import com.diddlebits.gpslib4j.Position;

public class SendDemo {
    public static void main(String args[]) {
        SendDemo test = new SendDemo();
        test.run();
        System.exit(0);
    }

    private void run() {
        GPS gps = Utils.get().connectGPS();
        if (gps != null) {
            sendWaypoints(gps);
            sendTrack(gps);
        }
        Utils.get().closeGPS();
    }

    private void sendTrack(GPS gps) {
        try {
            ITrackpointHeader header = gps.getFactory()
                    .createTrackpointHeader();
            header.setIdent("TEST");

            ArrayList points = new ArrayList(3);

            ITrackpoint point = gps.getFactory().createTrackpoint();
            point.setPosition(new Position(45.9, 4.9));
            points.add(point);

            point = gps.getFactory().createTrackpoint();
            point.setPosition(new Position(45.9, 4.8));
            points.add(point);

            point = gps.getFactory().createTrackpoint();
            point.setPosition(new Position(45.8, 4.8));
            points.add(point);

            gps.sendTrack(header, points);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
            return;
        }
    }

    /**
     * @param gps
     */
    private void sendWaypoints(GPS gps) {
        ArrayList waypoints = new ArrayList(2);

        try {
            IWaypoint waypoint;
            waypoint = gps.getFactory().createWaypoint();
            waypoint.setIdent("TEST1");
            waypoint.setPosition(new Position(46, 5));
            waypoints.add(waypoint);
        } catch (Exception e) {
            System.out.println("Cannot create a waypoint:");
            e.printStackTrace();
            System.exit(-1);
            return;
        }

        try {
            IWaypoint waypoint;
            waypoint = gps.getFactory().createWaypoint();
            waypoint.setIdent("TEST2");
            waypoint.setPosition(new Position(46.1, 5.1));
            waypoints.add(waypoint);
        } catch (Exception e) {
            System.out.println("Cannot create a waypoint:");
            e.printStackTrace();
            System.exit(-1);
            return;
        }

        try {
            gps.sendWaypoints(waypoints);
        } catch (Exception e) {
            System.out.println("Cannot send the waypoints:");
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
