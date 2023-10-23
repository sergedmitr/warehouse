package ru.sergdm.ws.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import ru.sergdm.ws.model.Reserve;

import java.util.List;

public interface ReserveRepository extends CrudRepository<Reserve, Long>,
		JpaSpecificationExecutor<Reserve> {
	List<Reserve> findByProductId(String productId);
}
