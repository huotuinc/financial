/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

var goodsListModule = $("#goodsListModule").modal("挑选商品", function () {
    //确定按钮触发的事件
    goodsListModule.hide();
}, {width: '500px', height: '500px'});
var goodsWin;

$(function () {

    $("#goodsList").Grid({
        method: 'POST',//提交方式GET|POST
        height: 'auto',//高度
        showNumber: false,//是否显示
        page: 1,
        pageSize: 20,
        pagerCount: 10,
        pageDetail: true,
        showNumber: true,
        url: '../financialGoods/getGoodsList',//数据来源Url|通过mobel自定义属性配置
        rows: [
            {width: '25%', field: 'title', title: '标题', align: 'left'},
            {
                width: '20%', field: 'price', title: '销售价', align: 'left'
            },
            {
                width: '20%', field: 'stock', title: '库存', align: 'left'
            },
            {
                width: '20%', field: 'caozuo', title: '操作', align: 'left', formatter: function (value, rowData) {
                return '<a href="javascript:void(0)" goodsName="' + rowData.title + '" goodsId="' + rowData.id + '" ' +
                    'onclick="choiceGoods(this)">选择</a>';
            }
            }
        ]
    });

    //var table = $("#goodsListTable").dataTable({
    //    language:lang, //提示信息
    //    autoWidth: false, //禁用自动调整列宽
    //    stripeClasses: ["odd", "even"], //为奇偶行加上样式，兼容不支持CSS伪类的场合
    //    processing: true, //隐藏加载提示,自行处理
    //    serverSide: true, //启用服务器端分页
    //    searching: false, //禁用原生搜索
    //    orderMulti: false, //启用多列排序
    //    order: [], //取消默认排序查询,否则复选框一列会出现小箭头
    //    renderer: "bootstrap", //渲染样式：Bootstrap和jquery-ui
    //    pagingType: "simple_numbers", //分页样式：simple,simple_numbers,full,full_numbers
    //    columnDefs: [{
    //        "targets": 'nosort', //列的样式名
    //        "orderable": false //包含上样式名‘nosort'的禁止排序
    //    }],
    //    ajax: function (data, callback, settings) {
    //        //封装请求参数
    //        var param = {};
    //        param.limit = data.length;//页面显示记录条数，在页面显示每页显示多少项的时候
    //        param.start = data.start;//开始的记录序号
    //        param.page = (data.start / data.length)+1;//当前页码
    //        $.ajax({
    //            type: "GET",
    //            url: "../financialGoods/getGoodsList",
    //            cache: false, //禁用缓存
    //            data: param, //传入组装的参数
    //            dataType: "json",
    //            success: function (result) {
    //                var returnData = {};
    //                returnData.draw = data.draw;//这里直接自行返回了draw计数器,应该由后台返回
    //                returnData.recordsTotal = result.total;//返回数据全部记录
    //                returnData.recordsFiltered = result.total;//后台不实现过滤功能，每次查询均视作全部结果
    //                returnData.data = result.data;//返回的数据列表
    //                //调用DataTables提供的callback方法，代表数据已封装完成并传回DataTables进行渲染
    //                //此时的数据需确保正确无误，异常判断应在执行此回调前自行处理完毕
    //                callback(returnData);
    //            }
    //        });
    //    },
    //    //列表表头字段
    //    columns: [
    //        { "data": "id" },
    //        //{ "data": "Name" },
    //        //{ "data": "Sex" }
    //    ]
    //}).api();
});

function showGoodsList() {
    goodsHandler.openGoodsWin();
}

var goodsHandler = {
    openGoodsWin: function () {
        var title = "挑选商品";
        goodsWin = layer.open({
            type: 1,
            title: title,
            shadeClose: true,
            shade: false,
            //maxmin: true, //开启最大化最小化按钮
            area: ['900px', '600px'],
            content: $("#goodsList")
        });
    },
    choiceGoods: function (obj) {

    }
};