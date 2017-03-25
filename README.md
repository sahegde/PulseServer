Pulse Astronaut health monitoring system.

Sample html for login
sample html which can hit a login API

<!DOCTYPE html>
<html>
	<body>
		<form action="http://127.0.0.1:8080/pulse/login" method="POST">
			Login :   <input type="text" id="username" name="username" />
			Password: <input type="text" id="password" name="password" />
			<input type="submit" value="Submit" />
		</form>
	</body>
</html>