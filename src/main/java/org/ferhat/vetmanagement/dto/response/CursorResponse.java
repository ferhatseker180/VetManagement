package org.ferhat.vetmanagement.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class CursorResponse<T> {

    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private List<T> items;
}
