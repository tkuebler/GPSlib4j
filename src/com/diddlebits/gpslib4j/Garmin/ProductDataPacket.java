package com.diddlebits.gpslib4j.Garmin;

import java.io.Serializable;

import com.diddlebits.gpslib4j.*;

public class ProductDataPacket extends GarminPacket implements Serializable {
    private static final long serialVersionUID = -5072518834552481189L;

    /** Product-ID of GPS. */
    protected int productID;

    /** Software version in GPS. */
    protected int SWversion;

    /** Description of GPS as given by GPS. */
    protected String productDesc;

    private static StringValidator ProductDescValidator;

    protected static IntegerSpecification ProductIdSpecification = new IntegerSpecification(
            0, 0xFFFF, false);

    protected static IntegerSpecification SoftwareVersionSpecification = new IntegerSpecification(
            -0x8000, 0x7FFF, false);

    public ProductDataPacket(GarminRawPacket p)
            throws PacketNotRecognizedException, InvalidFieldValue {
        super();
        initFromRawPacket(p);
    }

    /**
     * Treats the packet p as a packet containing product-data. Throws
     * PacketNotRecognizedException if p is not a product-data-packet.
     * 
     * @throws PacketNotRecognizedException
     * @throws InvalidFieldValue
     */
    public void initFromRawPacket(GarminRawPacket p)
            throws PacketNotRecognizedException, InvalidFieldValue {
        if (p.getPID() != GarminRawPacket.Pid_Product_Data) {
            throw (new PacketNotRecognizedException(
                    GarminRawPacket.Pid_Product_Data, p.getPID()));
        }

        super.initFromRawPacket(p);
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
        productID = (int) visitor.intField(UINT16, GPSFields.ProductIdField,
                productID, ProductIdSpecification, 0x10000);
        SWversion = (int) visitor.intField(SINT16, GPSFields.SWVersionField,
                SWversion, SoftwareVersionSpecification, 0x8000);
        productDesc = visitor.stringField(VCHAR,
                GPSFields.ProductDescriptionField, productDesc,
                GetProductDescValidator());
    }

    public String getPacketType() {
        return "product";
    }

    public static StringValidator GetProductDescValidator()
            throws InvalidFieldValue {
        if (ProductDescValidator == null) {
            ProductDescValidator = new RegexpStringValidator(
                    "product description", "^[\\w \\.]*$", 100, false);
        }
        return ProductDescValidator;
    }

    public int getPacketId() {
        return GarminRawPacket.Pid_Product_Data;
    }
}