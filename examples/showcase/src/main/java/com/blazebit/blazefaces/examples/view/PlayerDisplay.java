package com.blazebit.blazefaces.examples.view;

import java.util.Calendar;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import com.blazebit.blazefaces.examples.domain.Player;

@Named
@RequestScoped
public class PlayerDisplay {

	private Player player;

	public PlayerDisplay() {
		player = new Player();
		player.setName("Lionel Messi");
		player.setNationality("Argentinian");
		player.setPhoto("messi.jpg");
		player.setPosition("Forward");
		player.setHeight("1,69 m");
		player.setWeight("67");

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 24);
		calendar.set(Calendar.MONTH, 5);
		calendar.set(Calendar.YEAR, 1987);

		player.setBirth(calendar.getTime());
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}
