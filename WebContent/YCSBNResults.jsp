<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Results</title>
<style type="text/css">
    .container {
        width: 500px;
        clear: both;
    }
    .container input {
        width: 100%;
        clear: both;
    }

    </style>
</head>
</head>
<body>
<div class="container">
<font color="red" size="2"><b>YCSB Results</b></font>
<font size="2"><b>Executing the following command:=<%=request.getAttribute("command") %></b></font>
</br>
<font color="blue" size="2"><b>Please wait for the Result Visualizer Window to pop up and display the runtime variations in Latency, Throughput, and Staleness over Time, with consistency of Cassandra tuned using OptCon. Here, we are running a single node setup for cost reasons, hence the staleness is zero. However, during the actual demo we will be executing YCSB-D on a geo-replicated setup.</font>
</div>
</body>
</html>