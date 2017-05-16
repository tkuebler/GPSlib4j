package com.diddlebits.gpslib4j.Garmin;

import com.diddlebits.gpslib4j.FeatureNotSupportedException;
import com.diddlebits.gpslib4j.IGPSFactory;
import com.diddlebits.gpslib4j.ILap;
import com.diddlebits.gpslib4j.IPosition;
import com.diddlebits.gpslib4j.IRouteHeader;
import com.diddlebits.gpslib4j.IRouteWaypoint;
import com.diddlebits.gpslib4j.ITimeDate;
import com.diddlebits.gpslib4j.ITrackpoint;
import com.diddlebits.gpslib4j.ITrackpointHeader;
import com.diddlebits.gpslib4j.IWaypoint;
import com.diddlebits.gpslib4j.InvalidFieldValue;

/**
 * Factory that is able to create the good packet class in function of the GPS's
 * capabilities.
 */
public class GarminFactory implements IGPSFactory {
    private static final long serialVersionUID = 4142307769066730621L;

    // All the possible commands
    // ---------------------------------------------------
    // to get the real command ID, use getCommandId.

    /** Abort current transfer. */
    public static final int CmndAbortTransfer = 0;

    /** Transfer almanac. */
    public static final int CmndTransferAlm = 1;

    /** Transfer position. */
    public static final int CmndTransferPosn = 2;

    /** Transfer proximity waypoints. */
    public static final int CmndTransferPrx = 3;

    /** Transfer routes. */
    public static final int CmndTransferRte = 4;

    /** Transfer time. */
    public static final int CmndTransferTime = 5;

    /** Transfer track log. */
    public static final int CmndTransferTrk = 6;

    /** Transfer waypoints. */
    public static final int CmndTransferWpt = 7;

    /** Turn off power. */
    public static final int CmndTurnOffPwr = 8;

    /** Start transmitting PVT (Position, velocity, time) Data. */
    public static final int CmndStartPvtData = 9;

    /** Stop transmitting PVT (Position, velocity, time) Data. */
    public static final int CmndStopPvtData = 10;

    /** Transfer Laps */
    public static final int Cmnd_Transfer_Laps = 11;

    public static final int CmndFlightBook = 12;

    public static final int CmndTransferLaps = 13;

    // Command or Data base IDs --------------------------------
    public static final int WaypointBaseId = 100;

    public static final int RouteBaseId = 200;

    public static final int TrackBaseId = 300;

    public static final int ProximityBaseId = 400;

    public static final int AlmanacBaseId = 500;

    public static final int DateTimeBaseId = 600;

    public static final int FlighBookBaseId = 650;

    public static final int PositionBaseId = 700;

    public static final int PVTBaseId = 800;

    public static final int LapBaseId = 900;

    public static final int ShutdownBaseId = 1000; // fake id, not Garmin

    // stuff, see exception in
    // GarminFactory.checkCanDo

    /** Singleton instance */
    static private GarminFactory Instance = null;

    /** What packet versions to use */
    private ProtocolDataPacket protocols = null;

    /** What GPS versions to use */
    private ProductDataPacket product = null;

    static public GarminFactory Get() {
        if (Instance == null) {
            Instance = new GarminFactory();
        }
        return Instance;
    }

    private ProtocolDataPacket getProtocols() {
        if (protocols == null) {
            System.out
                    .println("WARNING: your GPS is not sending protocol information. Guess from the model...");
            protocols = new ProtocolDataPacket(product);
        }
        return protocols;
    }

    private int getDataPacketVersion(int protocol)
            throws ProtocolNotRecognizedException {
        int ret = getProtocols().getVersion('D', protocol);
        if (ret == -1) {
            throw new ProtocolNotRecognizedException('D', protocol);
        }
        return ret;
    }

    private boolean doCommand(int protocol) {
        return getProtocols().getVersion('A', protocol) >= 0;
    }

    public int getCommandId(int protocol) throws FeatureNotSupportedException {
        switch (protocol) {
        case CmndAbortTransfer:
            if (doCommand(10) || doCommand(11)) {
                return 0;
            }
            break;

        case CmndTransferAlm:
            if (doCommand(AlmanacBaseId)) {
                if (doCommand(10)) {
                    return 1;
                } else if (doCommand(11)) {
                    return 4;
                }
            }
            break;

        case CmndTransferPosn:
            if (doCommand(10) && doCommand(PositionBaseId)) {
                return 2;
            }
            break;

        case CmndTransferPrx:
            if (doCommand(ProximityBaseId)) {
                if (doCommand(10)) {
                    return 3;
                } else if (doCommand(11)) {
                    return 17;
                }
            }
            break;

        case CmndTransferRte:
            if (doCommand(RouteBaseId)) {
                if (doCommand(10)) {
                    return 4;
                } else if (doCommand(11)) {
                    return 8;
                }
            }
            break;

        case CmndTransferTime:
            if (doCommand(DateTimeBaseId)) {
                if (doCommand(10)) {
                    return 5;
                } else if (doCommand(11)) {
                    return 20;
                }
            }
            break;

        case CmndTransferTrk:
            if (doCommand(TrackBaseId) && doCommand(10)) {
                return 6;
            }
            break;

        case CmndTransferWpt:
            if (doCommand(10)) {
                int ret = getProtocols().getVersion('A', WaypointBaseId);
                if (ret == 100) {
                    return 7;
                } else if (ret == 101) {
                    return 121;
                }
            } else if (doCommand(WaypointBaseId) && doCommand(11)) {
                return 21;
            }
            break;

        case CmndTurnOffPwr:
            if (doCommand(10)) {
                return 8;
            } else if (doCommand(11)) {
                return 26;
            }
            break;

        case CmndStartPvtData:
            if (doCommand(PVTBaseId) && doCommand(10)) {
                return 49;
            }
            break;

        case CmndStopPvtData:
            if (doCommand(PVTBaseId) && doCommand(10)) {
                return 50;
            }
            break;

        case CmndFlightBook:
            if (doCommand(FlighBookBaseId) && doCommand(10)) {
                return 92;
            }
            break;

        case CmndTransferLaps:
            if (doCommand(LapBaseId) && doCommand(10)) {
                return 117;
            }
            break;
        }

        throw new FeatureNotSupportedException();
    }

