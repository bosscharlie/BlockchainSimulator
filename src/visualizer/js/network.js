nodes = new vis.DataSet([]);
edges = new vis.DataSet([]);
var traffic_count = true;
var traffic_edge = [];
var traffic_arrow = [];
var block_id = [];

var edge_id = 0;
var event_id = 0;

function add_node(){
    var nc = 1;
    for (var i=0; i<matrix_data[0].length; i++){
        if(document.getElementById("node").node.value == "1"){
            nodes.add([{id:i, label:"node"+i, group: 1}]);
            continue;
        }
        nodes.add([{id:i, label:"node"+i, group: nc}]);
        nc++;
    }
}

function add_edge(){
    for(var i=0;i<matrix_data[0].length;i++){
        for(var j=i;j<matrix_data[0].length;j++){
            if(matrix_data[i][j]==1){
                network.body.data.edges.update([{id:i*matrix_data.length-1+j,from:i, to:j,color :"#848484"}]);
                edge_id++;
            }
        }
    }
}


var hash_lists = [];

//对节点进行标号染色
var color_lists = [];

//记录节点高度
var node_index = [];

//FoundBlock事件
function update_node_found(node_id,index,hash){
    if(node_index.length==0){
        for(i=0;i<matrix_data[0].length;i++){
            node_index.push(0);
        }
    }
    
    //如果块的高度是最大的，则添加，进行染色
    if(node_index[node_id]<index){
        node_index[node_id] = index;
        hash_lists.push(hash)
        color_lists.push(color_lists.length+2);
        if(document.getElementById("node").node.value == "1"){
            nodes.update({id:node_id, group: color_lists.length+1});
        }
    } 
}
 
//接收区块并进行更新
function update_node_receive(node_id,index,hash){
    _color = 0;
    for(i=0;i<hash_lists.length;i++){
        if(hash == hash_lists[i]){
            _color = color_lists[i];
            break;
        }
    }
    if(node_index[node_id]<index){
        node_index[node_id] = index;
        if(document.getElementById("node").node.value == "1"){
            nodes.update({id:node_id, group: _color});
        }
    } 
}

//标记网络中区块的传播方向
function play_traffic(){
    if (traffic_count){
        traffic_count = false;
        for (var i=0; i<traffic_edge.length;i++){
            for(var j=i;j<traffic_edge.length;j++){
                if(traffic_edge[i][j]==1){
                    if(traffic_arrow[i][j]==0){
                        network.body.data.edges.update([{id:i*matrix_data.length-1+j,from:i, to:j,color :"red",width: 3, arrows: "to"}]);
                    }else{
                        network.body.data.edges.update([{id:i*matrix_data.length-1+j,from:i, to:j,color :"red",width: 3, arrows: "from"}]);
                    }
                }
            }
        }
    }else{
        traffic_count = true;
        for (var i=0; i<traffic_edge.length;i++){
            for(var j=i;j<traffic_edge.length;j++){
                if(traffic_edge[i][j]==1){      
                    network.body.data.edges.update([{id:i*matrix_data.length-1+j,from:i, to:j,color :"#848484"}]);
                }
            }
        }
    }
}

function init_traffic_list(){
    for(var i=0;i<matrix_data.length;i++){
        var _list = [];
        var _arrow = [];
        for(var j=0;j<matrix_data.length;j++){
            _list.push(0);
            _arrow.push(0);
        }
        traffic_edge.push(_list);
        traffic_arrow.push(_arrow);
    }
}

function add_traffic_list(from, to){
    if(from <= to){
        traffic_edge[from-1][to-1] = 1;
        //0 → to (arrow)
        traffic_arrow[from-1][to-1] = 0;
    }else{
        traffic_edge[to-1][from-1] = 1;
        //1 → from(arrow)
        traffic_arrow[to-1][from-1] = 1; 
    }
}

//删除所有的有向边标记
function remove_traffic(){
    var len = edges.length;
    for (var i=0; i<traffic_edge.length;i++){
        for(var j=i;j<traffic_edge.length;j++){
            if(traffic_edge[i][j]==1){
                traffic_edge[i][j] = 0;
                network.body.data.edges.update([{id:i*matrix_data.length-1+j,color :"#848484",width: 2,arrows:{to:{ enabled: false}}}]);
                network.body.data.edges.update([{id:i*matrix_data.length-1+j,color :"#848484",width: 2,arrows:{from:{ enabled: false}}}]);
            }
        }
    }
}

//初始化网络图
function init_network(){
    var container = document.getElementById("network_panel");
    var data = {
        nodes: nodes,
        edges: edges,
   };
   
   var options = {
       nodes: {
           shape: "dot",
           size: 15,
       },
       edges:{
           smooth: false,
       },
       physics: {
           forceAtlas2Based: {
               gravitationalConstant: -26,
               centralGravity: 0.005,
               springLength: 230,
               springConstant: 0.18,
           },
           maxVelocity: 146,
           solver: "forceAtlas2Based",
           timestep: 0.35,
       },
   };
   network = new vis.Network(container, data, options);
}


//右侧区块链图像的更新
function block_event(){
    if(event_data[event_id].type == "InitNode"){
        console.log(event_data[event_id].type);
    }else if(event_data[event_id].type == "FoundBlock"){ 
        update_node_found(Number(event_data[event_id].node),Number(event_data[event_id].height),event_data[event_id].hash);
        // console.log(event_data[event_id].type+" "+event_data[event_id].miner);
        // console.log(color_lists)
        // console.log(hash_lists)
        // console.log(node_index)
    }else if(event_data[event_id].type == "ReceiveBlock"){
        node_to = Number(event_data[event_id].node);
        node_from = Number(event_data[event_id].from);
        add_traffic_list(node_from+1,node_to+1);
        update_node_receive(Number(event_data[event_id].node),Number(event_data[event_id].height),event_data[event_id].hash);
        // console.log(event_data[event_id].type);
    }
    event_id++;
}