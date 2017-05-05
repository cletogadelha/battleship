package com.cletogadelha;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.cletogadelha.controller.GameController;
import com.cletogadelha.service.GameService;

@RunWith(SpringRunner.class)
@WebMvcTest(GameController.class)
public class GameControllerTest {
	
	 @Autowired
	 MockMvc mockMvc;
	 
	 @MockBean
	 GameService gameServiceMock;
	 
	 @Test
	 public void getGameTest() throws Exception {
	 }
	 

}
