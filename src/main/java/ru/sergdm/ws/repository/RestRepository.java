package ru.sergdm.ws.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import ru.sergdm.ws.model.Rest;

public interface RestRepository extends CrudRepository<Rest, Long>,
		JpaSpecificationExecutor<Rest> {
	Rest findByProductId(String productId);
}
