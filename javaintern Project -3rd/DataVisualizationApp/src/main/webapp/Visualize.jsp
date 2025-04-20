<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Visualization</title>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <style>
        body {
            font-family: Arial;
            background: skyblue;
        }
        .chart-container {
            background: white;
            border-radius: 10px;
            margin: 20px auto;
            padding: 20px;
            width: 80%;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        h2 {
            text-align: center;
        }
    </style>
</head>
<body>

    <div class="chart-container">
        <h2>Bar Chart</h2>
        <div id="bar_chart" style="height: 400px;"></div>
    </div>
    <div class="chart-container">
        <h2>Line Chart</h2>
        <div id="line_chart" style="height: 400px;"></div>
    </div>
    <div class="chart-container">
        <h2>Pie Chart</h2>
        <div id="pie_chart" style="height: 400px;"></div>
    </div>

   <script type="text/javascript">
    google.charts.load('current', {packages: ['corechart']});
    google.charts.setOnLoadCallback(drawCharts);

    function drawCharts() {
        var rawData = <%= request.getAttribute("chartData") %>;
        var data = google.visualization.arrayToDataTable(rawData);

        // Common options with type-safe configuration
        var commonOptions = {
            backgroundColor: '#ffffff',
            legend: { position: 'right' },
            hAxis: { 
                title: data.getColumnLabel(0), 
                textStyle: {color: '#333'},
                type: 'string' // Force first axis to be categorical
            },
            vAxis: { 
                title: 'Values', 
                textStyle: {color: '#333'},
                type: 'number' // Force vertical axis to be numeric
            }
        };

        // Bar Chart
        var barOptions = Object.assign({}, commonOptions, {
            title: 'Bar Chart Visualization',
            colors: ['#1b9e77', '#d95f02', '#7570b3'],
            isStacked: false
        });

        // Line Chart
        var lineOptions = Object.assign({}, commonOptions, {
            title: 'Line Chart Trends',
            colors: ['#6b5b95', '#d95f02', '#1b9e77'],
            curveType: 'function'
        });

        // Pie Chart (uses first data column)
        var pieOptions = {
            title: 'Primary Category Distribution',
            backgroundColor: '#ffffff',
            is3D: true,
            pieHole: 0.4,
            colors: ['#ff6f61', '#6b5b95', '#88b04b']
        };

        new google.visualization.ColumnChart(document.getElementById('bar_chart')).draw(data, barOptions);
        new google.visualization.LineChart(document.getElementById('line_chart')).draw(data, lineOptions);
        new google.visualization.PieChart(document.getElementById('pie_chart')).draw(data, pieOptions);
    }
</script>
    
</body>
</html>