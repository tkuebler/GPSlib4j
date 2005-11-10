package com.diddlebits.gpslib4j.examples;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Vector;

import javax.comm.CommPort;
import javax.comm.CommPortIdentifier;
import javax.comm.PortInUseException;

import com.diddlebits.gpslib4j.FeatureNotSupportedException;
import com.diddlebits.gpslib4j.GPS;
import com.diddlebits.gpslib4j.ITransferListener;

public class Utils implements ITransferListener {
    private BufferedReader inuser;

    private static Utils instance;

    private boolean transfering;

    private CommPort port;

    public static Utils get() {
        if (instance == null) {
            instance = new Utils();
        }
        return instance;
    }

    private Utils() {
        inuser = new BufferedReader(new InputStreamReader(System.in));
        transfering = false;
    }

    public GPS connectGPS() {
        String gpsBrand = ChooseBrand();
        CommPortIdentifier zPort = ChoosePort();

        try {
            port = zPort.open("ConnectionTest", 5000);
        } catch (PortInUseException e) {
            System.out.println("Port already in use by " + e.currentOwner);
            System.exit(-1);
            return null;
        }

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        BufferedInputStream input;
        BufferedOutputStream output;
        try {
            input = new BufferedInputStream(port.getInputStream());
            output = new BufferedOutputStream(port.getOutputStream());
        } catch (IOException e) {
            System.out.println("Error opening port");
            System.exit(-1);
            return null;
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        transfering = true;
        GPS gps;
        try {
            gps = GPS.CreateInterface(gpsBrand, input, output, this);
        } catch (FeatureNotSupportedException e) {
            System.out.println("Error creating a driver");
            System.exit(-1);
            return null;
        }

        System.out.println("Connecting to GPS.");
        // String description = ((GarminGPS) gps).getDescription();
        waitForResponse();
        System.out.println("Connected.");

        return gps;
    }

    public void closeGPS() {
        if (port != null) {
            port.close();
        }
    }

    private void waitForResponse() {
        int timeout = 200;

        while (transfering && timeout >= 0) {
            // TODO: need to transfer timeout and cancel interface
            timeout--;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
        if (transfering) {
            System.out.println("Timeout!!!");
            System.exit(-1);
        }
    }

    private String ChooseBrand() {
        Vector brands = GPS.GetBrands();
        int index = -1;
        while (index < 0) {
            System.out.println("Available brands: ");
            int j = 1;
            for (Enumeration i = brands.elements(); i.hasMoreElements();) {
                System.out.print("  " + j++ + ". ");
                System.out.println(i.nextElement());
            }

            if (GPS.GetBrands().size() > 1) {
                System.out.print("Select brand: ");
                String input = readFromUser();

                try {
                    index = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    index = -1;
                    continue;
                }

                if (index < 1 || index > GPS.GetBrands().size()) {
                    index = -1;
                    continue;
                }
            } else {
                index = 1;
                System.out
                        .println("Take the only one available for the moment: "
                                + (String) brands.elementAt(index - 1));
                System.out.println("");
            }
        }
        return (String) brands.elementAt(index - 1);
    }

    private CommPortIdentifier ChoosePort() {
        Vector names = null;
        int index = -1;
        while (index == -1) {
            int j = 1;
            names = new Vector();
            System.out.println("Available ports: ");
            CommPortIdentifier c;
            for (Enumeration i = CommPortIdentifier.getPortIdentifiers(); i
                    .hasMoreElements();) {
                c = (CommPortIdentifier) i.nextElement();
                System.out.print("  " + j++ + ". " + c.getName());
                names.add(c);
                if (c.getPortType() == CommPortIdentifier.PORT_SERIAL)
                    System.out.print("\t SERIAL\n");
                if (c.getPortType() == CommPortIdentifier.PORT_PARALLEL)
                    System.out.print("\t PARALLEL\n");
            }

            System.out.print("Select port: ");
            String input = readFromUser();

            try {
                index = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                index = -1;
                continue;
            }

            if ((index < 1) || (index > names.size())) {
                index = -1;
                continue;
            }

        }

        return (CommPortIdentifier) names.elementAt(index - 1);
    }

    private String readFromUser() {
        try {
            return inuser.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void transferStarted(int number) {
        System.out
                .println("transfer started notification arrived at connection test: "
                        + number);
        transfering = true;
    }

    public void transferComplete() {
        System.out
                .println("transfer complete notification arrived at connection test.");
        transfering = false;
    }

    public void errorReceived(Exception e) {
        System.out.println("Error during the communication:");
        System.out.println(e.toString());
        System.exit(1);
        transfering = false;
    }
}
