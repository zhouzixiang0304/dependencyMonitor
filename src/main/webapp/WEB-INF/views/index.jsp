<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script src ="/resources/js/jquery.min.js"></script>
    <script type ='text/javascript'src="/resources/js/jquery-ui.min.js"></script>
    <%--<script type ='text/javascript' src="/resources/js/jquery-ui.css"></script>--%>
    <%--<script type ='text/javascript' src="/resources/js/jquery-ui.css"></script>--%>
    <%--<script type ='text/javascript' src="/resources/js/neo4j.css"></script>--%>
    <link rel="stylesheet" href="/resources/css/jquery-ui.css"/>
    <link rel="stylesheet" href="/resources/css/bootstrap.css">
    <link rel="stylesheet" href="/resources/css/neo4j.css"/>
    <%--<link rel="stylesheet"--%>
          <%--href="http://neo4j-contrib.github.io/developer-resources/language-guides/assets/css/main.css">--%>
    <title>Dependency Monitor</title>

    <style>
        nodetext {
            font-size: 12px;
            font-family: SimSun;
            fill: #54A23A;
            nodetext-shadow: 0 1px 0 #fff, 1px 0 0 #fff, 0 -1px 0 #fff, -1px 0 0 #fff;
        }
    </style>

    <script src="/resources/js/d3.js" charset="utf-8"></script>

</head>

<body>
<!-- 侧边栏 -->
<aside id="detailsArea">
    <div id="collapse">
        <<
    </div>
    <div id="colOpen">
        >>
    </div>
    <div id="seaBox">
        <h3>依赖查询</h3>
        <div id="seaContainer" class="input-group input-group-sm">
            <input type="text" id="search" class="form-control" placeholder="请输入服务名称"/>
            <span class="input-group-btn">
                <button class="btn btn-danger" type="button" onclick="searchNode()">查询</button>
            </span>
        </div>
        <h3>依赖详情</h3>
        <div id="detailMsg">
            <ul id="MsgList">
            </ul>
        </div>
    </div>
</aside>
<div id="graph">
</div>

<script>
    //aside on
    $("#collapse").click(function () {
        $("#detailsArea").animate({width:'20px'});
        $("#colOpen").css("display","block");
        $("#seaBox").css("display","none");
        $(this).css("display","none");
    });
    //aside off
    $("#colOpen").click(function () {
        $("#detailsArea").animate({width:'280px'});
        $("#collapse").css("display","block");
        $("#seaBox").css("display","block");
        $(this).css("display","none");
    });

    var notes = d3.select('#MsgList');

    var optArray = []; //PLACE HOLDER FOR SEARCH NAMES
    var w = window.innerWidth;
    var h = window.innerHeight;
