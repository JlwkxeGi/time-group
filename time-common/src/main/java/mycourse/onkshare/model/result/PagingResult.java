package mycourse.onkshare.model.result;

/**
 * Created by ww on 2017/2/2.
 */
public class PagingResult extends DataGridResult {

    private Integer pageSize = 10;
    private Integer pageNumber = 1;
    private Integer pageTotal = 0;

    public Integer getPageSize() {
        if (pageSize == null ||pageSize <= 0 ) {
            pageSize = 10;
        }
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNumber() {
        if (pageNumber == null || pageNumber <= 0) {
            pageNumber = 1;
        }
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageTotal() {
        Integer total = getTotal().intValue();
        if (total != null) {
            Integer size = pageSize <= 0 ? 1:pageSize;
            pageTotal = total/size == 0 ? total/size + 1 : total/size;
            return pageTotal;
        }
        return pageTotal;
    }

    public void setPageTotal(Integer pageTotal) {
        this.pageTotal = pageTotal;
    }

}
