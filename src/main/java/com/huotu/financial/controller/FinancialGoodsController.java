/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.financial.controller;

import com.huotu.financial.entity.FinancialBuyFlow;
import com.huotu.financial.entity.FinancialGoods;
import com.huotu.financial.entity.FinancialProfit;
import com.huotu.financial.enums.FinancialStatus;
import com.huotu.financial.exceptions.UserException;
import com.huotu.financial.model.BuyFlowModel;
import com.huotu.financial.model.GoodsModel;
import com.huotu.financial.repository.FinancialBuyFlowRepository;
import com.huotu.financial.repository.FinancialGoodsRepository;
import com.huotu.financial.repository.FinancialProfitRepository;
import com.huotu.financial.service.CommonConfigsService;
import com.huotu.financial.service.FinancialBuyFlowService;
import com.huotu.financial.service.FinancialGoodsService;
import com.huotu.financial.util.RestUtil;
import com.huotu.financial.util.support.BasicNameValuePair;
import com.huotu.huobanplus.common.dataService.GoodsService;
import com.huotu.huobanplus.common.entity.Goods;
import com.huotu.huobanplus.sdk.mall.annotation.CustomerId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by Administrator on 2016/8/29.
 */
@Controller
@RequestMapping(value = "/financialGoods")
public class FinancialGoodsController {

    private static final int list_pageSize = 20;
    private static final int list_page = 1;
    private static final String sign_and = "&&";

    @Autowired
    private FinancialGoodsRepository financialGoodsRepository;
    @Autowired
    private CommonConfigsService commonConfigsService;
    //    @Autowired
//    private GoodsRepository goodsRepository;
    @Autowired
    private FinancialBuyFlowService financialBuyFlowService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private FinancialBuyFlowRepository financialBuyFlowRepository;
    @Autowired
    private FinancialProfitRepository financialProfitRepository;
    //    @Autowired
//    private UserService userService;
    @Autowired
    private FinancialGoodsService financialGoodsService;

    /**
     * 理财列表页
     *
     * @param customerId 商户id
     * @return /manage/index.html
     * @throws IOException
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index(@CustomerId Long customerId, @RequestParam(required = false) Integer page,
                              @RequestParam(required = false) Integer pageSize) throws IOException {
        if (Objects.isNull(page))
            page = 1;
        if (Objects.isNull(pageSize))
            pageSize = 20;
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Pageable pageable = new PageRequest(page - 1, pageSize, sort);
        Page<FinancialGoods> pages = financialGoodsRepository.findAllByCustomerId(customerId, pageable);
        Long count = pages.getTotalElements();
        int pageCount = Integer.parseInt(count.toString()) / pageSize + 1;
        return RestUtil.success("/manage/index",
                new BasicNameValuePair("customerId", customerId),
                new BasicNameValuePair("total", pages.getTotalElements()),
                new BasicNameValuePair("pageSize", pageSize),
                new BasicNameValuePair("page", page),
                new BasicNameValuePair("pageCount", pageCount),
                new BasicNameValuePair("url", getIndexURL() + "?page="),
                new BasicNameValuePair("list", pages.getContent()));
    }

    /**
     * 新增活动页面
     *
     * @param customerId 商户id
     * @return /financial/financialPage.html
     * @throws IOException
     */
    @RequestMapping(value = "/addPage", method = RequestMethod.GET)
    public ModelAndView addPage(@CustomerId Long customerId) throws IOException {
        return RestUtil.success("/manage/financialPage",
                new BasicNameValuePair("customerId", customerId),
                new BasicNameValuePair("isEdit", false));
    }

    /**
     * 编辑活动页面
     *
     * @param customerId 商户id
     * @return /manage/financialPage.html
     * @throws IOException
     */
    @RequestMapping(value = "/editPage", method = RequestMethod.GET)
    public ModelAndView editPage(@CustomerId Long customerId, @RequestParam Long id) throws IOException {
        FinancialGoods goods = financialGoodsRepository.findOne(id);
        return RestUtil.success("/manage/financialPage",
                new BasicNameValuePair("customerId", customerId),
                new BasicNameValuePair("financial", goods),
                new BasicNameValuePair("isEdit", true));
    }

