<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Proyecto ASR Pedro</title>
	</head>
	<body>
		<h1>Ejemplo de Proyecto de ASR con Cloudant</h1>
		<hr />
		<p>Opciones de Cloudant para IBM Cloud:</p>
		<ul>
			<li><a href="show">Listar</a></li>
			<li><a href="add?q=" onclick="return promptUser();" id="insert">Insertar</a></li>
		</ul>
		<script>
			function promptUser() {
				var name = prompt("Introduzca un nombre");
				var link = document.getElementById("insert");
				if(name) {
					link.href += name;
					return true;
				} else {
					alert("No ha introducido un nombre");
					return false;
				}
			}
		</script>
	</body>
</html>