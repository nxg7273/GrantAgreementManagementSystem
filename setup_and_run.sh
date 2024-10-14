#!/bin/bash

# Run the existing setup script
./setup_dev_environment.sh

# Set up the database
sudo mysql << MYSQL_SCRIPT
CREATE DATABASE IF NOT EXISTS grant_management;
CREATE USER IF NOT EXISTS 'grantuser'@'localhost' IDENTIFIED BY 'grantpassword';
GRANT ALL PRIVILEGES ON grant_management.* TO 'grantuser'@'localhost';
FLUSH PRIVILEGES;
MYSQL_SCRIPT

# Run SQL scripts
mysql -u grantuser -pgrantpassword grant_management < create_tables.sql
mysql -u grantuser -pgrantpassword grant_management < insert_sample_data.sql

# Start the backend
cd backend
mvn spring-boot:run &

# Start the frontend
cd ../frontend
npm install
npm start &

# Wait for services to start
sleep 30

# Expose ports
echo "Exposing backend port..."
sudo iptables -t nat -A PREROUTING -p tcp --dport 80 -j REDIRECT --to-port 8080
echo "Exposing frontend port..."
sudo iptables -t nat -A PREROUTING -p tcp --dport 3000 -j REDIRECT --to-port 3000

echo "Setup complete. You can now access:"
echo "Backend: http://$(curl -s ifconfig.me):80"
echo "Frontend: http://$(curl -s ifconfig.me):3000"
