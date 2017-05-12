package com.cletogadelha.domain;

import com.cletogadelha.domain.enums.ShipStatusEnum;
import com.cletogadelha.domain.enums.Ships;

public class ShipStatus {
	
	private Ships ship;
	private ShipStatusEnum status;

	public Ships getShip() {
		return ship;
	}

	public void setShip(Ships ship) {
		this.ship = ship;
	}

	public ShipStatusEnum getStatus() {
		return status;
	}

	public void setStatus(ShipStatusEnum status) {
		this.status = status;
	}

}
