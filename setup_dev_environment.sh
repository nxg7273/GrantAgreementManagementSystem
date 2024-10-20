#!/bin/bash

# Update system packages
sudo apt-get update
sudo apt-get upgrade -y

# Install Java Development Kit
sudo apt-get install -y openjdk-11-jdk

# Install Node.js and npm
curl -fsSL https://deb.nodesource.com/setup_20.x | sudo -E bash -
sudo apt-get install -y nodejs

# Install MySQL
sudo apt-get install -y mysql-server

# Install Maven
sudo apt-get install -y maven

# Install Git
sudo apt-get install -y git

# Navigate to the project root directory
cd /home/ubuntu/GrantAgreementManagementSystem

# Set up backend
if [ -d "backend" ]; then
    cd backend
    mvn clean install
    cd ..
else
    echo "Backend directory not found. Please check the project structure."
fi

# Set up frontend
if [ -d "frontend" ]; then
    cd frontend
    npm install
    cd ..
else
    echo "Frontend directory not found. Please check the project structure."
fi

echo "Development environment setup complete!"
