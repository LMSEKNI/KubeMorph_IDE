#!/bin/bash

# Retrieve the current IP address of the server
IP_ADDRESS=$(hostname -I | awk '{print $1}')

# Define the path to the Apache configuration file
APACHE_CONF="/etc/apache2/conf-available/servername.conf"

# Create or overwrite the servername.conf file with the new ServerName directive
echo "ServerName $IP_ADDRESS" | sudo tee $APACHE_CONF

# Enable the new configuration and reload Apache
sudo a2enconf servername
sudo systemctl reload apache2
