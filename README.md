In this repo, mysql is used to store data on developer profiles. An end point /list is provided with a queryparam type which can accept values such as pdf,xml and json.

Jersey is the jax-rs implementation used to develop the rest end point.

Jetty is used as the HTTP server

some instructions:

make sure you have the settings.xml set for the maven central repo.
also do a maven update if you change the central repo.

you can copy paste the sql script in a file and then hit 
mysql -u root -p and then hit enter

executing the script can be done by the below command
source file_name
