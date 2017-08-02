/**
 * Created by doll on 2/25.
 */
    //数据缓存
var temp = {
    tempUpdateNode:{
        id:"",
        text:"",
        state:"",
        folder:""
    },
    isShare: false,
};
var descriptionDivText;

Date.prototype.format = function(format){
    var o =  {
        "M+" : this.getMonth()+1, //month
        "d+" : this.getDate(), //day
        "h+" : this.getHours(), //hour
        "m+" : this.getMinutes(), //minute
        "s+" : this.getSeconds(), //second
        "q+" : Math.floor((this.getMonth()+3)/3), //quarter
        "S" : this.getMilliseconds() //millisecond
    };
    if(/(y+)/.test(format)){
        format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    }
    for(var k in o)  {
        if(new RegExp("("+ k +")").test(format)){
            format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length));
        }
    }
    return format;
};



function myMessager (data) {
    if (!data || data.length == 0) {
        $.messager.show({"title":"信息!!", "msg":"失败!!"});
        return;
    }
    if(typeof data == "string") {
        $.messager.show({
            title: data.title ? data.title : "信息!!",
            msg: data
        });
        return;
    }
    if (typeof(data.message) != 'undefined') {
        $.messager.show({
            title: data.title ? data.title : "信息!!",
            msg:data.message
        });
        return;
    }
    if (typeof(data.msg) != 'undefined') {
        console.log(data);
        $.messager.show({
            title: data.title ? data.title : "信息!!",
            msg:data.msg
        });
        return;
    }
    $.messager.show({
        title: data.title ? data.title : "信息!!",
        msg:data.msg.message ? data.msg.message : data
    });
}

function closeShare(tree) {
    tree.tree({"checkbox":false});
    temp.isShare = false;
}

function openShare(tree, treeUrl) {
    tree.tree('options').url  = treeUrl;
    tree.tree({"checkbox":true});
    temp.isShare = true;
}

/**
 * 文件详情
 **/

function fileDetails(target , id) {
    console.log("fileDetails");
    $.get("file/details", {"id":id}, function (result) {
        console.log("details")
        console.log(result);
        if (result) {
            if(result.object == null) {
                $(target).html("<p>无</p>");
                return;
            }
            $(target).html(result.object.fileDesc);
            return;
        }
        $(target).html("<p>无</p>");
    });
}


/**
 * 文件下载
 */
function downloadFile(tree){
    var node = tree.tree('getSelected');
    if(node.folder == false) {
        window.open("file/download?id=" + node.id);
    }else {
        myMessager("请选择文件!!");
    }

}

function updateShare(tree) {
    if (!temp.isShare) {
        return;
    }
    /*
     获取选中
     */
    var checked = tree.tree("getChecked", "checked");
    var checkedIds;
    for (var i = 0; i < checked.length; i++) {
        checkedIds = checkedIds + "," + checked[i].id;
    }
    console.log(checkedIds);
    /*
     获取未选中
     */
    var unchecked = tree.tree("getChecked", "unchecked");
    var uncheckedIds;
    for (var i = 0; i < unchecked.length; i++) {
        uncheckedIds = uncheckedIds + "," + unchecked[i].id;
    }
    /*
     获取不定节点
     */
    var indeterminate = tree.tree("getChecked", "indeterminate");
    var indeterminateIds;
    for (var i = 0; i < indeterminate.length; i++) {
        indeterminateIds = indeterminateIds + "," + indeterminate[i].id;
    }
    $.post("file/share/update",{"checkedIds":checkedIds,"uncheckedIds": uncheckedIds, "indeterminateIds":indeterminateIds}, function (result) {
        myMessager({"title":"更新信息!!", "msg":result});
        closeShare(tree);
    });
}


/**
 * 获取所有字节点
 * @param data
 * @param parentId
 * @returns {Array}
 */
function getNodesByParentId(data, parentId) {
    var list = [];
    var index = 0;
    for (var i =0; i< data.length; i++) {
        if (parentId == data[i].parentId) {
            list[index++] = data[i];
        }
    }
    return list;
}