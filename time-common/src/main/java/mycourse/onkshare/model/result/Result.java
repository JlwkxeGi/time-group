package mycourse.onkshare.model.result;

import java.io.Serializable;

public abstract class Result implements Serializable{

	public static final Result SUCCESS = new Result(){
		@Override
		public int getError() {
			return ResultConstant.SUCCESS_INT;
		}
		@Override
		public String getMessage() {
			return "success";
		}
	};

	public static final Result Error = new Result(){
		@Override
		public int getError() {
			return ResultConstant.ERROR_INT;
		}
		@Override
		public String getMessage() {
			return "error";
		}
	};

	public final static Result error(String message) {
		Result error = new MessageResult();
		error.setError(ResultConstant.ERROR_INT);
		if (message == null || message.length() == 0) {
			error.setMessage("error");
		}else {
			error.setMessage(message);
		}
		return error;
	}

	public final static Result success(String message) {
		Result success = new MessageResult();
		success.setError(ResultConstant.SUCCESS_INT);
		if (message == null || message.length() == 0) {
			success.setMessage("success");
		}else {
			success.setMessage(message);
		}
		return success;
	}



	private int error = ResultConstant.SUCCESS_INT;
	private String message = "success";
	
	
	public int getError() {
		return error;
	}
	public void setError(int error) {
		this.error = error;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
