package com.diddlebits.gpslib4j.Garmin;

public class ProtocolDataPacket extends GarminPacket {
	
	protected char[] tags;
	protected int[]  data;
	
	/**
	* Treats the packet p as a packet containing data about which protocols the GPS support.
	* Throws PacketNotRecognizedException if p is not a product-data-packet.
	*/		
	public ProtocolDataPacket(int[] p) {
		super(p);
		if (getID() != Pid_Protocol_Array) {
			throw(new PacketNotRecognizedException(Pid_Protocol_Array, getID()));
		}		
		
		if (getDataLength() % 3 != 0) {
			throw(new InvalidPacketException(packet, 2));				
		}
		
		tags = new char[getDataLength() / 3];
		data = new int[getDataLength() / 3];
		
		int packet_index = 3;
		int array_index = 0;
		while (packet_index != getDataLength() + 3) {
			tags[array_index] = (char) readByte(packet_index++);
			data[array_index] = readWord(packet_index);
			packet_index += 2;
			array_index++;
		}		
	}
	
	public ProtocolDataPacket(GarminPacket p) {
		this(p.packet);
	}
	
	/**
	* This method will return the exact version of a protocol.
	* If the protocol is not supported by the GPS, the method returns -1.
	*/ 
	public int getVersion(char tag, int protocol) {
		for (int i = 0 ; i < tags.length ; i++) {
			if (tags[i] == tag) {
				if ( data[i] / protocol == 1)
					return data[i];
			}
		}
		return -1;
	}
	
	public String toString() {
		StringBuffer res = new StringBuffer();
		res.append("Tag:\tData:\n");
		for (int i = 0 ; i < tags.length ; i++) {
			res.append(tags[i] + "\t" + data[i] + '\n');
		}
		return res.toString();
	}
}