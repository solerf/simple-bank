package app.domain;

/**
 * @author felipesoler - 2017
 * @project simple-bank
 */
public enum OperationType {

	DEPOSIT("Deposit"),
	WITHDRAW("Withdraw"),
	STATEMENT("Statement")
	;

	private String movementType;

	OperationType(String movementType) {
		this.movementType = movementType;
	}

	public String getValue(){
		return this.movementType;
	}
}