    /**
     * 保存理财活动
     *
     * @param id           活动id
     * @param customerId   商户id
     * @param title        活动名称
     * @param rate         日利率
     * @param redeemPeriod 赎回周期
     * @return 保存结果
     * @throws IOException
     */
    @Transactional
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap save(
            @RequestParam Long id, @RequestParam Long customerId, @RequestParam String title,
            @RequestParam BigDecimal rate, @RequestParam int redeemPeriod
    ) throws IOException {
        FinancialGoods financialGoods = financialGoodsRepository.findOne(id);
        if (Objects.isNull(financialGoods))
            financialGoods = new FinancialGoods();
        financialGoods.setId(id);
        financialGoods.setCustomerId(customerId);
        financialGoods.setTitle(title);
        financialGoods.setRate(rate);
        financialGoods.setRedeemPeriod(redeemPeriod);
        financialGoods.setCreateTime(new Date());
        financialGoodsRepository.save(financialGoods);
        ModelMap map = new ModelMap();
        map.addAttribute("success", true);
        map.addAttribute("url", getIndexURL());
        return map;
    }

//    /**
//     * 删除理财活动
//     *
//     * @param id 活动id
//     * @return
//     * @throws IOException
//     */
//    @RequestMapping(value = "/delete", method = RequestMethod.POST)
//    @ResponseBody
//    public ModelMap delete(@RequestParam Long id) throws IOException {
//        financialGoodsRepository.delete(id);
//        ModelMap map = new ModelMap();
//        return RestUtil.success(null, new BasicNameValuePair("success", true),
//                new BasicNameValuePair("url", getIndexURL()));
//    }

    /**
     * 得到列表URL
     *
     * @return
     */
    private String getIndexURL() {
        String webURL = commonConfigsService.getWebUrl();
        return webURL + "/financialGoods/index";
    }

    /**
     * 检查该商品是否被之前的活动使用过
     *
     * @param id 商户id
     * @return true:未被使用过，false：已使用
     * @throws IOException
     */
    @RequestMapping(value = "/checkGoodsUsed", method = RequestMethod.GET)
    @ResponseBody
    public boolean checkGoodsUsed(@RequestParam Long id) throws IOException {
        Long count = financialGoodsRepository.countById(id);
        return count.intValue() == 0;
    }

