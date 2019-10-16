package com.challenge.busroute.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BusRouteControllerTests {
	private static final String DEPARTURE_STATION_ID = "1";
	private static final String ARRIVAL_STATION_ID = "2";

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void direct() throws Exception {
		final MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/direct")
				.param("dep_sid", DEPARTURE_STATION_ID).param("arr_sid", ARRIVAL_STATION_ID))
				.andExpect(status().isOk()).andReturn();
		log.info(new String(result.getResponse().getContentAsByteArray()));
	}

}
