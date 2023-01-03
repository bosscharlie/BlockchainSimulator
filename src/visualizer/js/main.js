var time = 0;
var pause_sig=false;

function showClock() {    
    if (blockchain_data == undefined || event_data == undefined) {
        return;
    }
    remove_traffic();
    time = time + Number(document.getElementById("speed").value);
    while(true){
        if(blockchain_data.length <= block_index || time < Number(blockchain_data[block_index].timestamp)){
            break;
        }
        next_block();   
    }  
    while(true){
        if(event_data.length <= event_id || time < Number(event_data[event_id].time)){
            break;
        }
        block_event();    
    }
    document.getElementById("timestamp_area").innerHTML = "Simulation Time: " + time;
}

function sleep(msec) {
    return new Promise(function(resolve) {
        setTimeout(function() {resolve()}, msec);
    })
}

//开始进行仿真
async function start() {
    await sleep(500);

    //停止条件
    if(pause_sig || (blockchain_data.length <= block_index && event_data.length <= event_id)){
        return
    }                
    start();
    showClock();
}

//暂停仿真
function pause(){
    pause_sig=true;
}

//继续仿真
function begin(){
    pause_sig=false;
    start();
}

async function start_traffic() {
    await sleep(100);
    play_traffic();
    start_traffic()
}

function main(){
    add_node();
    init_network();
    add_edge(); 
    init_traffic_list();
    start();
    start_traffic();
}

input_data();

function start_simulation(){
    time = Number(document.getElementById("start_point").value);
    main();
}