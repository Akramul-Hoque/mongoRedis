# 🐳 Docker Deployment Guide

Complete guide to deploy the MongoDB-Redis Spring Boot application using Docker.

## 📋 Prerequisites

- Docker Desktop installed
- Docker Compose installed
- At least 2GB of free RAM

## 🚀 Quick Start

### 1. Build and Start All Services

```bash
docker-compose up --build
```

This will start:
- **MongoDB** on port `27017`
- **Redis** on port `6379`
- **Spring Boot App** on port `8080`

### 2. Access the Application

- **API Base URL**: http://localhost:8080/school
- **Swagger UI**: http://localhost:8080/school/swagger-ui.html
- **API Docs**: http://localhost:8080/school/v3/api-docs
- **Health Check**: http://localhost:8080/school/actuator/health

## 🛠️ Docker Commands

### Start Services (Detached Mode)

```bash
docker-compose up -d
```

### Stop Services

```bash
docker-compose down
```

### Stop and Remove Volumes (⚠️ Deletes all data)

```bash
docker-compose down -v
```

### View Logs

```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f app
docker-compose logs -f mongodb
docker-compose logs -f redis
```

### Rebuild Application Only

```bash
docker-compose up --build app
```

### Check Service Status

```bash
docker-compose ps
```

### Restart a Service

```bash
docker-compose restart app
```

## 🧪 Testing the API

### 1. Create a User (via UserController)

```bash
curl -X POST http://localhost:8080/school/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "age": 25,
    "credentials": {
      "username": "john@example.com",
      "password": "password123",
      "roles": ["ROLE_USER"]
    }
  }'
```

### 2. Login

```bash
curl -X POST http://localhost:8080/school/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "password123"
  }'
```

### 3. Use JWT Token

```bash
# Replace YOUR_JWT_TOKEN with the token from login response
curl -X GET http://localhost:8080/school/api/v1/users \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## 🔍 Troubleshooting

### Application Won't Start

1. **Check if ports are available**:
   ```bash
   # Windows
   netstat -ano | findstr :8080
   netstat -ano | findstr :27017
   netstat -ano | findstr :6379
   ```

2. **View application logs**:
   ```bash
   docker-compose logs app
   ```

### MongoDB Connection Issues

```bash
# Check MongoDB health
docker exec mongoredis-mongodb mongosh --eval "db.adminCommand('ping')"

# Access MongoDB shell
docker exec -it mongoredis-mongodb mongosh -u admin -p admin123
```

### Redis Connection Issues

```bash
# Check Redis health
docker exec mongoredis-redis redis-cli ping

# Access Redis CLI
docker exec -it mongoredis-redis redis-cli
```

### Clean Restart (⚠️ Deletes all data)

```bash
docker-compose down -v
docker-compose up --build
```

## 🔧 Configuration

### Environment Variables (docker-compose.yml)

You can modify these in `docker-compose.yml`:

```yaml
environment:
  # MongoDB
  SPRING_DATA_MONGODB_USERNAME: admin
  SPRING_DATA_MONGODB_PASSWORD: admin123
  
  # Redis
  SPRING_DATA_REDIS_HOST: redis
  SPRING_DATA_REDIS_PORT: 6379
  
  # Server
  SERVER_PORT: 8080
  
  # JWT Secret (⚠️ Change in production!)
  JWT_SECRET: your-super-secret-jwt-key
```

### Custom Ports

To change ports, edit `docker-compose.yml`:

```yaml
services:
  app:
    ports:
      - "9090:8080"  # External:Internal
```

## 📊 Monitoring

### Health Checks

```bash
# Application health
curl http://localhost:8080/school/actuator/health

# MongoDB health
docker exec mongoredis-mongodb mongosh --eval "db.adminCommand('ping')"

# Redis health
docker exec mongoredis-redis redis-cli ping
```

### Resource Usage

```bash
docker stats
```

## 🗂️ Data Persistence

Data is persisted in Docker volumes:

- **mongodb_data**: MongoDB database files
- **mongodb_config**: MongoDB configuration
- **redis_data**: Redis persistence files

### Backup MongoDB

```bash
docker exec mongoredis-mongodb mongodump --out /backup
docker cp mongoredis-mongodb:/backup ./backup
```

### Restore MongoDB

```bash
docker cp ./backup mongoredis-mongodb:/backup
docker exec mongoredis-mongodb mongorestore /backup
```

## 🚦 Production Deployment

### Important Security Changes

1. **Change MongoDB credentials** in `docker-compose.yml`
2. **Change JWT secret** environment variable
3. **Use environment-specific properties**
4. **Enable HTTPS**
5. **Configure proper logging**
6. **Set resource limits**:

```yaml
services:
  app:
    deploy:
      resources:
        limits:
          cpus: '1'
          memory: 1G
        reservations:
          cpus: '0.5'
          memory: 512M
```

## 📝 Notes

- The application uses a **local MongoDB** in Docker by default
- If you want to use **MongoDB Atlas** (cloud), modify `application-docker.properties`
- **Redis** is used for caching and JWT token storage
- All services are connected via a custom Docker network: `mongoredis-network`

## 🆘 Support

If you encounter issues:

1. Check logs: `docker-compose logs -f`
2. Verify all services are healthy: `docker-compose ps`
3. Ensure ports are not in use
4. Try a clean restart: `docker-compose down -v && docker-compose up --build`
