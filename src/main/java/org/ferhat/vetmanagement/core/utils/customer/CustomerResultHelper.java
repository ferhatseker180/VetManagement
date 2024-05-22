package org.ferhat.vetmanagement.core.utils.customer;

import org.ferhat.vetmanagement.core.result.Result;
import org.ferhat.vetmanagement.core.result.ResultData;
import org.ferhat.vetmanagement.core.utils.ResultHelper;
import org.ferhat.vetmanagement.dto.response.CursorResponse;
import org.springframework.data.domain.Page;

public class CustomerResultHelper {

    public static <T> ResultData<T> created(T data) {
        return new ResultData<>(true, CustomerMessage.CREATED, "201", data);
    }

    public static <T> ResultData<T> validateError(T data) {
        return new ResultData<>(false, CustomerMessage.VALIDATE_ERROR, "400", data);
    }

    public static <T> ResultData<T> success(T data) {
        return new ResultData<>(true, CustomerMessage.OK, "200", data);
    }

    public static Result ok() {
        return new Result(true, CustomerMessage.OK, "200");
    }

    public static Result resultNotFoundError(String message) {
        return new Result(false, message, "404");
    }


    public static <T> ResultData<CursorResponse<T>> cursor(Page<T> pageData) {
        CursorResponse<T> cursor = new CursorResponse<>();
        cursor.setItems(pageData.getContent());
        cursor.setPageNumber(pageData.getNumber());
        cursor.setPageSize(pageData.getSize());
        cursor.setTotalElements(pageData.getTotalElements());
        return ResultHelper.success(cursor);
    }
}