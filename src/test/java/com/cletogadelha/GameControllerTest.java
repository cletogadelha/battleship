//package com.cletogadelha;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
//import org.springframework.test.context.jdbc.SqlGroup;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import com.cletogadelha.controller.GameController;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes={GameController.class}, webEnvironment=WebEnvironment.RANDOM_PORT)
//@AutoConfigureTestDatabase
//@SqlGroup({@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts="classpath:gameSample.sql") })
//public class GameControllerTest {
//
//	@Autowired
//	private TestRestTemplate restTemplate;
//	
//	@Test
//	public void getGame(){
////		ResponseEntity<Game> response = 
////				restTemplate.getForEntity("", responseType)
//	}
//	
//}
