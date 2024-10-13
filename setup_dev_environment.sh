#!/bin/bash

# Update system packages
sudo apt-get update
sudo apt-get upgrade -y

# Install Java Development Kit
sudo apt-get install -y openjdk-11-jdk

# Install Node.js and npm
curl -sL https://deb.nodesource.com/setup_14.x | sudo -E bash -
sudo apt-get install -y nodejs

# Install MySQL
sudo apt-get install -y mysql-server

# Install Maven
sudo apt-get install -y maven

# Install Git
sudo apt-get install -y git

# Clone the project repository (replace with actual repository URL)
git clone https://github.com/your-org/grant-agreement-system.git

# Set up backend
cd grant-agreement-system/backend
mvn clean install

# Set up frontend
cd ../frontend
npm install

echo "Development environment setup complete!"
