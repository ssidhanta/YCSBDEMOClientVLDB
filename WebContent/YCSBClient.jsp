<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>YCSB Executor</title>
<style type="text/css">
    .container {
        width: 800px;
        clear: both;
    }
    .container input {
        width: 100%;
        clear: both;
    }

    </style>
    <script language="Javascript" type="text/javascript"> 
	var counter = 1; 
	var limit = 30; 
	function addInput(divName){ 
		if (counter == limit) { 
			alert("Reached adding limit" + counter + " inputs"); 
		} 
		else { 
			var newdiv = document.createElement('div'); 
			newdiv.innerHTML = "<b>Workload Sequence " + (counter + 1) + " </b>  &nbsp; &nbsp; &nbsp;Workload Type:<select style='width: 20%' name='workloadtype'><option value='a'>Select Workload Type</option><option value='a'>a</option><option value='b'>b</option><option value='c'>c</option><option value='d'>d</option><option value='e'>e</option><option value='f'>f</option></select> &nbsp; &nbsp; &nbsp;duration(seconds):<input style='width: 20%' type='text' name='sequence'>"; 
			document.getElementById(divName).appendChild(newdiv); 
			counter++; 
		} 
	} 
	
</script> 
</head>
<body>
<div class="container">
<form action="YCSBDEMOClientLanding" method="GET" onSubmit="document.getElementById('submit').disabled=true;">
<font color="blue" size="4"><b>Demonstrating OptCon (To appear in IEEE/ACM CCGrid 2016) with YCSB-D
</b><br/><b>This is a single node setup (for cost reasons we can not keep a geo-replicated cluster up and running 24 hrs)  to show how the demo will work.</b>
<br/><b>If you need a demo on an actual geo-replicated cluster, please send an email at ssidha1@lsu.edu, and we will send you a link within 3-5 days.</b>
<br/><b>YCSB-D Input Form:</b></font>
<br />
<font size="3"><b>Please Type in the YCSB Command Parameters: </b></font>
<br />
hosts:<input type="text" name="hosts" align="right">
<br />
threads:<input type="text" name="threads" align="right">
<br>
fieldcount: <input type="text" name="fieldcount" align="right">
<br />
operationcount: <input type="text" name="operationcount" align="right" >
<br />
recordcount: <input type="text" name="recordcount" align="right">
<br />
requestdistribution: <input type="text" name="requestdistribution" align="right">
<br />
Specify the sequence of YCS Workload types to be executed: 
</br>
<div id="dynamicInput"> 
<b>Workload Sequence 1</b>
 &nbsp; &nbsp; &nbsp;Workload Type:<select  style="width: 20%" name="workloadtype"><option value='a'>Select Workload Type</option><option value='a'>a</option><option value='b'>b</option><option value='c'>c</option><option value='d'>d</option><option value='e'>e</option><option value='f'>f</option></select>
 &nbsp; &nbsp; &nbsp;duration(seconds):<input style='width: 20%' type='text' name='sequence'>
</div> 
<input type="button" value="Add the next Workload Type" onClick="addInput('dynamicInput');"> 
<br />
<input type="submit" value="Submit" align="center" id="submit">
</form>
</div>
</body>
</html>