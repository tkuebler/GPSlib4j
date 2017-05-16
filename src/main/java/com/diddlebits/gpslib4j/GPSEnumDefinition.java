package com.diddlebits.gpslib4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class GPSEnumDefinition {
    protected HashMap names = new HashMap();

    protected HashMap values = new HashMap();

    protected HashMap graphs = new HashMap();

    private int min = Integer.MAX_VALUE;

    private int max = Integer.MIN_VALUE;

    private boolean canBeNull;

    protected String name;

    public GPSEnumDefinition(String xName, boolean xCanBeNull) {
        name = xName;
        canBeNull = xCanBeNull;
    }

    /**
     * Convert an enum into a user friendly string.
     * 
     * @param value
     *            The enum value.
     * @return The user friendly string.
     * @throws InvalidFieldValue
     *             If the enum value is unknown.
     */
    public String enumToString(int value) throws InvalidFieldValue {
        String ret = (String) names.get(new Integer(value));
        if (ret == null)
            throw new InvalidFieldValue(name, Integer.toString(value),
                    "Unknown value for enum");
        return ret;
    }

    /**
     * Convert an enum into a graphical representation of it.
     * 
     * @param value
     *            The enum value.
     * @return A Color, an Icon or NULL.
     * @throws InvalidFieldValue
     *             If the enum value is unknown.
     */
    public Object enumToGraph(int value) throws InvalidFieldValue {
        Object ret = graphs.get(new Integer(value));
        if (ret != null) {
            return ret;
        } else {
            enumToString(value); // just to check it's known and raise an
            // exception if not
            return null;
        }
    }

    /**
     * Convert an enum into a user friendly string.
     * 
     * @param txt
     *            The user friendly string.
     * @return The enum value.
     * @throws InvalidFieldValue
     *             If the enum value is unknown.
     */
    public int StringToEnum(String txt) throws InvalidFieldValue {
        Integer ret = (Integer) values.get(txt);
        if (ret == null)
            throw new InvalidFieldValue(name, txt, "Unknown value for enum");
        return ret.intValue();
    }

    /**
     * @return The minimum allowed value of the enum
     */
    public int getMinValue() {
        return min;
    }

    /**
     * @return The maximum allowed value of the enum
     */
    public int getMaxValue() {
        return max;
    }

    /**
     * Used to setup the enum.
     * 
     * @param name
     *            The string representation of the value
     * @param value
     *            The integer representation of the value
     * @param graph
     *            The graphical representation of the value (maybe a Color, or
     *            an Icon, ...) or NULL
     */
    public void addValue(String name, int value, Object graph) {
        Integer intValue = new Integer(value);
        if (values.containsKey(name)) {
            throw new RuntimeException("An enum named '" + name
                    + " already exists");
        }
        values.put(name, intValue);
        if (names.containsKey(intValue)) {
            throw new RuntimeException("An enum with a value of '" + intValue
                    + " already exists");
        }
        names.put(intValue, name);
        if (graph != null)
            graphs.put(intValue, graph);
        if (min > value)
            min = value;
        if (max < value)
            max = value;
    }

    /**
     * @return The list of string representations of the names.
     */
    public String[] getStrings() {
        String[] ret = new String[names.size()];
        Iterator it = names.values().iterator();
        for (int cpt = 0; cpt < ret.length && it.hasNext(); ++cpt) {
            ret[cpt] = (String) it.next();
        }
        Arrays.sort(ret);
        return ret;
    }

    public boolean isCanBeNull() {
        return canBeNull;
    }

    public Integer[] getValues() {
        Integer[] ret = new Integer[names.size()];
        Iterator it = names.keySet().iterator();
        for (int cpt = 0; cpt < ret.length && it.hasNext(); ++cpt) {
            ret[cpt] = (Integer) it.next();
        }
        Arrays.sort(ret);
        return ret;
    }
}
