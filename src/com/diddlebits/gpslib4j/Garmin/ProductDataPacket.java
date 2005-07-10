package com.diddlebits.gpslib4j.Garmin;

import com.diddlebits.gpslib4j.*;

public class ProductDataPacket extends GarminPacket {
    /** Product-ID of GPS. */
    protected int productID;

    /** Software version in GPS. */
    protected int SWversion;

    /** Description of GPS as given by GPS. */
    protected String productDesc;

    private static StringValidator ProductDescValidator;

    /**
     * Treats the packet p as a packet containing product-data. Throws
     * PacketNotRecognizedException if p is not a product-data-packet.
     * @throws PacketNotRecognizedException 
     * @throws InvalidFieldValue 
     */
    public ProductDataPacket(GarminRawPacket p) throws PacketNotRecognizedException, InvalidFieldValue {
        super();
        if (p.getID() != GarminRawPacket.Pid_Product_Data) {
            throw (new PacketNotRecognizedException(
                    GarminRawPacket.Pid_Product_Data, p.getID()));
        }

        initFromRawPacket(p);
    }

    /** Returns the product ID of the GPS. */
    public int getProductID() {
        return productID;
    }

    /** Returns the version of the software in the GPS. */
    public int getSWVersion() {
        return SWversion;
    }

    /** Returns the supplied description of the GPS. */
    public String getDescription() {
        return productDesc;
    }

    public void visit(GarminGPSDataVisitor visitor) throws InvalidFieldValue {
        productID = (int) visitor.intField(UINT16, GPSFields.ProductIdField, productID, 0,
                0xFFFF, 0x10000);
        SWversion = (int) visitor.intField(SINT16, GPSFields.SWVersionField,
                SWversion, -0x8000, 0x7FFF, 0x8000);
        productDesc = visitor.stringField(VCHAR, GPSFields.ProductDescriptionField,
                productDesc, 100, GetProductDescValidator());
    }

    public String getPacketType() {
        return "product";
    }

    public static StringValidator GetProductDescValidator()
            throws InvalidFieldValue {
        if (ProductDescValidator == null) {
            ProductDescValidator = new RegexpStringValidator(
                    "product description", "^[\\w \\.]*$");
        }
        return ProductDescValidator;
    }
}