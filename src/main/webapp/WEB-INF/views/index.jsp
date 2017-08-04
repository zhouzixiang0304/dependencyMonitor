<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0" >
    <link rel="stylesheet" href="http://neo4j-contrib.github.io/developer-resources/language-guides/assets/css/main.css">
    <title>Neo4j Servers</title>

    <style>
        nodetext{
        font-size:12px;
        font-family:SimSun;
        fill:#54A23A;
        nodetext-shadow: 0 1px 0 #fff, 1px 0 0 #fff, 0 -1px 0 #fff, -1px 0 0 #fff;
    }
    </style>
    <link rel="stylesheet" href="./css/tooltip.css"/>
</head>

<body>
<script src="/resources/js/jquery.min.js" ></script>
<script type="text/javascript" src="/resources/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="/resources/js/jquery-ui.css"></script>
<script src="/resources/js/d3.js" charset="utf-8"></script>

<div id="graph">
    <defs>
        <marker id="markerArrow" markerWidth="13" markerHeight="13" refx="2" refy="6" orient="auto">
            <path d="M2,2 L2,11 L10,6 L2,2" style="fill: #000000;" />
        </marker>
    </defs>
</div>
<div role="navigation" class="navbar navbar-default navbar-static-top">
    <div class="container">
        <div class="row">
            <div class="col-sm-6 col-md-6">
                <ul class="nav navbar-nav">
                    <li>
                        <form role="search" class="navbar-form">
                            <div class="form-group">
                                <input id ="search" placeholder="search by name"/>
                                <button type="button" onclick="searchNode()">Find</button>
                            </div>
                            <%--<button class="btn btn-default" type="submit" onclick="search(this.form.search.value)">Search</button>--%>
                        </form>
                    </li>
                </ul>
            </div>
            <div class="navbar-header col-sm-6 col-md-6">
                <div class="logo-well">
                    <a href="http://neo4j.com/developer-resources">
                        <img src="http://neo4j-contrib.github.io/developer-resources/language-guides/assets/img/logo-white.svg" alt="Neo4j World's Leading Graph Database" id="logo">
                    </a>
                </div>
                <div class="navbar-brand">
                    <div class="brand">Neo4j Servers</div>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    var optArray = []; //PLACE HOLDER FOR SEARCH NAMES
    var width = 800, height = 800;

    var highlight_trans = 0.1;
    /*d3.layout.force 基于物理模拟的位置连接，force.charge 获取或设置节点电荷数（表示吸引或排斥），
     linkDistance 获取或设置节点间连接线的距离， size获取宽和高*/
    var force = d3.layout.force()
        .charge(-1000).linkDistance(200).size([width, height]);
    /**/
    var svg = d3.select("#graph").append("svg")
        .attr("width", "100%").attr("height", "100%")
        .attr("pointer-events", "all");

    var marker =  svg.append("marker")
        .attr("id", "arrowhead")
        .attr("markerUnits","userSpaceOnUse")
        .attr("viewBox", "0 -5 10 10")//坐标系的区域
        .attr("refX",32)//箭头坐标
        .attr("refY", -1)
        .attr("markerWidth", 12)//标识的大小
        .attr("markerHeight", 12)
        .attr("orient", "auto")//绘制方向，可设定为：auto（自动确认方向）和 角度值
        .attr("stroke-width",2)//箭头宽度
        .append("path")
        .attr("d", "M0,-5L10,0L0,5")//箭头的路径
        .attr('fill','#1b72b4');//箭头颜色

    d3.json("/server/graph", function(error, graph) {
        if (error) return;

        //COLLECT ALL THE NODE NAMES FOR SEARCH AUTO-COMPLETE
        for (var i = 0; i<graph.nodes.length;i++){
            optArray.push(graph.nodes[i].id);
        }
        optArray = optArray.sort();

        /*force.node 获得或设置布局中的节点阵列组，links获得或设置布局中节点间得连接阵列组，start开启或恢复节点间得位置影响*/
        force.nodes(graph.nodes).links(graph.links).start();

        var link = svg.selectAll(".link")
            .data(graph.links).enter()
            .append("line")
            .style("stroke","#0fb5cc")
            .style("stroke-width",0.5)
            .style("pointer-events","none")
            .attr("class", "link")
            .attr("marker-end","url(#arrowhead)");

        var gNode = svg.selectAll(".node")
            .data(graph.nodes).enter()
            .append("g")
            .attr("class","layer nodes");
        var node = gNode.append("circle")
            .style("fill","#e7f8dc")
            .style('stroke','#54A23A')
            .attr("r", 28)
//            .on("click",function (node) {
//                link.style("style-width",function (line) {
//                    if(line.source.userId==node.userId || line.target.userId==node.userId ){
//                        return 4;
//                    }else{
//                        return 0.5;
//                    }
//                });
//            })
            .call(force.drag)
            .on('mouseover', function(d) {
                if (node.mouseoutTimeout) {
                    clearTimeout(node.mouseoutTimeout);
                    node.mouseoutTimeout = null;
                }
                highlightObject(d);
            })
            .on('mouseout', function() {
                if (node.mouseoutTimeout) {
                    clearTimeout(node.mouseoutTimeout);
                    node.mouseoutTimeout = null;
                }
                node.mouseoutTimeout=setTimeout(function() {
                    highlightObject(null);
                }, 300);
            });


        var text = svg.selectAll(".nodetext").data(graph.nodes).enter().append("text")
            .attr("text-anchor","middle").style('fill','#54A23A').style('font-size','12px')
            .style('text-shadow','0 1px 0 #fff, 1px 0 0 #fff, 0 -1px 0 #fff, -1px 0 0 #fff')
            .text(function (d) {
                return d.serverName;
            })

        // html title attribute
        node.append("title")
            .text(function (d) { return d.title; });

        //tooltip
        var highlighted = null;
        var highlightObject = function(obj){
            if (obj) {
                if (obj !== highlighted) {
                    var objIndex= obj.index;
                    var depends=[objIndex];
                    graph.links.forEach(function(lkItem){
                        if(objIndex==lkItem['source']['index']){
                            depends=depends.concat([lkItem.target.index])
                        }else if(objIndex==lkItem['target']['index']){
                            depends=depends.concat([lkItem.source.index])
                        }
                    });
                    node.classed('inactive',function(d){
                        return (depends.indexOf(d.index)==-1)
                    });
                    link.classed('inactive', function(d) {
                        return (obj !== d.source && obj !== d.target);
                    });
                }

                tooltip.html("<div class='title'>"+obj.serverName+"调用信息</div><table class='detail-info'><tr><td class='td-label'>connections:</td><td>"+obj.connections+"</td></tr></table>")
                    .style("left",(d3.event.pageX+20)+"px")
                    .style("top",(d3.event.pageY-20)+"px")
                    .style("opacity",1.0);
                highlighted = obj;
            } else {
                if (highlighted) {
                    node.classed('inactive', false);
                    link.classed('inactive', false);
                }
                tooltip.style("opacity",0.0);
                highlighted = null;
            }
        };

        var tooltip=d3.select("#graph").append("div")
            .attr("class","tooltip")
            .attr("opacity",0.0)
            .on('mouseover',function(){
                if (node.mouseoutTimeout) {
                    clearTimeout(node.mouseoutTimeout);
                    node.mouseoutTimeout = null;
                }
            })
            .on('mouseout',function(){
                if (node.mouseoutTimeout) {
                    clearTimeout(node.mouseoutTimeout);
                    node.mouseoutTimeout = null;
                }
                node.mouseoutTimeout=setTimeout(function() {
                    highlightObject(null);
                }, 300);
            });

        // force feed algo ticks
        force.on("tick", function(d) {
            var dx = function(d){
                return d.x;
            };
            var dy = function(d) {
                return d.y;
            };

            link.attr("x1", function(d) { return d.source.x; })
                .attr("y1", function(d) { return d.source.y; })
                .attr("x2", function(d) { return d.target.x; })
                .attr("y2", function(d) { return d.target.y; })
            node.attr("cx", dx)
                .attr("cy", dy);
//            var translate = "translate("+dx+" "+dy+")";
            text.attr("transform",function(d) { return "translate(" + d.x + "," + (d.y+5) + ")"; });
        });
    });

    // ASSIGN optArray TO search box
    $(function () {
        $("#search").autocomplete({
            source:optArray
        });
    });

    function  searchNode() {
        // FIND THE NODE
        var selectedVal = document.getElementById('search').value;

        svg.selectAll(".nodes")
            .filter(function (d) {return d.serverName != selectedVal;})
            .style("opacity",highlight_trans/2)
            .transition()
            .duration(5000)
            .style("opacity",1);
        svg.selectAll(".link")
            .style("opacity",highlight_trans/2)
            .transition()
            .duration(5000)
            .style("opacity",1);
    }
</script>

</body>
</html>
