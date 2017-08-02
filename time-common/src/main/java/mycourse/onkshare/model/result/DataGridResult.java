package mycourse.onkshare.model.result;

import java.util.Collection;

public class DataGridResult extends Result{
	
	private Number total = 0;
	private Collection<?> rows;
	
	
	public Number getTotal() {
		return total;
	}
	public void setTotal(Number total) {
		this.total = total;
	}
	public Collection<?> getRows() {
		return rows;
	}
	public void setRows(Collection<?> rows) {
		this.rows = rows;
	}
}