//    var width = 800, height = 800;
    var focus_node = null;
    var highlight_node = null;

    var highlight_color = "#569edc";
    var highlight_trans = 0.1;
    /*d3.layout.force 基于物理模拟的位置连接，force.charge 获取或设置节点电荷数（表示吸引或排斥），
     linkDistance 获取或设置节点间连接线的距离， size获取宽和高*/
    var force = d3.layout.force().size([w, h])
        .charge(-2000).linkDistance(Math.min(w,h)/3);

    var default_link_color = "#cbcbcb";
    var min_zoom = 0.2;
    var max_zoom = 7;
    var svg = d3.select("#graph").append("svg")
        .attr('width',w)
        .attr('height',h)
        .attr("pointer-events", "all");
    var zoom = d3.behavior.zoom().scaleExtent([min_zoom,max_zoom]);
    var g = svg.append("g");

    //绘制箭头
    var marker = svg.append("marker")
        .attr("id", "arrowhead")
        .attr("markerUnits", "userSpaceOnUse")
        .attr("viewBox", "0 -5 10 10")//坐标系的区域
        .attr("refX", 24)//箭头坐标
        .attr("refY", 0)
        .attr("markerWidth", 12)//标识的大小
        .attr("markerHeight", 12)
        .attr("orient", "auto")//绘制方向，可设定为：auto（自动确认方向）和 角度值
        .attr("stroke-width", 2)//箭头宽度
        .append("path")
        .attr("d", "M0,-5L10,0L0,5")//箭头的路径
        .attr('fill', '#6c6c6c');//箭头颜色

    d3.json("/server/graph", function (error, graph) {
        if (error) return;

        var linkedByIndex = {};
        graph.links.forEach(function(d){
            linkedByIndex[d.source + "," + d.target] = true;
        });
        function isConnected(a,b) {
            return linkedByIndex[a.index + ","+ b.index] || linkedByIndex[b.index +","+ a.index]
            || a.index == b.index;
        }
        //COLLECT ALL THE NODE NAMES FOR SEARCH AUTO-COMPLETE
        for (var i = 0; i < graph.nodes.length; i++) {
            optArray.push(graph.nodes[i].serverName);
        }
        optArray = optArray.sort();

        /*force.node 获得或设置布局中的节点阵列组，links获得或设置布局中节点间得连接阵列组，start开启或恢复节点间得位置影响*/
        force.nodes(graph.nodes).links(graph.links).start();

        var link = g.selectAll(".link")
            .data(graph.links).enter()
            .append("line")
            .style("stroke",default_link_color)
            .style("stroke-width", 0.5)
            .style("pointer-events", "none")
            .attr("class", "link")
            .attr("marker-end", "url(#arrowhead)");

        var gNode = g.selectAll(".node")
            .data(graph.nodes).enter()
            .append("g")
            .attr("class", "layer nodes");
        var node = gNode.append("circle")
            .style("fill", "#c02b11")
            .style('stroke', '#ffffff')
            .style('stroke-width','4')
            .attr("r", 15)
            .call(force.drag);

        //节点文字
        var text = g.selectAll(".text").data(graph.nodes).enter().append("text")
            .attr("dx","-1.3em").attr("dy", "2.5em").style('fill', '#a5a5a5').style('font-size', '12px')
            .style('text-shadow', '0 1px 0 #fff, 1px 0 0 #fff, 0 -1px 0 #fff, -1px 0 0 #fff')
            .text(function (d) {
                return d.serverName;
            });

        //set events
        node
            .on("mouseover",function (d) {
                set_highlight(d);
            })
            .on("mousedown",function(d){
            d3.event.stopPropagation(); //解决拖动SVG时不能拖动节点
            focus_node = d;
            set_focus(d);
            if(highlight_node === null) set_highlight(d)
        })
            .on("mouseout",function (d) {
                exit_highlight();
            })
        //double click nodes open sidebar
            .on("dblclick",function (d) {
            $("#detailsArea").animate({width:'280px'});
            $("#collapse").css("display","block");
            $("#seaBox").css("display","block");
            $("#colOpen").css("display","none");

            // Delete the current notes section for new notes
            notes.selectAll('*').remove();
            notes.append('h5').text(d.serverName);
            var listFather = notes.append('ul');
                listFather.append('li')
                    .text(d.connections);
            var listSon = notes.append('ul');
                listSon.append('li')
                    .text(d.serverName);
            notes.transition().style({'opacity':1});

             d3.event.stopPropagation(); //解决拖动SVG时不能拖动节点
             focus_node = d;
             set_focus(d);
             if(highlight_node === null) set_highlight(d)
        });

        d3.select(window).on("mouseup",function () {
            if (focus_node!==null){
                focus_node = null;
                if (highlight_trans<1){
                    node.style("opacity",1);
                    text.style("opacity",1);
                    link.style("opacity",1);
                }
            }
            if(highlight_node === null) exit_highlight();
        }).on("dblclick",function () {
                if (focus_node!==null){
                    focus_node = null;
                    if (highlight_trans<1){
                        node.style("opacity",1);
                        text.style("opacity",1);
                        link.style("opacity",1);
                    }
                }
                if(highlight_node === null) exit_highlight();
            })

        // mouse down on one of circles
        function set_focus(d){
            if (highlight_trans<1){
                node.style("opacity",function (o) {
                    return isConnected(d,o) ? 1:2 * highlight_trans;
                });
                text.style("opacity",function (o) {
                    return isConnected(d,o) ? 1: highlight_trans;
                });
                link.style("opacity",function (o) {
                    return o.source.index == d.index || o.target.index == d.index ?1:highlight_trans;
                });
            }
        }

        function  set_highlight(d) {
            if (focus_node!==null) d = focus_node;
            highlight_node = d;

            if(highlight_color!="white"){
                text.style("font-weight",function (o) {
                    return isConnected(d,o)? "bold":"normal";
                });
                link.style("stroke",function(o){
                    return o.source.index == d.index || o.target.index == d.index ?
                        highlight_color:default_link_color;
                });
            }
        }

        function exit_highlight() {
            highlight_node = null;
            if (focus_node===null){
                if(highlight_color!="white"){
                    text.style("font-weight","normal");
                    link.style("stroke",default_link_color);
                }
            }
        }

        //实现svg zoom缩放
        zoom.on("zoom",function () {
            g.attr("transform","translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")");
        });
        svg.call(zoom).on('dblclick.zoom',null); //取消鼠标双击放大的效果


        //适应屏幕尺寸
        resize();
        d3.select(window).on("resize",resize);
        function resize() {
            var width = window.innerWidth, height = window.innerHeight;
            svg.attr("width",width).attr("height",height);
            force.size([force.size()[0]+(width-w)/zoom.scale(),force.size()[1]+(height-h)/zoom.scale()]).resume();
            w = width;
            h = height;
        }


        // force feed algo ticks
        //实时更新nodes、links、text的坐标变化
        force.on("tick", function (d) {
            text.attr("transform", function (d) {
                return "translate(" + d.x + "," + d.y + ")";
            });
            var dx = function (d) {
                return d.x;
            };
            var dy = function (d) {
                return d.y;
            };

            link.attr("x1", function (d) {
                return d.source.x;
            })
                .attr("y1", function (d) {
                    return d.source.y;
                })
                .attr("x2", function (d) {
                    return d.target.x;
                })
                .attr("y2", function (d) {
                    return d.target.y;
                });
            node.attr("cx", dx)
                .attr("cy", dy);
//            var translate = "translate("+dx+" "+dy+")";
        });
    });

    // ASSIGN optArray TO search box
    $(function () {
        $("#search").autocomplete({
            source: optArray
        });
    });

    function searchNode() {
        // FIND THE NODE
        var selectedVal = document.getElementById('search').value;

        svg.selectAll(".nodes")
            .filter(function (d) {
                return d.serverName != selectedVal;
            })
            .style("opacity", highlight_trans / 2)
            .transition()
            .duration(5000)
            .style("opacity", 1);
        svg.selectAll(".link")
            .style("opacity", highlight_trans / 2)
            .transition()
            .duration(5000)
            .style("opacity", 1);
        // Delete the current notes section for new notes
        notes.selectAll('*').remove();

        var list = notes.append('ul');
        list.append('li')
            .text(d.serverName);
        notes.transition().style({'opacity':1});
    }
</script>
</body>
</html>