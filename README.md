<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

</head>
<body>

<h1>TrekLink Microservices Project</h1>

<h2>Overview</h2>
<p>TrekLink is a microservices-based project developed using Spring Boot. The project consists of several services that communicate with each other, providing a robust and scalable architecture. The main services include:</p>
<ol>
    <li>User Service</li>
    <li>Post Service</li>
    <li>Search Service</li>
    <li>Config Service</li>
    <li>API Gateway</li>
    <li>Discovery Service (Eureka)</li>
</ol>

<h2>Architecture</h2>
<p>The TrekLink project is designed with the following architecture:</p>
<ul>
    <li><strong>User Service</strong>: Manages user information and authentication.</li>
    <li><strong>Post Service</strong>: Handles the creation, updating, and deletion of posts.</li>
    <li><strong>Search Service</strong>: Provides search functionality for searching users.</li>
    <li><strong>Config Service</strong>: Centralized configuration management for all services.</li>
    <li><strong>API Gateway</strong>: Routes requests to the appropriate services and provides a single entry point.</li>
    <li><strong>Discovery Service (Eureka)</strong>: Service registry for dynamic service discovery.</li>
</ul>

<h2>Services</h2>

<h3>User Service</h3>
<ul>
    <li><strong>Port</strong>: 8300</li>
    <li><strong>Description</strong>: Manages user information and authentication.</li>
    <li><strong>Endpoints</strong>:
        <ul>
            <li>GET /users: Retrieve a list of users.</li>
            <li>GET /users/{id}: Retrieve a specific user by ID.</li>
            <li>POST /users: Create a new user.</li>
            <li>PUT /users/{id}: Update an existing user.</li>
        </ul>
    </li>
</ul>

<h3>Post Service</h3>
<ul>
    <li><strong>Port</strong>: 8200</li>
    <li><strong>Description</strong>: Handles the creation, updating, and deletion of posts.</li>
    <li><strong>Endpoints</strong>:
        <ul>
            <li>GET /users/{userId}/posts: Retrieve a list of posts.</li>
            <li>GET /users/{userId}/posts/{id}: Retrieve a specific post by ID.</li>
            <li>POST /users/{userId}/posts: Create a new post.</li>
            <li>PUT /users/{userId}/posts/{id}: Update an existing post.</li>
            <li>DELETE /users/{userId}/posts/{id}: Delete a post.</li>
        </ul>
    </li>
</ul>

<h3>Search Service</h3>
<ul>
    <li><strong>Port</strong>: 8400</li>
    <li><strong>Description</strong>: Provides search functionality for searching users.</li>
    <li><strong>Endpoints</strong>:
        <ul>
            <li>GET /search/{username}: Search for users based on a username.</li>
        </ul>
    </li>
</ul>

<h3>Config Service</h3>
<ul>
    <li><strong>Port</strong>: 8888</li>
    <li><strong>Description</strong>: Centralized configuration management for all services.</li>
    <li><strong>Endpoints</strong>: Serves configurations for each microservice.</li>
</ul>

<h3>API Gateway</h3>
<ul>
    <li><strong>Port</strong>: 8765</li>
    <li><strong>Description</strong>: Routes requests to the appropriate services and provides a single entry point.</li>
    <li><strong>Endpoints</strong>: Acts as a proxy for all service endpoints.</li>
</ul>

<h3>Discovery Service (Eureka)</h3>
<ul>
    <li><strong>Port</strong>: 8761</li>
    <li><strong>Description</strong>: Service registry for dynamic service discovery.</li>
    <li><strong>Endpoints</strong>: Provides a registry for all microservices to register themselves and discover others.</li>
</ul>

<h2>Getting Started</h2>

<h3>Prerequisites</h3>
<ul>
    <li>Java 21</li>
    <li>Maven 3.8+</li>
    <li>MySQL database</li>
    <li>Docker</li>
    <li>Docker Compose</li>
</ul>

<h3>Running the Services</h3>
<p>You can run the services using Docker Compose for easy orchestration:</p>
<pre><code>docker-compose up</code></pre>
<p>This will start all the services as defined in the <code>docker-compose.yml</code> file.</p>

<h3>Docker Compose Configuration</h3>
<p>The following is an example of a <code>docker-compose.yml</code> configuration for the TrekLink project:</p>
<pre><code>
version: '3.8'
services:
  config-service:
    image: treklink/config-service
    ports:
      - "8888:8888"
  discovery-service:
    image: treklink/discovery-service
    ports:
      - "8761:8761"
  api-gateway:
    image: treklink/api-gateway
    ports:
      - "8765:8765"
  user-service:
    image: treklink/user-service
    ports:
      - "8300:8300"
    depends_on:
      - config-service
      - discovery-service
  post-service:
    image: treklink/post-service
    ports:
      - "8200:8200"
    depends_on:
      - config-service
      - discovery-service
  search-service:
    image: treklink/search-service
    ports:
      - "8400:8400"
    depends_on:
      - config-service
      - discovery-service
  mysql:
    image: mysql:latest
    environment:
      MYSQL_ROOT_PASSWORD: root_password
      MYSQL_DATABASE: treklink
    ports:
      - "3306:3306"
</code></pre>

<h3>Configuration</h3>
<p>Configurations for all services are managed centrally by the config service. Ensure the <code>application.properties</code> or <code>bootstrap.properties</code> in each service points to the config server:</p>
<pre><code>spring.config.import=optional:configserver:http://config-service:8888</code></pre>

<h3>Testing</h3>
<p>You can test the services using tools like Postman or Curl by sending requests to the API Gateway, which will route them to the appropriate service.<br>We also added the Swagger tool for API testing for each service:</p>
<ul>
<li><strong>User:</strong> <a href="http://localhost:8300/swagger-ui/index.html">http://localhost:8300/swagger-ui/index.html</a></li>
<li><strong>Post:</strong> <a href="http://localhost:8200/swagger-ui/index.html">http://localhost:8200/swagger-ui/index.html</a></li>
<li><strong>Search:</strong> <a href="http://localhost:8400/swagger-ui/index.html">http://localhost:8400/swagger-ui/index.html</a></li>
</ul>

<h2>Monitoring and Logging</h2>
<p>All services are configured to log information to console and files. You can customize logging settings in the <code>application.properties</code> file of each service.</p>

<h2>Security</h2>
<p>Each service can be secured using Spring Security. JWT tokens are used for authentication and authorization across services.</p>

</body>
</html>
