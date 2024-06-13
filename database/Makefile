database-up:
	docker build -t socialmedia-postgres-img .
	docker run -d -p 5432:5432 --name socialmedia-postgres-db socialmedia-postgres-img 
delete-database-and-image:
	docker kill socialmedia-postgres-db
	docker rm socialmedia-postgres-db
	docker rmi socialmedia-postgres-img