    public ProductDataPacket createProductDataAndInitFromIt(GarminRawPacket p)
            throws ProtocolNotRecognizedException,
            PacketNotRecognizedException, InvalidFieldValue {
        product = new ProductDataPacket(p);
        return product;
    }

    public ProtocolDataPacket createProtocolArrayAndInitFromIt(GarminRawPacket p)
            throws ProtocolNotRecognizedException,
            PacketNotRecognizedException, InvalidPacketException,
            InvalidFieldValue {
        protocols = new ProtocolDataPacket(p);
        return protocols;
    }

    public ITimeDate createTimeDate() throws ProtocolNotRecognizedException,
            ProtocolNotSupportedException {
        int proto = getDataPacketVersion(600);
        switch (proto) {
        case 600:
            return new TimeDataPacket600();
        default:
            throw new ProtocolNotSupportedException('D', proto);
        }
    }

    public ILap createLap() throws ProtocolNotRecognizedException,
            ProtocolNotSupportedException {
        int proto = getDataPacketVersion(900);
        switch (proto) {
        case 906:
            return new LapDataPacket906();
        default:
            throw new ProtocolNotSupportedException('D', proto);
        }
    }

    public IPosition createPosition() throws ProtocolNotRecognizedException,
            ProtocolNotSupportedException {
        int proto = getDataPacketVersion(700);
        switch (proto) {
        case 700:
            return new PositionDataPacket700();
        default:
            throw new ProtocolNotSupportedException('D', proto);
        }
    }

    public ITrackpoint createTrackpoint()
            throws ProtocolNotRecognizedException,
            ProtocolNotSupportedException {
        int proto = getDataPacketVersion(300);
        switch (proto) {
        case 300:
            return new TrackpointDataPacket300();
        case 301:
            return new TrackpointDataPacket301();
        case 302:
            return new TrackpointDataPacket302();
        default:
            throw new ProtocolNotSupportedException('D', proto);
        }
    }

    public ITrackpointHeader createTrackpointHeader()
            throws ProtocolNotRecognizedException,
            ProtocolNotSupportedException {
        int proto = getDataPacketVersion(310);
        switch (proto) {
        case 310:
            return new TrackpointHeaderPacket310();
        case 311:
            return new TrackpointHeaderPacket311();
        case 312:
            return new TrackpointHeaderPacket312();
        default:
            throw new ProtocolNotSupportedException('D', proto);
        }
    }

    public IWaypoint createWaypoint() throws ProtocolNotRecognizedException,
            ProtocolNotSupportedException {
        int proto = getDataPacketVersion(100);
        switch (proto) {
        case 103:
            return new WaypointDataPacket103();
        case 108:
            return new WaypointDataPacket108();
        case 109:
            return new WaypointDataPacket109();
        default:
            throw new ProtocolNotSupportedException('D', proto);
        }
    }

    public IPosition createPVT() throws ProtocolNotRecognizedException,
            ProtocolNotSupportedException {
        int proto = getDataPacketVersion(800);
        switch (proto) {
        case 800:
            return new PVTDataPacket800();
        default:
            throw new ProtocolNotSupportedException('D', proto);
        }
    }

    public RecordsPacket createRecords() throws ProtocolNotRecognizedException,
            ProtocolNotSupportedException {
        // low level stuff, not versioned
        return new RecordsPacket();
    }

    public IRouteHeader createRouteHeader()
            throws ProtocolNotRecognizedException,
            ProtocolNotSupportedException {
        int proto = getDataPacketVersion(200);
        switch (proto) {
        case 200:
            return new RouteHeaderPacket200();
        case 201:
            return new RouteHeaderPacket201();
        case 202:
            return new RouteHeaderPacket202();
        default:
            throw new ProtocolNotSupportedException('D', proto);
        }
    }

    public IRouteWaypoint createRouteWaypoint()
            throws ProtocolNotRecognizedException,
            ProtocolNotSupportedException {
        int proto = getDataPacketVersion(210);
        switch (proto) {
        case 210:
            return new RouteWaypointPacket210();
        default:
            throw new ProtocolNotSupportedException('D', proto);
        }
    }

    /**
     * @return True if a protocol data packet is expected
     */
    public static boolean isWaitingForProtocolData() {
        // TODO: to implement...
        return true;
    }
}
