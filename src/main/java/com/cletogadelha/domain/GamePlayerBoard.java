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
	
}
