package com.huotu.financial.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by Administrator on 2016/9/10.
 */
@Getter
@Setter
public class ViewRedeemListPageModel {
    private List<ViewRedeemListModel> list;
    private PagingModel page;

}
