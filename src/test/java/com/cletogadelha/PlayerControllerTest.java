package com.cletogadelha;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;

import com.cletogadelha.domain.Player;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts="classpath:data.sql")
public class PlayerControllerTest {
	
	private static final String URL = "/rest/player/";
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Test
	public void shouldReturnPlayer(){
		ResponseEntity<Player> responseEntity = 
				restTemplate.getForEntity(URL + "1", Player.class);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(responseEntity.getBody().getName(), "Test Player");
		assertEquals(responseEntity.getBody().getId(), new Integer(1));
	}
	
//	@Test
//	public void shouldUpdatePlayer(){
//		Player updatedPlayer = new Player();
//		updatedPlayer.setName("Updated Player");
//		updatedPlayer.setId(2);
//		
//		ResponseEntity<Player> responseEntity = 
//				restTemplate.postForEntity(URL, updatedPlayer, Player.class);
//		
//		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//		assertEquals(responseEntity.getBody().getName(), "Updated Player");
//		assertEquals(responseEntity.getBody().getId(), new Integer(2));
//	}

}
