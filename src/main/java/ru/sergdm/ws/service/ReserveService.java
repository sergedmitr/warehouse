package ru.sergdm.ws.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sergdm.ws.enums.ReserveStatuses;
import ru.sergdm.ws.exception.ResourceNotExpectedException;
import ru.sergdm.ws.exception.ResourceNotFoundException;
import ru.sergdm.ws.exception.WrongStatusException;
import ru.sergdm.ws.model.Reserve;
import ru.sergdm.ws.repository.ReserveRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReserveService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private ReserveRepository reserveRepository;

	public List<Reserve> getReserves() {
		List<Reserve> reserves = new ArrayList<>();
		reserveRepository.findAll().forEach(reserves::add);
		System.out.println("reserves = " + reserves);
		return reserves;
	}

	public Reserve getReserve(Long reserveId) throws ResourceNotFoundException{
		Reserve delivery = reserveRepository.findById(reserveId)
				.orElseThrow(() -> new ResourceNotFoundException("Reserve not found"));
		logger.info("reserve = {}", delivery);
		return delivery;
	}

	public Reserve addReserve(Reserve reserve) {
		reserve.setStatus(ReserveStatuses.CREATED);
		return reserveRepository.save(reserve);
	}

	public Long getReserved(String productId) {
		List<Reserve> reserves = reserveRepository.findByProductId(productId);
		Long rest = reserves.stream()
				.filter(e -> e.getStatus() == ReserveStatuses.CREATED)
				.map(m -> m.getAmount())
				.reduce(0L,
				(subtotal, e) -> subtotal + e);
		return rest;
	}

	public Reserve shipReserve(Long reserveId, Long orderId) throws ResourceNotFoundException, ResourceNotExpectedException,
			WrongStatusException {
		Reserve reserve = reserveRepository.findById(reserveId)
				.orElseThrow(() -> new ResourceNotFoundException("Reserve not found"));
		if (!reserve.getOrderId().equals(orderId)) {
			throw new ResourceNotExpectedException("Reserve with other orderId");
		}
		if (reserve.getStatus() != ReserveStatuses.CREATED) {
			throw new WrongStatusException("Wrong status = " + reserve.getStatus() + " for ship operation");
		}
		reserve.setStatus(ReserveStatuses.SHIPPED);
		reserveRepository.save(reserve);
		return reserve;
	}

	public void cancelReserve(Long reserveId, Long orderId) throws ResourceNotFoundException, ResourceNotExpectedException,
			WrongStatusException {
		Reserve reserve = reserveRepository.findById(reserveId)
				.orElseThrow(() -> new ResourceNotFoundException("Reserve not found"));
		if (!reserve.getOrderId().equals(orderId)) {
			throw new ResourceNotExpectedException("Reserve with other orderId");
		}
		if (reserve.getStatus() != ReserveStatuses.CREATED) {
			throw new WrongStatusException("Wrong status = " + reserve.getStatus() + " for ship operation");
		}
		reserve.setStatus(ReserveStatuses.CANCELLED);
		reserveRepository.save(reserve);
	}

	public Reserve returnReserve(Long reserveId, Long orderId) throws ResourceNotFoundException, ResourceNotExpectedException,
			WrongStatusException {
		Reserve reserve = reserveRepository.findById(reserveId)
				.orElseThrow(() -> new ResourceNotFoundException("Reserve not found"));
		if (!reserve.getOrderId().equals(orderId)) {
			throw new ResourceNotExpectedException("Reserve with other orderId");
		}
		if (reserve.getStatus() != ReserveStatuses.SHIPPED) {
			throw new WrongStatusException("Wrong status = " + reserve.getStatus() + " for ship operation");
		}
		reserve.setStatus(ReserveStatuses.RETURNED);
		reserveRepository.save(reserve);
		return reserve;
	}

	public void deleteAll() {
		reserveRepository.deleteAll();
	}
}
