package com.diddlebits.gpslib4j;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Class containing the list of every possible fields available. This class is
 * here for several reasons:
 * <ul>
 * <li>Force a standard naming between the different GPS models</li>
 * <li>Make easy the translation of the field names</li>
 * </ul
 */
public class GPSFields {
    /** Singleton instance */
    private static GPSFields Instance;

    /** Singleton accessor */
    public static GPSFields Get() {
        if (Instance == null) {
            Instance = new GPSFields();
        }
        return Instance;
    }

    // The list of every GPSFields supported so far
    public final static String IdentField = "ident";

    public final static String StartTimeField = "start time";

    public final static String TotalTimeField = "total time";

    public final static String TotalDistanceField = "total distance";

    public final static String StartPositionField = "start position";

    public final static String EndPositionField = "end position";

    public final static String CaloriesField = "calories";

    public final static String IndexField = "index";

    public final static String PositionField = "position";

    public final static String ProductIdField = "product ID";

    public final static String SWVersionField = "software version";

    public final static String ProductDescriptionField = "product description";

    public final static String AltitudeField = "altitude";

    public final static String DepthField = "depth";

    public final static String EPEField = "estimated position error";

    public final static String HEPEField = "horizontal estimated position error";

    public final static String VEPEField = "vertical estimated position error";

    public final static String PositionFixTypeField = "position fix type";

    public final static String TimeOfWeekField = "time of week";

    public final static String VelocityEastField = "velocity east";

    public final static String VelocityNorthField = "velocity north";

    public final static String VelocityUpField = "velocity up";

    public final static String MslHeightField = "height of WGS84 ellipsoid above MSL";

    public final static String LeapSecondsField = "difference between GPS and UTC";

    public final static String WeekNumberdaysField = "week number days";

    public final static String NumberField = "number"; // used in records

    public final static String CommentField = "comment";

    public final static String ClassField = "class";

    public final static String SubClassField = "sub class";

    public final static String TimeField = "time";

    public final static String NewTrackField = "new track";

    public final static String TemperatureField = "temperature";

    public final static String DisplayField = "display";

    public final static String ColorField = "color";

    public final static String SymbolField = "symbol";

    public final static String ProxymityField = "proximity distance";

    public final static String StateField = "state";

    public final static String CountryCodeField = "country code";

    public final static String FacilityField = "facility";

    public final static String CityField = "city";

    public final static String AddressField = "address";

    public final static String CrossRoadField = "cross-road";

    public final static String ETEField = "ete";

    // Internal only fields (no passed the the IGPSData*Visitor classes
    public final static String UnusedField = "+unused";

    public final static String AttrField = "+attr";

    public final static String DisplayColorField = "+dspl_color";

    private ArrayList fieldNames;

    private GPSFields() {
        fieldNames = new ArrayList();

        // use the reflexion to find out the list of fields
        Field fields[] = getClass().getDeclaredFields();
        for (int cpt = 0; cpt < fields.length; ++cpt) {
            Field cur = fields[cpt];
            // check it's a field
            if (cur.getName().endsWith("Field")) {
                Object value;
                try {
                    value = cur.get(null);
                    if (value != null && value.getClass() == String.class) {
                        String txt = (String) value;
                        // check it's not an internal field
                        if (!txt.startsWith("+"))
                            fieldNames.add(txt);
                    }
                } catch (IllegalArgumentException e) {
                } catch (IllegalAccessException e) {
                }
            }
        }
    }

    public ArrayList getFieldNames() {
        return fieldNames;
    }
}
