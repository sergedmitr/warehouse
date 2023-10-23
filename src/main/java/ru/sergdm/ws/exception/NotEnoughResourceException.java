package ru.sergdm.ws.exception;

public class NotEnoughResourceException extends Exception {
	public NotEnoughResourceException() {
	}
	
	public NotEnoughResourceException(String msg) {
		super(msg);
	}
}
