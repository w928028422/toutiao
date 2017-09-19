<@extends name="header.ftl"></@extends>
    <div id="main">
        <div class="container">
            <div class="post detail">

                <div class="votebar">
                <#if (like > 0)>
                    <button class="click-like up pressed" data-id="${news.id}" title="赞同"><i class="vote-arrow"></i><span class="count">${news.likeCount}</span></button>
                <#else>
                    <button class="click-like up" data-id="${news.id}" title="赞同"><i class="vote-arrow"></i><span class="count">${news.likeCount}</span></button>
                </#if>
                <#if (like < 0)>
                    <button class="click-dislike down pressed" data-id="${news.id}" title="反对"><i class="vote-arrow"></i></button>
                <#else>
                    <button class="click-dislike down" data-id="${news.id}" title="反对"><i class="vote-arrow"></i></button>
                </#if>
                </div>

                <div class="content" data-url="http://nowcoder.com/posts/5l3hjr">
                      <div class="content-img">
                          <img src="${news.image}?imageView2/0/w/100/h/80/" alt="">
                      </div>
                      <div class="content-main">
                          <h3 class="title">
                              <a target="_blank" rel="external nofollow" href="${news.link}">${news.title}</a>
                          </h3>
                          <div class="meta">
                              ${news.link}
                              <span >
                                  <i class="fa icon-comment comments_length">${news.commentCount}</i>
                              </span>
                          </div>
                      </div>
                  </div>
            <div class="user-info">
                <div class="user-avatar">
                    <a href="http://nowcoder.com/u/125701"><img width="32" class="img-circle" src="${owner.headUrl}"></a>
                </div>

                </div>

                <div class="subject-name">来自 <a href="/user/${owner.id}">${owner.name}</a></div>
            </div>

            <div class="post-comment-form">
                <#if user??>
                <span>评论 (${news.commentCount})</span>
                  <div class="form-group text required comment_content">
                      <label class="text required sr-only">
                          <abbr title="required">*</abbr> 评论
                      </label>
                      <input type="hidden" name="newsId" value="${news.id}"/>
                      <textarea rows="5" class="text required comment-content form-control" name="content" id="content"></textarea>
                  </div>
                  <div class="text-right">
                    <input type="submit" name="commit" value="提 交" class="btn btn-default btn-info jsSubmit">
                  </div>
                    <script type="text/javascript" src="/scripts/main/component/comment.js"></script>
                <#else>
                <div class="login-actions">
                    <a class="btn btn-success" id="preURL" href="/?pop=1&url=">登录后评论</a>
                </div>
                    <script>
                        var pre = document.getElementById("preURL");
                        pre.setAttribute("href", pre.getAttribute("href") + window.location.href);
                    </script>
                </#if>
            </div>

            <div id="comments" class="comments">
                <#list comments as commentvo>
                <div class="media">
                    <a class="media-left" href="http://nowcoder.com/u/210176">
                        <img src="${commentvo.user.headUrl}">
                    </a>
                    <div class="media-body">
                        <h4 class="media-heading"><small>${commentvo.user.name}: </small> <small class="date">${commentvo.comment.createdDate?string('yyyy-MM-dd HH:mm:ss')}</small></h4>
                        <div>${commentvo.comment.content}</div>
                    </div>
                </div>
                </#list>
            </div>

        </div>
        <script type="text/javascript">
          $(function(){

            // If really is weixin
            $(document).on('WeixinJSBridgeReady', function() {

              $('.weixin-qrcode-dropdown').show();

              var options = {
                "img_url": "",
                "link": "http://nowcoder.com/j/wt2rwy",
                "desc": "",
                "title": "读《Web 全栈工程师的自我修养》"
              };

              WeixinJSBridge.on('menu:share:appmessage', function (argv){
                WeixinJSBridge.invoke('sendAppMessage', options, function (res) {
                  // _report('send_msg', res.err_msg)
                });
              });

              WeixinJSBridge.on('menu:share:timeline', function (argv) {
                WeixinJSBridge.invoke('shareTimeline', options, function (res) {
                  // _report('send_msg', res.err_msg)
                });
              });

            });

          })
        </script>
    </div>

<script type="text/javascript" src="/scripts/main/site/detail.js"></script>
<@extends name="footer.ftl"></@extends>