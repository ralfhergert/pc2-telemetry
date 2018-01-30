package de.ralfhergert.telemetry.pc2.datagram.v2;

/**
 * Existing package types.
 * Do not change order. Ordinal is important.
 */
public enum PackageTypes {
	CarPhysics,
	RaceDefinition,
	Participants,
	Timings,
	GameState,
	WeatherState, // not sent at the moment, information can be found in the game state packet
	VehicleNames, //not sent at the moment
	TimeStats,
	ParticipantVehicleNames
}
