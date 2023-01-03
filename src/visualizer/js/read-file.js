const matrix_filename = "./outputFile/adjacencyMatrix.csv";
const blockchain_filename = "./outputFile/block.json";
const event_filename = "./outputFile/event.json";

var blockchain_data;
var event_data;
var matrix_data;

//读取simulator产生的仿真结果
function input_data() {
    $(function() {
        $.getJSON(event_filename, function(data) {
            //事件列表
            event_data = data;
        })
    });
    $(function() {
        $.getJSON(blockchain_filename, function(data) {
            //区块列表
            blockchain_data = data;
        })
    });
    getCSVFile();
}

function getCSVFile() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function() {
        createArray(xhr.responseText);
    };
    xhr.open("get", matrix_filename , true);
    xhr.send(null);
}

function createArray(csvData) {
    var tempArray = csvData.split("\n");
    var csvArray = new Array();
    for(var i = 0; i<tempArray.length;i++){
        csvArray[i] = tempArray[i].split(",");
    }
    //网络拓扑
    matrix_data = csvArray;
}
