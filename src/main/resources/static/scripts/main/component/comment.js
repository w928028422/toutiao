// 点击添加评论
Date.prototype.Format = function(fmt)
{
    var o = {
        "M+" : this.getMonth()+1,                 //月份
        "d+" : this.getDate(),                    //日
        "h+" : this.getHours(),                   //小时
        "m+" : this.getMinutes(),                 //分
        "s+" : this.getSeconds(),                 //秒
        "q+" : Math.floor((this.getMonth()+3)/3), //季度
        "S"  : this.getMilliseconds()             //毫秒
    };
    if(/(y+)/.test(fmt))
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    for(var k in o)
        if(new RegExp("("+ k +")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
    return fmt;
}
var bSubmit = false;
    $('.jsSubmit').on('click', function () {
        var oCmtIpt = document.getElementsByClassName('comment-content')[0];
        var oListDv = document.getElementsByClassName('comments')[0];
        var oCommentsLength = document.getElementsByClassName('comments_length')[0];
        var oNewsId = document.getElementsByName("newsId")[0];
        //console.log(oCmtIpt);
        var sCmt = $.trim(oCmtIpt.value);
        // 评论为空不能提交
        if (!sCmt) {
            return alert('评论不能为空');
        }
        // 上一个提交没结束之前，不再提交新的评论
        if (bSubmit) {
            return;
        }
        bSubmit = true;
        $.ajax({
            url: '/addComment/',
            type: 'post',
            dataType: 'json',
            data: {
                newsId: oNewsId.value,
                content: sCmt
            }
        }).done(function (oResult) {
            if (oResult.code !== 0) {
                return alert(oResult.msg || '提交失败，请重试');
            }
            // 清空输入框
            oCmtIpt.value = '';
            // 渲染新的评论
            var sHtml = [
                '<div class="media">',
                '<a class="media-left" href="http://nowcoder.com/u/210176">',
                '<img src="' + oResult["headUrl"] + '">',
                '</a>',
                '<div class="media-body">',
                '<h4 class="media-heading"><small>' + oResult["userName"] + ': </small> <small class="date">'
                + new Date(oResult["createdDate"]).Format("yyyy-MM-dd hh:mm:ss") + '</small></h4>',
            '<div>' + oResult["content"] + '</div>',
            '</div>',
            '</div>'
            ].join('');

            oListDv.prepend($(sHtml).get(0));
            oCommentsLength.value = Number(oCommentsLength.value) + 1;
        }).fail(function (oResult) {
            alert(oResult.msg || '提交失败，请重试');
        }).always(function () {
            bSubmit = false;
        });
    });

