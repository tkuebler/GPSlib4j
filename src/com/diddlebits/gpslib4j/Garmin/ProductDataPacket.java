package com.diddlebits.gpslib4j.Garmin;

import com.diddlebits.gpslib4j.IProductData;

public class ProductDataPacket extends GarminPacket implements IProductData {	
	/** Product-ID of GPS. */
	protected int productID;
	/** Software version in GPS.*/
	protected int SWversion;
	/** Description of GPS as given by GPS. */
	protected String productDesc;
	
	/**
	* Treats the packet p as a packet containing product-data.
	* Throws PacketNotRecognizedException if p is not a product-data-packet.
	*/
	public ProductDataPacket(int[] p) {
		super(p);
		if (getID() != Pid_Product_Data) {
			throw(new PacketNotRecognizedException(Pid_Product_Data, getID()));
		}		
		
		productID = readWord(3);
		SWversion = readWord(5);
		productDesc = readNullTerminatedString(7);
	}
	
	/** 
	* This method is a copy-constructor allowing to "upgrade" a GarminPacket to a ProductDataPacket.
	* Throws PacketNotRecognizedException if p is not a product-data-packet.
	*/	
	public ProductDataPacket(GarminPacket p) {
		this( p.packet );		
	}
	
	
	/** Returns the product ID of the GPS.*/
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
	/** Returns a human-readable version of this packet. */
	public String toString() {
		StringBuffer res = new StringBuffer();
		res.append(productDesc + '\n');
		res.append("Product ID: " + productID);
		res.append("\nSoftware version: " + SWversion);
		return res.toString();
	}

	/* (non-Javadoc)
	 * @see com.diddlebits.gpslib4j.IProductData#getVersion()
	 */
	public String getVersion() {
		// TODO Auto-generated method stub
		return String.valueOf(getSWVersion());
	}

	/* (non-Javadoc)
	 * @see com.diddlebits.gpslib4j.IProductData#getId()
	 */
	public String getId() {
		// TODO Auto-generated method stub
		return String.valueOf(productID);
	}
	
}