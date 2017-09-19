<@extends name="header.ftl"></@extends>

    <div id="main">


        <div class="container" id="daily">
            <div class="jscroll-inner">
                <div class="daily">
                    <#assign cur_date = "">
                    <#list views as vo>
                    <#if cur_date != vo.news.createdDate?string("yyyy-MM-dd")>
                        <#if (vo_index > 0) >
                        </div>
                        </#if>
                        <#assign cur_date=vo.news.createdDate?string("yyyy-MM-dd")>
                        <h3 class="date">
                            <i class="fa icon-calendar"></i>
                            <span>头条资讯 &nbsp; ${vo.news.createdDate?string("yyyy-MM-dd")}</span>
                        </h3>

                    <div class="posts">
                    </#if>
                        <div class="post">
                            <div class="votebar">
                                <#if (vo.like > 0)>
                                    <button class="click-like up pressed" data-id="${vo.news.id}" title="赞同"><i class="vote-arrow"></i><span class="count">${vo.news.likeCount}</span></button>
                                <#else>
                                    <button class="click-like up" data-id="${vo.news.id}" title="赞同"><i class="vote-arrow"></i><span class="count">${vo.news.likeCount}</span></button>
                                </#if>
                                <#if (vo.like < 0)>
                                    <button class="click-dislike down pressed" data-id="${vo.news.id}" title="反对"><i class="vote-arrow"></i></button>
                                <#else>
                                    <button class="click-dislike down" data-id="${vo.news.id}" title="反对"><i class="vote-arrow"></i></button>
                                </#if>
                            </div>
                            <div class="content" data-url="http://nowcoder.com/posts/5l3hjr">
                                <div >
                                    <a href="/news/${vo.news.id}">
                                        <img class="content-img" src="${vo.news.image}" alt="">
                                    </a>
                                </div>
                                <div class="content-main">
                                    <h3 class="title">
                                        <a target="_blank" rel="external nofollow" href="${vo.news.link}">${vo.news.title}</a>
                                    </h3>
                                    <div class="meta">
                                        ${vo.news.link}
                                        <span>
                                            <i class="fa icon-comment"></i> ${vo.news.commentCount}
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="user-info">
                                <div class="user-avatar">
                                    <a href="/user/${vo.user.id}/"><img width="32" class="img-circle" src="${vo.user.headUrl}"></a>
                                </div>

                            </div>

                            <div class="subject-name">来自 <a href="/user/${vo.user.id}/">${vo.user.name}</a></div>
                        </div>

                    <#if !vo_has_next>
                        </div>
                    </#if>

                    </#list>


                </div>
            </div>
        </div>

    </div>
<script type="text/javascript" src="/scripts/main/site/home.js"></script>
<@extends name="footer.ftl"></@extends>