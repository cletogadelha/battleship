package com.cletogadelha.domain;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Transient;

@Entity
@AssociationOverrides({
	@AssociationOverride(name = "pk.player",
		joinColumns = @JoinColumn(name = "PLAYER_ID")),
	@AssociationOverride(name = "pk.game",
		joinColumns = @JoinColumn(name = "GAME_ID")),
	@AssociationOverride(name = "pk.board",
		joinColumns = @JoinColumn(name = "BOARD_ID")) })
public class GamePlayerBoard extends AbstractBaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private GamePlayerBoardId pk = new GamePlayerBoardId(); 
	
	@Transient
	public Player getPlayer() {
		return getPk().getPlayer();
	}
	
	public void setPlayer(Player player) {
		getPk().setPlayer(player);
	}
	
	@Transient
	public Board getBoard() {
		return getPk().getBoard();
	}
	
	public void setBoard(Board board) {
		getPk().setBoard(board);
	}
	
	@Transient
	public Game getGame() {
		return getPk().getGame();
	}
	
	public void setGame(Game game) {
		getPk().setGame(game);
	}

	public GamePlayerBoardId getPk() {
		return pk;
	}

	public void setPk(GamePlayerBoardId pk) {
		this.pk = pk;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((pk == null) ? 0 : pk.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		GamePlayerBoard other = (GamePlayerBoard) obj;
		if (pk == null) {
			if (other.pk != null)
				return false;
		} else if (!pk.equals(other.pk))
			return false;
		return true;
	}
	
}
