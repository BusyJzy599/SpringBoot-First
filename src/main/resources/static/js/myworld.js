/**
 * 封装一下功能方法
 * @param targetId
 * @param type
 * @param content
 */

function comment2targetId(targetId,type,content) {

    $.ajax({
        type: "POST",
        url: "/comment",
        contentType:"application/json",
        data:JSON.stringify( {
            "parentId":targetId,
            "content":content,
            "type":type
        }),
        success: function (response) {
            if(response.code==200){
                //$("#comment_section").hide();
                location.reload();
            }else {
                if(response.code==2003){
                    //如果未登录
                    var isAccept = confirm(response.message);
                    if(isAccept){
                        window.open("https://github.com/login/oauth/authorize?client_id=6cadd4ad60f1467acede&redirect_uri=http://localhost:8887/callback&scope=user&state=1");
                        //自定义localStorag对象
                        window.localStorage.setItem("closable",true);
                    }

                }else {
                    alert(response.message);
                }
            }
            console.log(response);
        },
        dataType: "json"
    });
}


/**
 * 提交回复
 */
function postComment(){
    var questionId=$("#question_id").val();
    var content=$("#comment_content").val();
    comment2targetId(questionId,1,content);

}

/**
 *
 * @param commentId
 */

function comment(e) {
    var commentId=e.getAttribute("data-id");
    var content=$("#input-"+commentId).val();
    comment2targetId(commentId,2,content)

}

/**
 *展开二级评论
 */
function collapseComments(e) {
    var id=e.getAttribute("data-id");
    var comments=$("#comment-"+id);

    //获取一下二级评论的展开状态
    var collapse=e.getAttribute("data-collapse");
    if(collapse){
        //折叠
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    }else{
        //获取二级评论请求
        var subCommentContainer=$("#comment-"+id);
       if(subCommentContainer.children().length!=1){
            //展开二级评论
            comments.addClass("in");
            //标记二级评论状态
            e.setAttribute("data-collapse","in");
            e.classList.add("active");
        }else{
            $.post("/comment/"+id, function(data) {
                $.each(data.data.reverse(),function (index,comment) {
                    console.log(comment);
                    //拼接HTML代码
                    var mediaLeftElement=$( "<div/>", {
                        "class": "media-left",
                    }).append($("<img/>",{
                        "class":"media-object img-circle",
                        "style":" width: 45px; height: 45px",
                        "src":comment.user.avatarUrl
                    }));

                    var mediaBodyElement=$("<div/>",{
                        "class":"media-body"
                    }).append($("<h5/>",{
                        "class":"media-heading",
                        "html":comment.user.name
                    })).append($("<div/>",{
                        "html":comment.content
                    })).append($("<div/>",{
                        "class":"menu"
                    })).append($("<span/>",{
                        "class":"btn-publish",
                        "html":moment(comment.gmtCreate).format('YYYY-MM-DD')
                    }));

                    var mediaElement=$( "<div/>", {
                        "class": "media",
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    var commentElement =$( "<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments",
                    }).append(mediaElement);

                    commentElement.append(mediaElement);
                    subCommentContainer.prepend(commentElement);
                });

                //展开二级评论
                comments.addClass("in");
                //标记二级评论状态
                e.setAttribute("data-collapse","in");
                e.classList.add("active");
            });

        }

    }


}

/**
 * 点击标签方法
 */
function selectTag(e) {
    var value=e.getAttribute("data-tag");
    var previous=$("#tag").val();
    if(previous.indexOf(value)==-1){
        if (previous){
            //添加
            $("#tag").val(previous+','+value);
        }else {
            $("#tag").val(value);
        }
    }

}

/**
 * 展示标签
 */
function showSelectTag() {
   $("#select-tag").show();
}
