package app.domain;

import java.util.Calendar;
import java.util.Date;

/**
 * @author felipesoler - 2017
 * @project simple-bank
 */
public class OperationResult<T> {

	private OperationType type;
	private Date timestamp;
	private T result;

	public OperationResult(OperationType type, T amount) {
		this.result = amount;
		this.type = type;
		this.timestamp = Calendar.getInstance().getTime();
	}

	public T getResult() {
		return result;
	}

	public String getType() {
		return type.getValue();
	}

	public Date getTimestamp() {
		return timestamp;
	}
}
