from flask import Flask, render_template, request

app = Flask(__name__)

@app.route("/", methods=["GET", "POST"])
def login():
    if request.method == "POST":
        username = request.form['username']
        return f"Hello, {username}!"
    return render_template("login.html")

if __name__ == "__main__":
    app.run(debug=True)

import mysql.connector

def get_db_connection():
    return mysql.connector.connect(
        host="localhost",      # Replace with your MySQL host
        user="your_username",  # Replace with your MySQL username
        password="your_password",  # Replace with your MySQL password
        database="your_database"  # Replace with your database name
    )

from flask import Flask, render_template, request, redirect, url_for, session
from flask_session import Session

app.config['SESSION_TYPE'] = 'filesystem'  # Or use 'redis' for more advanced storage
Session(app)


@app.route("/", methods=["GET", "POST"])
def login():
    if request.method == "POST":
        username = request.form['username']
        password = request.form['password']

        conn = get_db_connection()
        cursor = conn.cursor(dictionary=True)

        # Query to check user credentials (add password hashing in real scenarios)
        cursor.execute("SELECT * FROM users WHERE username = %s AND password = %s", (username, password))
        user = cursor.fetchone()

        conn.close()

        if user:
            session['user_id'] = user['id']
            session['role'] = user['role']  # Admin or Employee (stored in the database)
            return redirect(url_for('dashboard'))
        else:
            return "Invalid login. Please try again."

    return render_template("login.html")
