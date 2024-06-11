package francisco.equipment.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import francisco.dto.EquipmentDTO;
import francisco.dto.EquipmentNewDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class EquipmentIntegrationTest {

	private ObjectMapper mapper = new ObjectMapper();
	private final String equipmentURL = "http://localhost:8080/api/equipment";

	// POST - EQUIPMENT
	// caso de sucesso - criando novo objeto
	@Test
	public void testPostEquipment() {
		
		Date data = new Date();
		
		EquipmentNewDTO equipment =
				new EquipmentNewDTO(1L, "Equipment 1", "PRovider 1", data, 3.0f, true);
		
		Response resp = RestAssured
				.given()
				.contentType(ContentType.JSON)
				.body(equipment)
				.post(equipmentURL);
		assertEquals(HttpStatus.CREATED.value(), resp.getStatusCode());
	}
	
	// caso de erro - objeto já criado
	@Test
	public void testPostEquipmentWhenObjectAlreadyExists() {
		
		Date data = new Date();
		
	    EquipmentNewDTO newEquipment =
	    		new EquipmentNewDTO(2L, "Equipment 2", "PRovider 2", data, 3.0f, true);
	    
	    Response creationResponse = RestAssured
	    		.given()
	            .contentType(ContentType.JSON)
	            .body(newEquipment)
	            .post(equipmentURL);
	    
	    assertEquals(HttpStatus.CREATED.value(),
	    		creationResponse.getStatusCode());
	    
	    Response errorResponse = RestAssured
	            .given()
	            .contentType(ContentType.JSON)
	            .body(newEquipment)
	            .post(equipmentURL);
	    
	    assertEquals(HttpStatus.BAD_REQUEST.value(),
	    		errorResponse.getStatusCode());
	}
	
	// caso de erro - body inválido
	@Test
	public void testPostEquipmentWithInvalidParameters() {
		
		Date data = new Date();
		
	    EquipmentNewDTO invalidEquipment =
	    		new EquipmentNewDTO(3L, "Provider 3", data, 3.0f);
	    
	    Response resp = RestAssured
	            .given()
	            .contentType(ContentType.JSON)
	            .body(invalidEquipment)
	            .post(equipmentURL);
	    
	    assertEquals(HttpStatus.BAD_REQUEST.value(), resp.getStatusCode());
	}
	
	// UPDATE - EQUIPMENT
	// caso de sucesso - fazendo update no campo active
	@Test
	public void testUpdateEquipmentStatus() {
		
		Date data = new Date();
		
	    EquipmentNewDTO newEquipment = new EquipmentNewDTO(4L, "Equipment 4", "PRovider 4", data, 3.0f, true);
	    
	    Response creationResponse = RestAssured
	            .given()
	            .contentType(ContentType.JSON)
	            .body(newEquipment)
	            .post(equipmentURL);
	    
	    assertEquals(HttpStatus.CREATED.value(), creationResponse.getStatusCode());
	    
	    EquipmentDTO updateEquipment = new EquipmentDTO(4L, "Equipment 4 changed", "Provider 4", data, 3.0f, false);
	    
	    Response updateResponse = RestAssured
	            .given()
	            .contentType(ContentType.JSON)
	            .body(updateEquipment)
	            .put(equipmentURL + "/1");
	    
	    assertEquals(HttpStatus.OK.value(), updateResponse.getStatusCode());
	    
	    Response getResponse = RestAssured.get(equipmentURL + "/1");
	    boolean isActive = getResponse.jsonPath().getBoolean("active");
	    assertFalse(isActive);
	}


}
