package com.huotu.financial.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Administrator on 2016/9/10.
 */
@Getter
@Setter
public class PagingModel {
    private Integer pageNo;
    private Integer pageSize;
    private Integer totalPage;
    private Integer recordCount;
}
