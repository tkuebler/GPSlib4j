package com.diddlebits.gpslib4j;

import java.net.URL;

import javax.swing.ImageIcon;

public class IconEnumDefinition extends GPSEnumDefinition {

    private String path;
    
    public IconEnumDefinition(String name, boolean canBeNull, String path) {
        super(name, canBeNull);
        this.path=path;
    }

    /**
     * Used to setup the enum.
     * 
     * @param name
     *            The string representation of the value
     * @param value
     *            The integer representation of the value
     */
    public void addValue(String name, int value) {
        super.addValue(name, value, null);
    }

    /**
     * Convert an enum into a graphical representation of it.
     * 
     * @param value
     *            The enum value.
     * @return An Icon or NULL.
     * @throws InvalidFieldValue
     *             If the enum value is unknown.
     */
    public Object enumToGraph(int value) throws InvalidFieldValue {
        Integer key=new Integer(value);
        if(graphs.containsKey(key)) {
            return graphs.get(key);
        } else {
            Object icon=getIcon(value);
            graphs.put(key, icon);
            return icon;
        }
    }

    private ImageIcon getIcon(int value) {
        String fileName;
        try {
            fileName = getNewFilename(enumToString(value));
        } catch (InvalidFieldValue e1) {
            System.out.println("Invalid enum ("+name+") value: "+value);
            return null;
        }
        URL url = IconEnumDefinition.class.getResource(fileName);
        if (url != null) {
            return new ImageIcon(url);
        } else {
            try {
                System.out.println("Not found icon '"+fileName+"' ("+enumToString(value)+")");
            } catch (InvalidFieldValue e) {
            }
            return null;
        }
    }

    public String getNewFilename(String value) {
        return path+"_"+value.replaceAll("[^\\w\\d]+","_").toLowerCase()+".png";
    }
}
