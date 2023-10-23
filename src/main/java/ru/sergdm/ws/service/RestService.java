package ru.sergdm.ws.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sergdm.ws.model.Rest;
import ru.sergdm.ws.repository.RestRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class RestService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private RestRepository restRepository;

	public void addRest(Long amount, String productId) {
		Rest existingRest = restRepository.findByProductId(productId);
		if (existingRest == null) {
			Rest newRest = new Rest();
			newRest.setAmount(amount);
			newRest.setProductId(productId);
			restRepository.save(newRest);
		} else {
			existingRest.setAmount(existingRest.getAmount() + amount);
			restRepository.save(existingRest);
		}
	}

	public List<Rest> getRests() {
		List<Rest> result = new ArrayList<>();
		restRepository.findAll().forEach(e -> result.add(e));
		return result;
	}

	public Rest getRest(String productId) {
		Rest result = restRepository.findByProductId(productId);
		return result;
	}

	public void decreaseRest(String productId, Long subtrahend) {
		logger.info("decreaseRest. productId = {}, subtrahend = {}", productId, subtrahend);
		Rest existingRest = restRepository.findByProductId(productId);
		existingRest.setAmount(existingRest.getAmount() - subtrahend);
		logger.info("decreaseRest. existingRest = {}", existingRest);
		restRepository.save(existingRest);
	}

	public void increaseRest(String productId, Long subtrahend) {
		Rest existingRest = restRepository.findByProductId(productId);
		existingRest.setAmount(existingRest.getAmount() + subtrahend);
		restRepository.save(existingRest);
	}

	public void deleteAll() {
		restRepository.deleteAll();
	}
}