    /**
     * 查询商品列表
     *
     * @param customerId 商户id
     * @return 商品列表
     * @throws IOException
     */
    @SuppressWarnings("SpellCheckingInspection")
    @RequestMapping(value = "/getGoodsList", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public ModelMap getGoodsList(@CustomerId Long customerId, @RequestParam(value = "page", required = false) Integer page,
                                 @RequestParam(value = "pagesize", required = false) Integer pageSize) throws IOException {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        if (Objects.isNull(page)) page = list_page;
        if (Objects.isNull(pageSize)) pageSize = list_pageSize;
        Pageable pageable = new PageRequest(page - 1, pageSize, sort);
//        Page<Goods> pages = goodsRepository.findByOwner_Id(customerId, pageable);
        Page<Goods> pages = goodsService.findNormalEnabledByTitleAndCategory(customerId, 99, null, null, false, pageable);
        List<GoodsModel> list = changeDomainToModelList(pages.getContent());

        Long count = pages.getTotalElements();
        int pageCount = Integer.parseInt(count.toString()) / pageSize + 1;
        ModelMap map = new ModelMap();
        map.addAttribute("Total", pages.getTotalElements());
        map.addAttribute("PageSize", pageSize);
        map.addAttribute("Rows", list);
        map.addAttribute("PageIndex", page);
        map.addAttribute("PageCount", pageCount);
        return map;
    }

    private List<GoodsModel> changeDomainToModelList(List<Goods> domains) {
        List<GoodsModel> list = new ArrayList<>();
        for (Goods goods : domains) {
            GoodsModel model = new GoodsModel();
            model.setId(goods.getId());
            model.setPrice(goods.getPrice());
            model.setStock(goods.getStock());
            model.setTitle(goods.getTitle());
            list.add(model);
        }
        return list;
    }


    /**
     * 用户理财产品详情页
     *
     * @param customerId 商户id
     * @param id         商品id
     * @return /manage/buyFlowIndex.html
     * @throws IOException
     */
    @RequestMapping(value = "/buyFlowIndex", method = RequestMethod.GET)
    public ModelAndView buyFlowIndex(@CustomerId Long customerId, @RequestParam Long id,
                                     @RequestParam(required = false) String no,
                                     @RequestParam(required = false) Integer page,
                                     @RequestParam(required = false) Integer pageSize) throws IOException, UserException {
        Sort sort = new Sort(Sort.Direction.DESC, "buyTime");
        if (Objects.isNull(page)) page = list_page;
        if (Objects.isNull(pageSize)) pageSize = list_pageSize;
        Pageable pageable = new PageRequest(page - 1, pageSize, sort);
        Page<FinancialBuyFlow> pages = financialBuyFlowService.findAllByCustomerIdAndGoodIdAndNo(customerId, id, no, pageable);
        List<BuyFlowModel> list = financialBuyFlowService.changeDomainToModelList(pages.getContent());
        Long count = pages.getTotalElements();
        int pageCount = Integer.parseInt(count.toString()) / pageSize + 1;
        String url = commonConfigsService.getWebUrl() +
                "/financialGoods/buyFlowIndex?id=" + id;
        String rootURL = no == null ? url : url + "&&no=" + no;
        return RestUtil.success("/manage/buyFlowIndex", new BasicNameValuePair("customerId", customerId),
                new BasicNameValuePair("total", pages.getTotalElements()),
                new BasicNameValuePair("pageSize", pageSize),
                new BasicNameValuePair("page", page),
                new BasicNameValuePair("pageCount", pageCount),
                new BasicNameValuePair("url", rootURL + "&&page="),
                new BasicNameValuePair("searchUrl", url + "&&no="),
                new BasicNameValuePair("list", list));
    }

    /**
     * 根据用户和理财期号查询用户每日流水列表
     *
     * @param page     页码
     * @param pageSize 每日条数
     * @param no       期号
     * @return 流水列表
     * @throws IOException
     */
    @RequestMapping(value = "/getPageProfit", method = RequestMethod.GET)
    public ModelAndView getPageProfit(@RequestParam(required = false) Integer page,
                                      @RequestParam(required = false) Integer pageSize,
                                      @RequestParam Long goodsId,
                                      @RequestParam String no) throws IOException {
        Sort sort = new Sort(Sort.Direction.DESC, "time");
        if (Objects.isNull(page)) page = list_page;
        if (Objects.isNull(pageSize)) pageSize = list_pageSize;
        Pageable pageable = new PageRequest(page - 1, pageSize, sort);
        Page<FinancialProfit> pages = financialProfitRepository.findAllByNo(no, pageable);
        Long count = pages.getTotalElements();
        int pageCount = Integer.parseInt(count.toString()) / pageSize + 1;
        String url = commonConfigsService.getWebUrl() +
                "/financialGoods/getPageProfit?no=" + no;
        return RestUtil.success("/manage/profitIndex",
                new BasicNameValuePair("total", pages.getTotalElements()),
                new BasicNameValuePair("pageSize", pageSize),
                new BasicNameValuePair("page", page),
                new BasicNameValuePair("goodsId", goodsId),
                new BasicNameValuePair("pageCount", pageCount),
                new BasicNameValuePair("url", url + "&&page="),
                new BasicNameValuePair("list", pages.getContent()));
    }

    /**
     * 同意理财赎回操作
     *
     * @param no
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/updateFlowStatus", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public ModelMap updateFlowStatus(@RequestParam String no) throws IOException {
        return financialGoodsService.updateFlowStatus(no);

    }

//    @RequestMapping(value = "/redeemList", method = RequestMethod.GET)
//    public String redeemList(@CustomerId Long customerId, Model model) {
//        model.addAttribute("customerId", customerId);
//        return "manage/redeemlist";
//    }

    @RequestMapping(value = "/redeemList", method = RequestMethod.GET)
    public ModelAndView redeemList(@CustomerId Long customerId,
                                   @RequestParam(required = false) String no,
                                   @RequestParam(required = false) Integer page,
                                   @RequestParam(required = false) Integer pageSize) throws IOException, UserException {
        Sort sort = new Sort(Sort.Direction.DESC, "buyTime");
        if (Objects.isNull(page)) page = list_page;
        if (Objects.isNull(pageSize)) pageSize = list_pageSize;
        Pageable pageable = new PageRequest(page - 1, pageSize, sort);
        Page<FinancialBuyFlow> pages = financialBuyFlowService.findAllByCustomerIdAndNoAndStatus(customerId, no,
                FinancialStatus.DOING, pageable);
        List<BuyFlowModel> list = financialBuyFlowService.changeDomainToModelList(pages.getContent());
        Long count = pages.getTotalElements();
        int pageCount = Integer.parseInt(count.toString()) / pageSize + 1;
        String url = commonConfigsService.getWebUrl() +
                "/financialGoods/redeemList";
        String rootURL = no == null ? url + "?page=" : url + "?no=" + no + "&&page=";
        return RestUtil.success("manage/redeemlist", new BasicNameValuePair("customerId", customerId),
                new BasicNameValuePair("total", pages.getTotalElements()),
                new BasicNameValuePair("pageSize", pageSize),
                new BasicNameValuePair("page", page),
                new BasicNameValuePair("pageCount", pageCount),
                new BasicNameValuePair("url", rootURL),
                new BasicNameValuePair("searchUrl", url + "?no="),
                new BasicNameValuePair("list", list));
    }

//    @RequestMapping(value = "/redeemlist.do")
//    @ResponseBody
//    public ViewRedeemListPageModel redeemListDo(@RequestParam(required = false) Integer page,
//                                                @RequestParam(required = false) Integer pageSize,
//                                                @RequestParam(required = false) String no) {
//
//        Sort sort = new Sort(Sort.Direction.DESC, "buyTime");
//        if (Objects.isNull(page)) page = list_page;
//        if (Objects.isNull(pageSize)) pageSize = list_pageSize;
//        Pageable pageable = new PageRequest(page - 1, pageSize, sort);
//        Page<FinancialBuyFlow> pages = financialBuyFlowService.findAllByCustomerIdAndGoodIdAndNo(customerId, id, no, pageable);
//        ViewRedeemListPageModel viewRedeemListPageModel = new ViewRedeemListPageModel();
//        PagingModel pagingModel = new PagingModel();
//        pagingModel.setPageNo(page);
//        pagingModel.setPageSize(pageSize);
//        pagingModel.setRecordCount(pages.getTotalElements());
//        pagingModel.setTotalPage(6);
//        viewRedeemListPageModel.setPage(pagingModel);
//
//        List<ViewRedeemListModel> list = new ArrayList<>();
//        list.add(new ViewRedeemListModel("001", "lgh", "20%利润", new Date()));
//        list.add(new ViewRedeemListModel("002", "lgh", "20%利润", new Date()));
//        list.add(new ViewRedeemListModel("003", "lgh", "20%利润", new Date()));
//        list.add(new ViewRedeemListModel("004", "lgh", "20%利润", new Date()));
//        list.add(new ViewRedeemListModel("005", "lgh", "20%利润", new Date()));
//
//        viewRedeemListPageModel.setList(list);
//        return viewRedeemListPageModel;
//    }
}
