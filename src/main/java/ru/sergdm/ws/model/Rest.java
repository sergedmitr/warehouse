package ru.sergdm.ws.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "rests")
public class Rest {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long restId;
	@Column(nullable = false)
	private String productId;
	@Column(nullable = false)
	private Long amount;
	@Transient
	private Long reserved;

	public Long getRestId() {
		return restId;
	}

	public void setRestId(Long restId) {
		this.restId = restId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Long getReserved() {
		return reserved;
	}

	public void setReserved(Long reserved) {
		this.reserved = reserved;
	}

	@Override
	public String toString() {
		return "Rest{" +
				"restId=" + restId +
				", productId='" + productId + '\'' +
				", amount=" + amount +
				", reserved=" + reserved +
				'}';
	}
}
