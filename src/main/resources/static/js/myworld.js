/*回复时触发*/
function postComment(){
    var questionId=$("#question_id").val();
    var content=$("#comment_content").val();

    $.ajax({
        type: "POST",
        url: "/comment",
        contentType:"application/json",
        data:JSON.stringify( {
            "parentId":questionId,
            "content":content,
            "type":1
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