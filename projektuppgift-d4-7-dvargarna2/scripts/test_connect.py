import mysql.connector as mariadb
from tabulate import tabulate

# Function to handle user login
def user_login():
    print("Welcome to the Car Log System")
    print("Username: admin| Password: admin123")
    print("Username: driver| Password driver123")
    while True:
        username = input("Enter username: ")
        password = input("Enter password: ")

        # Simulated user roles (in a real-world application, you would query this from a database)
        if username == "admin" and password == "admin123":
            return "admin"
        elif username == "driver" and password == "driver123":
            return "driver"
        else:
            print("Invalid credentials. Please try again.")

# Function to display car log info for a specific regnr
def display_car_info(cursor, regnr):
    sql_statement = "SELECT * FROM carlog_db WHERE regnr = %s"
    cursor.execute(sql_statement, (regnr,))
    my_result = cursor.fetchall()
    if my_result:
        print("Information:")
        print(tabulate(my_result, headers=["regnr", "make", "model", "color", "current_place", "future_place", 'pickup_date_time', 'delivery_date_time', "pickup_status", "company_name"], tablefmt="sql"))
    else:
        print(f"No car logs found for registreringsnummer: {regnr}.")

# Admin functionality to add a car
def add_car(cursor):
    print("Add a New Car to the Car Log System")
    
    # Gather information from admin
    regnr = input("Enter registration number: ").strip().upper()
    make = input("Enter car make: ").strip()
    model = input("Enter car model: ").strip()
    color = input("Enter car color: ").strip()
    current_place = input("Enter current place: ").strip()
    future_place = input("Enter future place: ").strip()
    pickup_date_time = input("Enter pickup date and time (YYYY-MM-DD HH:MM:SS): ").strip()
    delivery_date_time = input("Enter delivery date and time (YYYY-MM-DD HH:MM:SS): ").strip()
    pickup_status = "Pending"  # Default status when adding new car
    company_name = input("Enter company name: ").strip()
    
    # Create an INSERT SQL statement
    sql_statement = """
    INSERT INTO carlog_db (regnr, make, model, color, current_place, future_place, pickup_date_time, delivery_date_time, pickup_status, company_name)
    VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
    """
    
    # Execute the statement
    cursor.execute(sql_statement, (regnr, make, model, color, current_place, future_place, pickup_date_time, delivery_date_time, pickup_status, company_name))
    
    # Commit the transaction to the database
    print(f"Car with registration number {regnr} has been successfully added.")

# Admin functionality to update car info
def admin_actions(cursor):
    print("Admin Actions:")
    print("1. Add a new car")
    print("2. Update car information")
    
    choice = input("Choose an action: ").strip()

    if choice == "1":
        add_car(cursor)  # Call the function to add a car
    elif choice == "2":
        regnr = input("Enter registration number for the car you want to update: ").strip().upper()
        print("What would you like to update?")
        print("1. Color")
        print("2. Current place")
        print("3. Future place")
        print("4. Pickup time")
        print("5. Delivery date")
        print("6. Status")

        update_choice = input("Select an option: ").strip()

        if update_choice == "1":
            new_color = input("Enter new color: ").strip()
            cursor.execute("UPDATE carlog_db SET color = %s WHERE regnr = %s;", (new_color, regnr))
        elif update_choice == "2":
            new_current_place = input("Enter new current place: ").strip()
            cursor.execute("UPDATE carlog_db SET current_place = %s WHERE regnr = %s;", (new_current_place, regnr))
        elif update_choice == "3":
            new_future_place = input("Enter new future place: ").strip()
            cursor.execute("UPDATE carlog_db SET future_place = %s WHERE regnr = %s;", (new_future_place, regnr))
        elif update_choice == "4":
            new_pickup_date_time = input("Enter new pickup time (YYYY-MM-DD HH:MM:SS): ").strip()
            cursor.execute("UPDATE carlog_db SET pickup_date_time = %s WHERE regnr = %s;", (new_pickup_date_time, regnr))
        elif update_choice == "5":
            new_delivery_date_time = input("Enter new delivery date (YYYY-MM-DD HH:MM:SS): ").strip()
            cursor.execute("UPDATE carlog_db SET delivery_date_time = %s WHERE regnr = %s;", (new_delivery_date_time, regnr))
        elif update_choice == "6":
            print("Select a new status:")
            status_options = {1: "Pending", 2: "In Progress", 3: "Completed"}
            for key, value in status_options.items():
                print(f"{key}: {value}")
            while True:
                try:
                    status_choice = int(input("Enter the number for the new status: "))
                    if status_choice in status_options:
                        new_status = status_options[status_choice]
                        cursor.execute("UPDATE carlog_db SET pickup_status = %s WHERE regnr = %s;", (new_status, regnr))
                        print(f"Status updated to {new_status}.")
                        break
                    else:
                        print("Invalid choice. Please select a valid number.")
                except ValueError:
                    print("Please enter a valid number.")
        else:
            print("Invalid choice.")
    else:
        print("Invalid action selected.")


# Chauffeur functionality
def driver_actions(cursor, regnr):
    print("Driver Actions: You can only change status.")
    print("Select a new status:")
    status_options = {1: "Pending", 2: "Completed", 3: "In Progress", 4: "Canceled"}
    for key, value in status_options.items():
        print(f"{key}: {value}")
    while True:
        try:
            status_choice = int(input("Enter the number corresponding to the new status: "))
            if status_choice in status_options:
                new_status = status_options[status_choice]
                cursor.execute("UPDATE carlog_db SET pickup_status = %s WHERE regnr = %s", (new_status, regnr))
                print(f"Status updated to {new_status}")
                break
            else:
                print("Invalid choice. Please select a valid number.")
        except ValueError:
            print("Please enter a number.")

# Main program
def main():
    try:
        conn = mariadb.connect(
            user="root",
            password="p", # OBS vi kör olika password!!!!!!!!!!!
            database="carlog", # OBS vi kör olika namn på database!!!!!!!!!!!
            host="127.0.0.1",
            port="3306",
            charset="utf8mb4",
            collation="utf8mb4_general_ci",
        )
        cur = conn.cursor()

        # User login (only happens once)
        role = user_login()

        while True:
            # Get car registration number from user
            cur.execute("SELECT regnr FROM carlog_db")
            print("Available registreringsnummer: ")
            for regnr in cur:
                print(f"{regnr[0]}")  # regnr is a tuple, so we print the first element
            nummer = input("Registreringsnummer: ").replace(" ", "").lower()

            # Display car information for the specific registreringsnummer
            display_car_info(cur, nummer)

            # Role-based actions
            if role == "admin":
                admin_actions(cur)
            elif role == "driver":
                driver_actions(cur, nummer)

            # Commit changes
            conn.commit()

            # Ask if the user wants to continue after completing actions
            continue_program = input("Do you want to perform another action? (yes/no): ").lower()
            if continue_program != "yes":
                print("Logging out. Goodbye!")
                break

        # Close the connection
        cur.close()
        conn.close()

    except mariadb.Error as e:
        print(f"Error: {e}")

# Run the program
if __name__ == "__main__":
    main()