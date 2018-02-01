package de.ralfhergert.telemetry.pc2.datagram.v2;

import de.ralfhergert.telemetry.pc2.datagram.IntegerParser;

import java.util.Date;

/**
 * Each package shares the same base package information identifying the package.
 * @see <a href="https://www.projectcarsgame.com/project-cars-2-api.html"></a>
 * @see <a href="https://www.projectcarsgame.com/uploads/2/0/6/5/20658008/sms_udp_definitions.hpp">UDP Patch 3</a>
 */
public class BasePackage {

	private Date receivedDate;

	private int packetNumber;          //0 counter reflecting all the packets that have been sent during the game run
	private int categoryPacketNumber;  //4 counter of the packet groups belonging to the given category
	private short partialPacketIndex;  //8 If the data from this class had to be sent in several packets, the index number
	private short partialPacketNumber; //9 If the data from this class had to be sent in several packets, the total number
	private PackageTypes packetType;   //10 what is the type of this packet (ordinal of the PackageTypes)
	private short packetVersion;       //11 what is the version of protocol for this handler, to be bumped with data structure change

	public BasePackage() {}

	public BasePackage(byte[] data) {
		this(data, new Date());
	}

	public BasePackage(byte[] data, Date receivedDate) {
		if (data == null) {
			throw new IllegalArgumentException("data must not be null");
		}
		if (data.length < 12) {
			throw new IllegalArgumentException("given data array is too short to be read as BasePackage");
		}
		packetNumber = IntegerParser.parse(data, 0);
		categoryPacketNumber = IntegerParser.parse(data, 4);
		partialPacketIndex = data[8];
		partialPacketNumber = data[9];
		packetType = PackageTypes.values()[data[10]];
		packetVersion = data[11];
		this.receivedDate = receivedDate;
	}

	public int getPacketNumber() {
		return packetNumber;
	}

	public void setPacketNumber(int packetNumber) {
		this.packetNumber = packetNumber;
	}

	public int getCategoryPacketNumber() {
		return categoryPacketNumber;
	}

	public void setCategoryPacketNumber(int categoryPacketNumber) {
		this.categoryPacketNumber = categoryPacketNumber;
	}

	public short getPartialPacketIndex() {
		return partialPacketIndex;
	}

	public void setPartialPacketIndex(short partialPacketIndex) {
		this.partialPacketIndex = partialPacketIndex;
	}

	public short getPartialPacketNumber() {
		return partialPacketNumber;
	}

	public void setPartialPacketNumber(short partialPacketNumber) {
		this.partialPacketNumber = partialPacketNumber;
	}

	public PackageTypes getPacketType() {
		return packetType;
	}

	public void setPacketType(PackageTypes packetType) {
		this.packetType = packetType;
	}

	public short getPacketVersion() {
		return packetVersion;
	}

	public void setPacketVersion(short packetVersion) {
		this.packetVersion = packetVersion;
	}

	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	@Override
	public String toString() {
		return "BasePackage{" +
			"packetNumber=" + packetNumber +
			", categoryPacketNumber=" + categoryPacketNumber +
			", partialPacketIndex=" + partialPacketIndex +
			", partialPacketNumber=" + partialPacketNumber +
			", packetType=" + packetType +
			", packetVersion=" + packetVersion +
			'}';
	}
}
