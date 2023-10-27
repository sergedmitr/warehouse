package ru.sergdm.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.sergdm.ws.exception.ResourceNotExpectedException;
import ru.sergdm.ws.exception.ResourceNotFoundException;
import ru.sergdm.ws.exception.WrongStatusException;
import ru.sergdm.ws.model.Reserve;
import ru.sergdm.ws.model.Rest;
import ru.sergdm.ws.model.ShipmentRequest;
import ru.sergdm.ws.model.SystemName;
import ru.sergdm.ws.service.ReserveService;
import ru.sergdm.ws.service.RestService;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
public class ApiController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private RestService restService;
	@Autowired
	private ReserveService reserveService;


	@GetMapping("/")
	public ResponseEntity<Object> name() {
		SystemName name = new SystemName();
		return new ResponseEntity<>(name, HttpStatus.OK);
	}

	@GetMapping("/rests")
	public ResponseEntity<List<Rest>> rests(){
		logger.info("rests");
		return new ResponseEntity(restService.getRests(), HttpStatus.OK);
	}

	@DeleteMapping("/rests")
	public ResponseEntity<?> deleteRests(){
		logger.info("Delete all rests");
		restService.deleteAll();
		return ResponseEntity.ok().body(HttpStatus.OK);
	}

	@GetMapping("/rests/{productId}")
	public ResponseEntity<Rest> rest(@PathVariable String productId){
		logger.info("rests. productId = {}", productId);
		Rest rest = restService.getRest(productId);
		rest.setReserved(reserveService.getReserved(productId));
		return new ResponseEntity(rest, HttpStatus.OK);
	}

	@PostMapping("/add-rest")
	public ResponseEntity<?> addRest(@Valid @RequestBody Rest rest) {
		restService.addRest(rest.getAmount(), rest.getProductId());
		return ResponseEntity.ok().body(HttpStatus.OK);
	}

	@GetMapping("/reserves")
	public ResponseEntity<List<Reserve>> reserve(){
		logger.info("reserves");
		return new ResponseEntity(reserveService.getReserves(), HttpStatus.OK);
	}

	@GetMapping("/reserves/{reserveId}")
	public ResponseEntity<?> getReserve(@PathVariable Long reserveId){
		logger.info("GET reserves. reserveId = {}", reserveId);
		try {
			Reserve reserve = reserveService.getReserve(reserveId);
			return new ResponseEntity(reserve, HttpStatus.OK);
		} catch (ResourceNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		}
	}


	@GetMapping("/reserves/{productId}/reserved")
	public ResponseEntity<?> getReserved(@PathVariable String productId){
		logger.info("reserved");
		return new ResponseEntity(reserveService.getReserved(productId), HttpStatus.OK);
	}

	@PostMapping("/reserves")
	public ResponseEntity<?> addReserve(@Valid @RequestBody Reserve reserve) {
		Rest rest = restService.getRest(reserve.getProductId());
		Long restLong = rest == null ? 0 : rest.getAmount() == null ? 0 :rest.getAmount();
		Long reserved = reserveService.getReserved(reserve.getProductId());
		if (restLong - reserved - reserve.getAmount() >= 0) {
			Reserve reserveNew = reserveService.addReserve(reserve);
			return ResponseEntity.ok().body(reserveNew);
		} else {
			return ResponseEntity.status(409).body("Not enough product");
		}
	}

	@DeleteMapping("/reserves")
	public ResponseEntity<?> deleteReserves(){
		logger.info("Delete all reserves");
		reserveService.deleteAll();
		return ResponseEntity.ok().body(HttpStatus.OK);
	}

	@PostMapping("/ship")
	public ResponseEntity<?> ship(@Valid @RequestBody ShipmentRequest request) {
		try {
			Reserve reserve = reserveService.shipReserve(request.getReserveId(), request.getOrderId());
			restService.decreaseRest(reserve.getProductId(), reserve.getAmount());
			return ResponseEntity.ok().body(HttpStatus.OK);
		} catch (ResourceNotExpectedException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
		} catch (ResourceNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		} catch (WrongStatusException ex) {
			return ResponseEntity.status(409).body(ex.getMessage());
		}
	}

	@PostMapping("/cancel")
	public ResponseEntity<?> cancel(@Valid @RequestBody ShipmentRequest request) {
		try {
			reserveService.cancelReserve(request.getReserveId(), request.getOrderId());
			return ResponseEntity.ok().body(HttpStatus.OK);
		} catch (ResourceNotExpectedException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
		} catch (ResourceNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(request);
		} catch (WrongStatusException ex) {
			return ResponseEntity.status(409).body(ex.getMessage());
		}
	}

	@PostMapping("/return")
	public ResponseEntity<?> doReturn(@Valid @RequestBody ShipmentRequest request) {
		try {
			Reserve reserve = reserveService.returnReserve(request.getReserveId(), request.getOrderId());
			restService.increaseRest(reserve.getProductId(), reserve.getAmount());
			return ResponseEntity.ok().body(HttpStatus.OK);
		} catch (ResourceNotExpectedException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
		} catch (ResourceNotFoundException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(request);
		} catch (WrongStatusException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.status(409).body(ex.getMessage());
		}
	}
}
