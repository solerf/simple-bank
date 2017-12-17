package app.controller.response;

/**
 * @author felipesoler - 2017
 * @project simple-bank
 */
public class ApiResponse {

	private Object data;
	private String message;

	private ApiResponse(Object data, String message){
		this.data = data;
		this.message = message;
	}

	public static ApiResponse success(Object data){
		return new ApiResponse(data, "Success");
	}

	public static ApiResponse error(Object data){
		return new ApiResponse(data, "Error");
	}

	public Object getData(){
		return this.data;
	}

	public String getMessage() { return this.message; }
}
