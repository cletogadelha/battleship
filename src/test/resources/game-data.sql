SET REFERENTIAL_INTEGRITY FALSE;

--Players--

INSERT INTO player(player_id, created, updated, version, losses, name, score, wins)
	VALUES ('1', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 0, 0, 'Player 1', 0, 0);
	
INSERT INTO player(player_id, created, updated, version, losses, name, score, wins)
	VALUES ('2', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 0, 0, 'Player 2', 0, 0);
	
--Boards--

INSERT INTO board( board_id, created, updated, version, finished_placement) 
	VALUES (1, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 0, 'false');

INSERT INTO board( board_id, created, updated, version, finished_placement) 
	VALUES (2, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 0, 'true');	
	
INSERT INTO board( board_id, created, updated, version, finished_placement) 
	VALUES (3, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 0, 'true');	
	
INSERT INTO board( board_id, created, updated, version, finished_placement) 
	VALUES (4, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 0, 'true');			
	
--Games--

INSERT INTO game( game_id, created, updated, version, game_status, turn_holder, player_winner) 
	VALUES ('1', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 0, 'WAITING_OPPONENT', null, null);

INSERT INTO game( game_id, created, updated, version, game_status, turn_holder, player_winner) 
	VALUES ('2', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 0, 'SETUP_PHASE', null, null);
	
INSERT INTO game( game_id, created, updated, version, game_status, turn_holder, player_winner) 
	VALUES ('3', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 0, 'WAITING_FLIP_COIN', null, null);
	
INSERT INTO game( game_id, created, updated, version, game_status, turn_holder, player_winner) 
	VALUES ('4', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 0, 'IN_PROGRESS', '1', null);
	
INSERT INTO game( game_id, created, updated, version, game_status, turn_holder, player_winner) 
	VALUES ('5', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 0, 'PAUSED', '1', null);
	
--Inserting player1 on the game
INSERT INTO game_player_board( created, updated, version, player_id, board_id, game_id) 
	VALUES (CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 0, 1, 1, 1);
	
INSERT INTO game_player_board( created, updated, version, player_id, board_id, game_id) 
	VALUES (CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 0, 1, 1, 2);
	
INSERT INTO game_player_board( created, updated, version, player_id, board_id, game_id) 
	VALUES (CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 0, 1, 1, 3);
	
INSERT INTO game_player_board( created, updated, version, player_id, board_id, game_id) 
	VALUES (CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 0, 2, 2, 3);	

INSERT INTO game_player_board( created, updated, version, player_id, board_id, game_id) 
	VALUES (CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 0, 1, 3, 4);
	
INSERT INTO game_player_board( created, updated, version, player_id, board_id, game_id) 
	VALUES (CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 0, 2, 4, 4);

--BoardPlacement
INSERT INTO board_placement( id, created, updated, version, damage, direction, ship, board_id) 
	VALUES ('1', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 0, 0, 'VERTICAL', 'SUBMARINE','1');
	
INSERT INTO board_placement( id, created, updated, version, damage, direction, ship, board_id) 
	VALUES ('3', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 0, 0, 'VERTICAL', 'SUBMARINE','3');
	
INSERT INTO board_placement( id, created, updated, version, damage, direction, ship, board_id) 
	VALUES ('4', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 0, 0, 'HORIZONTAL', 'SUBMARINE','4');		

--Placement-Coordinate
INSERT INTO placement_coordinate( placement_id, coordinate_id) 
	VALUES ('1', '1'); --coordinate A1
	
INSERT INTO placement_coordinate( placement_id, coordinate_id) 
	VALUES ('3', '1'); --coordinate A1
	
INSERT INTO placement_coordinate( placement_id, coordinate_id) 
	VALUES ('4', '1'); --coordinate A1		

INSERT INTO move( id, created, updated, version, coordinate_id, player_id, game_id) 
	VALUES (1, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 0, 2, 1, 4);
	
	
--------------------------------WIN GAME---------------------------------------------

INSERT INTO game( game_id, created, updated, version, game_status, turn_holder, player_winner) 
	VALUES ('100', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 0, 'IN_PROGRESS', '1', null);
		
INSERT INTO board( board_id, created, updated, version, finished_placement) 
	VALUES (101, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 0, 'true');		
	
INSERT INTO game_player_board( created, updated, version, player_id, board_id, game_id) 
	VALUES (CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 0, 2, 101, 100);
	
INSERT INTO board_placement( id, created, updated, version, damage, direction, ship, board_id) 
	VALUES ('100', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 0, 1, 'HORIZONTAL', 'DESTROYER', '101');

INSERT INTO placement_coordinate( placement_id, coordinate_id) 
	VALUES ('100', '2'); 	
	
INSERT INTO board_placement( id, created, updated, version, damage, direction, ship, board_id) 
	VALUES ('101', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 0, 3, 'VERTICAL', 'SUBMARINE', '101');
	
INSERT INTO board_placement( id, created, updated, version, damage, direction, ship, board_id) 
	VALUES ('102', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 0, 3, 'HORIZONTAL', 'CRUISER', '101');
	
INSERT INTO board_placement( id, created, updated, version, damage, direction, ship, board_id) 
	VALUES ('103', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 0, 4, 'VERTICAL', 'BATTLESHIP', '101');
	
INSERT INTO board_placement( id, created, updated, version, damage, direction, ship, board_id) 
	VALUES ('104', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 0, 5, 'HORIZONTAL', 'AIRCRAFT_CARRIER', '101');
	
		
	
		

