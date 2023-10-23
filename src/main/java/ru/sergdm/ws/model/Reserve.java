package ru.sergdm.ws.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import ru.sergdm.ws.enums.ReserveStatuses;

@Entity
@Table(name = "reserves")
public class Reserve {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long reserveId;
	@Column(nullable = false)
	String productId;
	@Column(nullable = false)
	Long orderId;
	@Column(nullable = false)
	Long amount;
	@Column(nullable = false)
	ReserveStatuses status;

	public Long getReserveId() {
		return reserveId;
	}

	public void setReserveId(Long reserveId) {
		this.reserveId = reserveId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public ReserveStatuses getStatus() {
		return status;
	}

	public void setStatus(ReserveStatuses status) {
		this.status = status;
	}
}
