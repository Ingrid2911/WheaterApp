# 🌦️ Weather App

A Java-based console application that allows users to create accounts which contains username, password, current location (longitutde and latitude) and a role for the user. The user can ask for the whater in the current location. If the role for the user is admin, he can add a json file with other weather data which will be automatically added in the database. Also, the user can choose the option to update his location, so the weather forecast will be for the new location. Communication is handled via WebSocket.
## 🔧 Technologies Used
- Java
- cool.scx.data.jdbc.sqlite2 (SQLite JDBC driver)
- google.code.gson12 (Gson for JSON parsing)
- WebSocket
- JSON
- SQL

## 🚀 Features
- Account creation (username + password + current location)
- User login and authentication
- Save and update user location
- Get weather data for the current saved location
- Role management: `admin` / `regular user`
- Persistent data storage in SQL database

## 🧠 What I Learned
- Real-time communication with WebSocket
- Working with relational databases in Java
- Managing user sessions and permissions
- Parsing JSON for weather data extraction

## 🏗️ Project Status
✅ Completed  
📌 Solo project
